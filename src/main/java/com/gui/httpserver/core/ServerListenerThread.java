package com.gui.httpserver.core;

import com.gui.httpserver.HttpServer;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import org.slf4j.Logger;

public class ServerListenerThread extends Thread {
    private final static Logger LOGGER = LoggerFactory.getLogger(HttpServer.class);

    ServerSocket serverSocket;

    public ServerListenerThread(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
    }

    @Override
    public void run() {
        try {
            while (serverSocket.isBound() && !serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                LOGGER.info(" * Connection Accepted: " + socket.getInetAddress() + " *");
                HttpConnectionWorkerThread workerthread = new HttpConnectionWorkerThread(socket);
                workerthread.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if ( serverSocket != null ){
                try {
                    serverSocket.close();
                } catch (IOException ignored) {
                }
            }
        }
    }
}
