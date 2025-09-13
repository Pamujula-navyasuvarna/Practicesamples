package com.example.app;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import java.net.InetSocketAddress;
import java.io.OutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Minimal HTTP server using JDK's com.sun.net.httpserver.HttpServer.
 * - Listens on port 8080 (configurable via --port system property)
 * - GET /       -> returns simple JSON message
 * - GET /health -> returns 200 OK and JSON {"status":"UP"}
 */
public class App {
    public static void main(String[] args) throws Exception {
        int port = 8080;
        String portProp = System.getProperty("port");
        if (portProp != null) {
            try { port = Integer.parseInt(portProp); } catch (NumberFormatException ignored) {}
        }

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/", exchange -> {
            if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                String body = "{\"message\":\"Hello from minimal Java app\",\"path\":\"/\"}";
                sendJson(exchange, 200, body);
            } else {
                exchange.sendResponseHeaders(405, -1); // Method Not Allowed
            }
        });
      server.createContext("/health", exchange -> {
            if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                String body = "{\"status\":\"UP\"}";
                sendJson(exchange, 200, body);
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        });

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down server...");
            server.stop(0);
        }));

        System.out.printf("Starting server on port %d%n", port);
        server.start();
    }

private static void sendJson(HttpExchange exchange, int statusCode, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}
