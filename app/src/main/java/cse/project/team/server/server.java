package cse.project.team.server;

import com.sun.net.httpserver.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.*;
import java.util.Map;
import java.util.HashMap;

public class server {
    private static final int SERVER_PORT = 8100;
    private static final String SERVER_HOSTNAME = "localhost";
    static HttpServer server;
    
    public server() throws IOException{
        init();
    }

    public static void main(String[] args) throws IOException {
        init();
    }

    public static void init() throws IOException{
        // create a thread pool to handle requests
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);

        // create a server
        server = HttpServer.create(
                new InetSocketAddress(SERVER_HOSTNAME, SERVER_PORT),
                0); /*
                     * this code creates an HTTP server instance
                     * that is bound to the local address (localhost)
                     * and the specified port number (8100). Once the
                     * server is created, it can listen for incoming
                     * HTTP requests and route them to the appropriate
                     * handlers.
                     */
        Map<String, String> data = new HashMap<>();
        server.createContext("/db/", new DBHandler("Main_CSE110_Proj"));
        server.createContext("/account/",new accountHandler("Main_CSE110_Proj"));
        server.createContext("/gen/", new GenHandler(new genAPI()));

        server.createContext("/share/",new shareHandler(data));
        server.setExecutor(threadPoolExecutor);
        server.start();

        System.out.println("Server started on port " + SERVER_PORT);

    }

    public void stop() {
        server.stop(0);
    }
}