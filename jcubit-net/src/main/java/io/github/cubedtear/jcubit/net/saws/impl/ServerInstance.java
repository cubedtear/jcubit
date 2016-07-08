package io.github.cubedtear.jcubit.net.saws.impl;

import com.google.common.collect.Maps;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Primitives;
import io.github.cubedtear.jcubit.net.saws.api.HandlerFactory;
import io.github.cubedtear.jcubit.bds.BDS;
import io.github.cubedtear.jcubit.bds.BDSUtil;
import io.github.cubedtear.jcubit.bds.SerializationException;
import io.github.cubedtear.jcubit.logging.OSLogger;
import io.github.cubedtear.jcubit.logging.core.ILogger;
import io.github.cubedtear.jcubit.logging.core.LogLevel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Map;

/**
 * @author Aritz Lopez
 */
public class ServerInstance implements Runnable {

    public static final byte[] INITIAL_MAGIC = new byte[]{0xB, 0xE, 0xE, 0xF};
    public static final byte[] FINAL_MAGIC = new byte[]{0xF, 0xA, 0xC, 0xE};
    private static ILogger LOG = new OSLogger.Builder(System.err, "Server").setLevel(LogLevel.INFO).build();
    private final Map<String, Object> handlers;
    private final InputStream inStream;
    private final OutputStream outStream;
    private final Socket s;

    public ServerInstance(Map<String, HandlerFactory> handlers, Socket s) throws IOException {
        this.inStream = s.getInputStream();
        this.outStream = s.getOutputStream();
        this.s = s;
        this.handlers = Maps.newHashMap();
        for (Map.Entry<String, HandlerFactory> entry : handlers.entrySet()) {
            this.handlers.put(entry.getKey(), entry.getValue().create(s.getInetAddress().getHostAddress(), s.getPort()));
        }
    }

    public static void setLogger(ILogger logger) {
        ServerInstance.LOG = logger;
    }

    @Override
    public void run() {
        while (!this.s.isClosed()) {
            try {
                byte[] data = PacketReader.readPacket(inStream);
                if (data == null) continue;
                handlePacket(data);
            } catch (SocketException ignored) {
            } catch (IOException e) {
                LOG.e("Error reading from socket.", e);
                break;
            }
        }
        if (this.s.isClosed()) {
            LOG.i("Connection with {}:{} has been closed.", s.getInetAddress().getHostAddress(), s.getPort());
        }
    }

    private void handlePacket(byte[] payload) {
        BDS bds = BDS.load(payload);

        if (!"Request".equals(bds.getName())) {
            LOG.e("Packet does not have required format!");
            return;
        }

        String interfaceName = bds.getString("Interface");
        String methodName = bds.getString("Name");
        String returnType = bds.getString("ReturnValue");
        if (!handlers.containsKey(interfaceName)) {
            LOG.e("Handler for interface {} is not registered, when running method {}!", interfaceName, methodName);
            return;
        }

        int argc = bds.getInt("Argc");
        Object[] args = new Object[argc];
        Class<?>[] types = new Class[argc];
        boolean hasNull = false;
        for (int i = 0; i < argc; i++) {
            BDS argi = bds.getBDS("Arg" + i);
            if (argi == null) {
                LOG.e("Illegal argument count: Expected {} but argument {} does not exits!", argc, i);
                return;
            }
            try {
                Object arg = BDSUtil.deserialize(argi);
                args[i] = arg;
                types[i] = arg != null ? arg.getClass() : null;
                hasNull |= arg == null;
            } catch (SerializationException e) {
                LOG.e("Error deserializing argument {} of {}", e, i, argc);
            }
        }

        LOG.d("Running method " + methodName + " of interface " + interfaceName);
        LOG.d("With arguments: " + Arrays.toString(args));

        try {
            Class<?> inter = Class.forName(interfaceName);

            Method m;
            if (hasNull) {
                m = null;
                for (Method me : inter.getDeclaredMethods()) {
                    if (methodName.equals(me.getName())) {
                        Class<?>[] parameterTypes = me.getParameterTypes();
                        boolean good = true;
                        for (int i = 0; good && i < parameterTypes.length; i++) {
                            Class<?> paramType = parameterTypes[i];
                            good = types[i] == null || types[i].equals(paramType) || (Primitives.isWrapperType(types[i]) && Primitives.unwrap(types[i]).equals(paramType)) || (types[i].isPrimitive() && Primitives.wrap(types[i]).equals(paramType));
                        }
                        if (good) {
                            m = me;
                            break;
                        }
                    }
                }
                if (m == null) {
                    LOG.e("Method {} of interface {} could not be found!", methodName, interfaceName);
                    return;
                }
            } else {
                try {
                    m = inter.getDeclaredMethod(methodName, types);
                    if (!m.isAccessible()) {
                        try {
                            m.setAccessible(true);
                        } catch (SecurityException e) {
                            LOG.e("Method {} of interface {} could not be made accesible!", e, m.getName(), inter.getName());
                            return;
                        }
                    }
                } catch (NoSuchMethodException e) {
                    LOG.e("Method {} could not be found on interface {}!", e, methodName, inter.getName());
                    return;
                }
            }
            try {
                Object implementor = handlers.get(interfaceName);
                Object ret = m.invoke(implementor, args);
                sendResponse(interfaceName, methodName, ret);
            } catch (IllegalAccessException e) {
                LOG.e("Method {} of interface {} could not be made accesible!", e, m.getName(), inter.getName());
            } catch (InvocationTargetException e) {
                sendException(interfaceName, methodName, e.getCause());
                LOG.e("Method {} of interface {} threw an exception!", e, m.getName(), inter.getName());
            }

        } catch (ClassNotFoundException e) {
            LOG.e("Interface {} could not be found !", e, interfaceName);
        }
    }

    private void sendException(String interfaceName, String methodName, Throwable exception) {
        BDS response = BDS.createEmpty("Response");
        response.addString("Interface", interfaceName);
        response.addString("Name", methodName);
        try {
            BDS value = BDSUtil.serialize(exception);
            value.setName("Exception");
            response.addBDS(value);
            sendPacket(response.write());
        } catch (SerializationException e) {
            LOG.e("Error serializing exception", e);
        }
    }

    private void sendResponse(String interfaceName, String methodName, Object ret) {
        BDS response = BDS.createEmpty("Response");
        response.addString("Interface", interfaceName);
        response.addString("Name", methodName);
        try {
            BDS value = BDSUtil.serialize(ret);
            value.setName("Value");
            response.addBDS(value);
            sendPacket(response.write());
        } catch (SerializationException e) {
            LOG.e("Error serializing return value", e);
        }
    }

    private void sendPacket(byte[] data) {
        try {
            outStream.write(ServerInstance.INITIAL_MAGIC);
            outStream.write(Ints.toByteArray(data.length));
            outStream.write(data);
            outStream.write(ServerInstance.FINAL_MAGIC);
        } catch (IOException e) {
            LOG.e("Packet could not be sent", e);
        }
    }
}
