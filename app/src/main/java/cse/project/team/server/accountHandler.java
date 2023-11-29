package cse.project.team.server;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import com.sun.net.httpserver.*;

import static com.mongodb.client.model.Filters.eq;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.stream.Collectors;

import org.bson.Document;
import org.bson.conversions.Bson;

public class accountHandler implements HttpHandler {
    private MongoCollection<Document> accountCollection;

    public accountHandler(String dataBase) {
        MongoClient mongoClient = MongoClients
                .create("mongodb://yig017:Gym201919@ac-lzsqbrn-shard-00-00.cfigpzh.mongodb.net:27017,ac-lzsqbrn-shard-00-01.cfigpzh.mongodb.net:27017,ac-lzsqbrn-shard-00-02.cfigpzh.mongodb.net:27017/?ssl=true&replicaSet=atlas-9thc6y-shard-0&authSource=admin&retryWrites=true&w=majority");
        MongoDatabase db = mongoClient.getDatabase(dataBase);
        this.accountCollection = db.getCollection("accounts");
    }

    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "Request Received";
        String method = httpExchange.getRequestMethod();

        try {
            if (method.equals("GET")) {
                response = handleGet(httpExchange);
            } else if (method.equals("POST")) {
                response = handlePost(httpExchange);
            } else if (method.equals("DELETE")) {
                response = handleDelete(httpExchange);
            } else if (method.equals("PUT")) {
                response = handlePut(httpExchange);
            } else {
                throw new Exception("Not Valid Request Method");
            }
        } catch (Exception e) {
            response = e.toString();
            e.printStackTrace();
        }

        // Sending back response to the client
        httpExchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream outStream = httpExchange.getResponseBody();
        outStream.write(response.getBytes());
        outStream.flush();
        outStream.close();
    }

    private String handleGet(HttpExchange httpExchange) throws IOException {
        URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery();

        if (query != null) {
            String title = URLDecoder.decode(query.substring(query.indexOf("=") + 1), "UTF-8");
            return getRecDetail(title);
        } else {
            return getRecList();
        }
    }

    private String handlePost(HttpExchange httpExchange) throws IOException {
        InputStream inStream = httpExchange.getRequestBody();
        Scanner scanner = new Scanner(inStream);
        String postData = scanner.toString();
        String title = postData.substring(
                0,
                postData.indexOf(",")), details = postData.substring(postData.indexOf(",") + 1);
        scanner.close();
        return doPost(title, details);
    }

    private String handlePut(HttpExchange httpExchange) throws IOException {
        InputStream inStream = httpExchange.getRequestBody();
        BufferedReader in = new BufferedReader(new InputStreamReader(inStream));
        String postData = in.lines().collect(Collectors.joining("\n"));
        String title = postData.substring(
                0,
                postData.indexOf(",")), details = postData.substring(postData.indexOf(",") + 1);
        in.close();

        return doPost(title, details);
    }

    private String handleDelete(HttpExchange httpExchange) throws IOException {
        URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery();
        String response = "Could not delete";
        if (query != null) {
            String title = URLDecoder.decode(query.substring(query.indexOf("=") + 1), "UTF-8");
            doDelete(title);
            response = "Deleted Entry";

        }
        return response;
    }

    public String getRecDetail(String title) {
        Document target = accountCollection.find(eq("title", title)).first();
        return (target == null) ? "Does not exist" : target.getString("description");
    }

    public String getRecList() {
        StringBuilder response = new StringBuilder();
        List<Document> recipes = accountCollection.find().into(new ArrayList<>());
        for (Document i : recipes) {
            response.append("*" + i.getString("title"));
        }
        response.delete(0, 1);
        return response.toString();
    }

    public String doPost(String title, String details) {
        if (getRecDetail(title).equals("Does not exist")) {
            Bson filter = eq("title", title);
            Bson updateOperation = com.mongodb.client.model.Updates.set("description", details);
            UpdateOptions options = new UpdateOptions().upsert(true);
            accountCollection.updateOne(filter, updateOperation, options);
            return "Added";
        }
        return "Username taken";
    }

    public void doDelete(String title) {
        Bson filter = eq("title", title);
        accountCollection.deleteOne(filter);
    }

    public void clear() {
        accountCollection.deleteMany(new Document());
    }
}