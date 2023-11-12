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

public class RequestHandler implements HttpHandler {
    private genAPI generation;
    private MongoCollection<Document> recipeCollection;

    public RequestHandler() {
        generation = new genAPI();
<<<<<<< HEAD
        MongoClient mongoClient = MongoClients.create("mongodb+srv://yig017:Gym201919@cluster0.cfigpzh.mongodb.net/?retryWrites=true&w=majority");
=======

        //String uri = "mongodb+srv://yax016:Xyg19970609@cluster0.tqvgogm.mongodb.net/?retryWrites=true&w=majority";
        MongoClient mongoClient = MongoClients.create("mongodb+srv://yig017:Gym201919@cluster0.cfigpzh.mongodb.net/?retryWrites=true&w=majority");
        //MongoClient mongoClient = MongoClients.create(uri);
>>>>>>> da968706891e56ce716c713abf09d547eec8e4ab
        MongoDatabase db = mongoClient.getDatabase("cse110_project");
        this.recipeCollection = db.getCollection("recipes");
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
            if (title.equals("Team35110")) {
                String genResponse = genRecipe();
                return genResponse;
            } else {
                Document target = recipeCollection.find(eq("title", title)).first();
                return target.getString("description");
            }
        } else {
            StringBuilder response = new StringBuilder();
            List<Document> recipes = recipeCollection.find().into(new ArrayList<>());
            for (Document i : recipes) {
                response.append("*" + i.getString("title"));
            }
            response.delete(0, 1);

            return response.toString();

        }
    }

    private String handlePost(HttpExchange httpExchange) throws IOException {
        InputStream inStream = httpExchange.getRequestBody();
        Scanner scanner = new Scanner(inStream);
        String postData = scanner.toString();
        String title = postData.substring(
                0,
                postData.indexOf(",")), details = postData.substring(postData.indexOf(",") + 1);
        Bson filter = eq("title", title);
        Bson updateOperation = com.mongodb.client.model.Updates.set("description", details);
        UpdateOptions options = new UpdateOptions().upsert(true);
        recipeCollection.updateOne(filter, updateOperation, options);
        scanner.close();

        return "Did Something?";
    }

    private String handlePut(HttpExchange httpExchange) throws IOException {
        InputStream inStream = httpExchange.getRequestBody();
        BufferedReader in = new BufferedReader(new InputStreamReader(inStream));
        String postData = in.lines().collect(Collectors.joining("\n"));
        String title = postData.substring(
                0,
                postData.indexOf(",")), details = postData.substring(postData.indexOf(",") + 1);
        Bson filter = eq("title", title);
        Bson updateOperation = com.mongodb.client.model.Updates.set("description", details);
        UpdateOptions options = new UpdateOptions().upsert(true);
        recipeCollection.updateOne(filter, updateOperation, options);
        in.close();

        return "Did Something?";
    }

    private String handleDelete(HttpExchange httpExchange) throws IOException {
        URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery();
        String response = "Could not delete";
        if (query != null) {
            String title = URLDecoder.decode(query.substring(query.indexOf("=") + 1), "UTF-8");
            Bson filter = eq("title", title);
            recipeCollection.deleteOne(filter);
            response = "Deleted Entry";

        }
        return response;
    }

    private String genRecipe() {
        try {
            return generation.generate();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (URISyntaxException e1) {
            e1.printStackTrace();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return null;
    }
}