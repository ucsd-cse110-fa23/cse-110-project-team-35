package cse.project.team.server;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.sun.net.httpserver.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

import org.bson.Document;

public class server {

    // initialize server port and hostname
    private static final int SERVER_PORT = 8100;
    private static final String SERVER_HOSTNAME = "localhost";

    public static void main(String[] args) throws IOException {

        // create a thread pool to handle requests
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);

        // create a server
        HttpServer server = HttpServer.create(
                new InetSocketAddress(SERVER_HOSTNAME, SERVER_PORT),
                0); /*
                     * this code creates an HTTP server instance
                     * that is bound to the local address (localhost)
                     * and the specified port number (8100). Once the
                     * server is created, it can listen for incoming
                     * HTTP requests and route them to the appropriate
                     * handlers.
                     */

        server.createContext("/", new RequestHandler());
        server.setExecutor(threadPoolExecutor);
        server.start();

        System.out.println("Server started on port " + SERVER_PORT);

    }

}