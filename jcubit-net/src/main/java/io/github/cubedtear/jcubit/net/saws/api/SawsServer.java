package io.github.cubedtear.jcubit.net.saws.api;

import io.github.cubedtear.jcubit.net.saws.impl.ServerInstance;
import io.github.cubedtear.jcubit.logging.OSLogger;
import io.github.cubedtear.jcubit.logging.core.ILogger;
import io.github.cubedtear.jcubit.logging.core.LogLevel;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Aritz Lopez
 */
public class SawsServer implements Runnable {

    private static ILogger LOG = new OSLogger.Builder(System.err, "Server").setLevel(LogLevel.INFO).build();
    private final ServerSocket serverSocket;
    private Map<String, HandlerFactory> handlers;
    private Thread listenerThread = null;
    private volatile boolean running = false;



    private SawsServer(ServerSocket serverSocket, Map<String, HandlerFactory> handlers) {
        this.serverSocket = serverSocket;
        this.handlers = handlers;
    }

    public static <T> SawsServer singleConstant(int port, Class<T> inter, T instance) throws IOException {
        Map<String, HandlerFactory> map = new HashMap<>();
        map.put(inter.getName(), new ConstantHandler<>(instance));
        return new SawsServer(new ServerSocket(port), map);
    }

    public static <T> SawsServer singleConstant(ServerSocket serverSocket, Class<T> inter, T instance) {
        Map<String, HandlerFactory> map = new HashMap<>();
        map.put(inter.getName(), new ConstantHandler<>(instance));
        return new SawsServer(serverSocket, map);
    }

    public static <T> SawsServer singleFactory(int port, Class<T> inter, HandlerFactory<T> factory) throws IOException {
        Map<String, HandlerFactory> map = new HashMap<>();
        map.put(inter.getName(), factory);
        return new SawsServer(new ServerSocket(port), map);
    }

    public static <T> SawsServer singleFactory(ServerSocket serverSocket, Class<T> inter, HandlerFactory<T> factory) {
        Map<String, HandlerFactory> map = new HashMap<>();
        map.put(inter.getName(), factory);
        return new SawsServer(serverSocket, map);
    }

    public static SawsServer multipleConstants(int port, Map<Class, Object> handlers) throws IOException {
        Map<String, HandlerFactory> map = new HashMap<>();
        for (Map.Entry<Class, Object> entry : handlers.entrySet()) {
            map.put(entry.getKey().getName(), new ConstantHandler<>(entry.getValue()));
        }
        return new SawsServer(new ServerSocket(port), map);
    }

    public static SawsServer multipleConstants(ServerSocket serverSocket, Map<Class, Object> handlers) {
        Map<String, HandlerFactory> map = new HashMap<>();
        for (Map.Entry<Class, Object> entry : handlers.entrySet()) {
            map.put(entry.getKey().getName(), new ConstantHandler<>(entry.getValue()));
        }
        return new SawsServer(serverSocket, map);
    }

    public static SawsServer multipleFactories(int port, Map<Class, HandlerFactory> handlers) throws IOException {
        Map<String, HandlerFactory> map = new HashMap<>();
        for (Map.Entry<Class, HandlerFactory> entry : handlers.entrySet()) {
            map.put(entry.getKey().getName(), entry.getValue());
        }
        return new SawsServer(new ServerSocket(port), map);
    }

    public static SawsServer multipleFactories(ServerSocket serverSocket, Map<Class, HandlerFactory> handlers) {
        Map<String, HandlerFactory> map = new HashMap<>();
        for (Map.Entry<Class, HandlerFactory> entry : handlers.entrySet()) {
            map.put(entry.getKey().getName(), entry.getValue());
        }
        return new SawsServer(serverSocket, map);
    }

    public synchronized void start() {
        if (running || listenerThread != null) throw new IllegalStateException("Server is already started!");

        listenerThread = new Thread(this, "SAWS-Connection-Listener");
        listenerThread.start();
    }

    /**
     * Stops the server from listening to any more connections.
     * <ul>
     *     <li>If the server <b>is not running</b>, it does nothing and immediately returns {@code true}.</li>
     *     <li>If the server <b>is running</b>, closes the socket and waits at most 2 seconds until the thread ends.</li>
     * </ul>
     * @return {@code true} if the server was already closed, or was closed successfully. <p>
     * {@code false} does not guarantee that the server has not been closed, only that an error happened.
     */
    public synchronized boolean stop() {
        if (!running || this.listenerThread == null || this.serverSocket.isClosed()) return true;
        running = false;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        try {
            this.listenerThread.join(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    @Override
    public void run() {
        running = true;
        while (running) {
            try {
                Socket s = this.serverSocket.accept();
                LOG.i("Connected to client {}:{}.", s.getInetAddress().getHostAddress(), s.getPort());
                new Thread(new ServerInstance(handlers, s), "server").start();
            } catch (SocketException ignored) {
                // Socket was closed
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
