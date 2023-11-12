package cse.project.team.server;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;
import com.sun.net.httpserver.*;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.text;

import java.io.*;
import java.net.*;
import java.util.*;

import org.bson.Document;
import org.bson.conversions.Bson;

public class RequestHandler implements HttpHandler {
    private genAPI generation;
    private MongoCollection<Document> recipeCollection;

    public RequestHandler() {
        generation = new genAPI();

        String uri = "mongodb+srv://bbreeze:Breeze1011@cluster0.6mbm76b.mongodb.net/?retryWrites=true&w=majority";

        MongoClient mongoClient = MongoClients.create(uri);
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
            } else if (method.equals("GEN")) {
                response = handleGen(httpExchange);
            }else {
                throw new Exception("Not Valid Request Method");
            }
        } catch (Exception e) {
            System.out.println("An erroneous request, method: " + method);
            response = e.toString();
            e.printStackTrace();
        }

        // Sending back response to the client
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream outStream = httpExchange.getResponseBody();
        outStream.write(response.getBytes());
        outStream.close();
    }

    private String handleGen(HttpExchange httpExchange) {
        String genResponse = genRecipe();
        String title = genResponse.trim().split("\n")[0];
        String details = genResponse.trim().substring(title.length()).trim();
        return title + "*" + details;
    }

    private String handleGet(HttpExchange httpExchange) throws IOException{
        URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery();
        
        if (query != null) {
            String title = URLDecoder.decode(query.substring(query.indexOf("=") + 1),"UTF-8");
            Document target = recipeCollection.find(eq("title", title)).first();

            System.out.println(target.getString("description"));
    
            return target.getString("description");
        } else {
            StringBuilder response = new StringBuilder();
            List<Document> recipes = recipeCollection.find().into(new ArrayList<>());
            for (Document i : recipes) {
                response.append("*" + i.getString("title"));
            }
            return response.toString();

        }
    }

    private String handlePost(HttpExchange httpExchange) throws IOException {
        InputStream inStream = httpExchange.getRequestBody();
        Scanner scanner = new Scanner(inStream);
        String postData = scanner.nextLine();
        String title = postData.substring(
                0,
                postData.indexOf(",")), details = postData.substring(postData.indexOf(",") + 1);
        Bson filter = eq("title", title);
        Bson updateOperation = com.mongodb.client.model.Updates.set("description",details);
        UpdateOptions options = new UpdateOptions().upsert(true);
        UpdateResult updateResult = recipeCollection.updateOne(filter, updateOperation, options);
        scanner.close();

        return "Did Something?";
    }

    private String handlePut(HttpExchange httpExchange) throws IOException {
        InputStream inStream = httpExchange.getRequestBody();
        Scanner scanner = new Scanner(inStream);
        String postData = scanner.nextLine();
        String title = postData.substring(
                0,
                postData.indexOf(",")), details = postData.substring(postData.indexOf(",") + 1);

        Bson filter = eq("title", title);
        Bson updateOperation = com.mongodb.client.model.Updates.set("description",details);
        UpdateOptions options = new UpdateOptions().upsert(true);
        UpdateResult updateResult = recipeCollection.updateOne(filter, updateOperation, options);

        scanner.close();

        return "Did Something?";
    }

    private String handleDelete(HttpExchange httpExchange) throws IOException {
        URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery();
        String response = "Could not delete";
        if (query != null) {
            String title = URLDecoder.decode(query.substring(query.indexOf("=") + 1),"UTF-8");
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