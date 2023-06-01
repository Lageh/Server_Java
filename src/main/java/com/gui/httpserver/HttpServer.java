package com.gui.httpserver;

import com.gui.httpserver.config.Configuration;
import com.gui.httpserver.config.ConfigurationManager;
import com.gui.httpserver.core.ServerListenerThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class HttpServer {
    private final static Logger LOGGER = LoggerFactory.getLogger(HttpServer.class);
    public static void main(String[] args) {
        LOGGER.info("Server Starting...");

        ConfigurationManager.getInstance().loadConfigurationFile("src/main/resources/http.json");

        Configuration conf = ConfigurationManager.getInstance().getCurrentConfiguration();

        LOGGER.info("Port: " + ConfigurationManager.getInstance().getCurrentConfiguration().getPort());
        LOGGER.info("Webroot: " + ConfigurationManager.getInstance().getCurrentConfiguration().getWebroot());
        LOGGER.info("Username: " + ConfigurationManager.getInstance().getCurrentConfiguration().getUsername());

        try {
            ServerListenerThread serverListenerThread = new ServerListenerThread(conf.getPort());
            serverListenerThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
