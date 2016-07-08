package io.github.cubedtear.jcubit.net.saws.impl;

import com.google.common.primitives.Ints;
import io.github.cubedtear.jcubit.bds.BDS;
import io.github.cubedtear.jcubit.bds.BDSUtil;
import io.github.cubedtear.jcubit.bds.SerializationException;
import io.github.cubedtear.jcubit.logging.core.ILogger;
import io.github.cubedtear.jcubit.logging.core.NullLogger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;

/**
 * @author Aritz Lopez
 */
public class RemoteRunner implements InvocationHandler {


    private final Socket s;
    private final OutputStream outStream;
    private final InputStream inStream;
    private final ILogger LOG;

    public RemoteRunner(Socket s) throws IOException {
        this(s, new NullLogger());
    }

    public RemoteRunner(Socket s, ILogger logger) throws IOException {
        this.s = s;
        this.inStream = s.getInputStream();
        this.outStream = s.getOutputStream();
        this.LOG = logger;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        String interfaceName = method.getDeclaringClass().getName();

        LOG.d("Invoking method {} of {} with args: {}", methodName, method.getDeclaringClass(), Arrays.toString(args));
        OutputStream outStream = s.getOutputStream();
        BDS bds = BDS.createEmpty("Request");
        bds.addString("Interface", interfaceName);
        bds.addString("Name", methodName);
        bds.addString("ReturnType", method.getReturnType().getName());
        bds.addInt("Argc", args != null ? args.length : 0);
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                BDS object = BDSUtil.serialize(args[i]);
                object.setName("Arg" + i);
                bds.addBDS(object);
            }
        }
        sendPacket(outStream, bds);
        return waitForResponse(interfaceName, methodName);
    }

    private Object waitForResponse(String interfaceName, String methodName) throws Throwable {
        while (!this.s.isClosed()) {
            try {
                byte[] data = PacketReader.readPacket(inStream);
                return handlePacket(interfaceName, methodName, data);
            } catch (SocketException ignored) {
            } catch (IOException e) {
                LOG.e("Error reading from socket.", e);
                break;
            }
        }
        if (this.s.isClosed()) {
            LOG.i("Connection with {}:{} has been closed.", s.getInetAddress().getHostAddress(), s.getPort());
        }
        return null;
    }

    private Object handlePacket(String interfaceName, String methodName, byte[] payload) throws Throwable {
        BDS bds = BDS.load(payload);

        if (!"Response".equals(bds.getName())) {
            LOG.e("Packet does not have required format!");
            return null;
        }

        if (!(interfaceName.equals(bds.getString("Interface"))) || !(methodName.equals(bds.getString("Name")))) {
            LOG.e("Packet for unexpected method arrived!");
        }
        if (bds.getBDS("Value") != null) {
            BDS value = bds.getBDS("Value");
            try {
                return BDSUtil.deserialize(value);
            } catch (SerializationException e) {
                LOG.e("Error deserializing return value!", e);
            }
        } else if (bds.getBDS("Exception") != null) {
            BDS exception = bds.getBDS("Exception");
            try {
                throw (Throwable) BDSUtil.deserialize(exception);
            } catch (SerializationException e) {
                LOG.e("Error deserializing thrown exception!", e);
            }
        } else {
            LOG.e("Packet does not have required format!");
        }
        return null;
    }

    private static void sendPacket(OutputStream os, BDS bds) throws IOException {
        os.write(ServerInstance.INITIAL_MAGIC);
        byte[] data = bds.write();
        os.write(Ints.toByteArray(data.length));
        os.write(data);
        os.write(ServerInstance.FINAL_MAGIC);
    }
}
