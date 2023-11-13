package cse.project.team.server;

import com.sun.net.httpserver.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.*;

public class server {
    private static final int SERVER_PORT = 8100;
    private static final String SERVER_HOSTNAME = "localhost";
    private static genI generation;
    static HttpServer server;

    public server(genI genI) throws IOException{
        generation = genI;
        init();
    }

    public static void main(String[] args) throws IOException {
        generation = new genMock();
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

        server.createContext("/", new RequestHandler(generation));
        server.setExecutor(threadPoolExecutor);
        server.start();

        System.out.println("Server started on port " + SERVER_PORT);

    }

    public void stop() {
        server.stop(0);
    }
}