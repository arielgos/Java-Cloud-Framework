package com.agos;

import com.agos.server.FileHandler;
import com.agos.server.HttpHandler;
import com.agos.server.ServiceHandler;
import com.agos.util.Configuration;
import com.agos.util.Log;
import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;

/**
 * Created by arielgos on 12/30/16.
 */
public class Main {

    public static void main(String[] args) {

        try {
            Configuration config = new Configuration();
            String root = config.get("fs.root");
            String path = config.get("fs.path");
            int port = Integer.parseInt(config.get("http.port"));
            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
            //Main Handler
            server.createContext("/", new HttpHandler());
            //Service Handler
            server.createContext("/ws/", new ServiceHandler());
            //File Handler
            server.createContext("/" + path + "/", new FileHandler(root, path));
            server.start();
            Log.d("Server Start at port : " + port);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
