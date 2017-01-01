package com.agos.server;

import com.agos.util.Log;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URI;

/**
 * Created by arielgos on 12/31/16.
 */
public class ServiceHandler implements com.sun.net.httpserver.HttpHandler {

    public void handle(HttpExchange t) throws IOException {
        URI uri = t.getRequestURI();
        Log.d("Path: " + uri.getPath());
        String path = uri.getPath().replace("/ws/", "");
        String[] parameters = path.split("\\/");
        String service = "default";
        if (parameters.length > 0) {
            service = parameters[0];
        }
        service = service.substring(0, 1).toUpperCase() + service.substring(1);
        String method = "value";
        if (parameters.length > 1) {
            method = parameters[1];
        }
        method = t.getRequestMethod().toLowerCase() + method.substring(0, 1).toUpperCase() + method.substring(1);
        ClassLoader loader = ServiceHandler.class.getClassLoader();
        try {
            Class serviceClass = loader.loadClass("com.agos.ws." + service);
            Log.d("Service Loaded : " + serviceClass.getName());
            try {
                Method serviceMethod = serviceClass.getMethod(method);
                Log.d("Method Loaded : " + serviceMethod.getName());
                String response = new Gson().toJson(serviceMethod.invoke(serviceClass.newInstance()));
                Log.d(response);
                t.getResponseHeaders().add("content-type", "application/json");
                t.sendResponseHeaders(200, response.length());
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
