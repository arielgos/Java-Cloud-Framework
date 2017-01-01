package com.agos.server;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by arielgos on 12/31/16.
 */
public class HttpHandler implements com.sun.net.httpserver.HttpHandler {
    public void handle(HttpExchange t) throws IOException {
        String response = "Hello, World!\n";
        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}