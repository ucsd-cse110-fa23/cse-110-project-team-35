package cse.project.team.server;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.sun.net.httpserver.*;

import static com.mongodb.client.model.Filters.eq;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.stream.Collectors;

import org.bson.Document;
import org.bson.conversions.Bson;

public class DBHandler implements HttpHandler {
    private MongoCollection<Document> recipeCollection;

    public DBHandler(String dataBase) {
        MongoClient mongoClient = MongoClients
                .create("mongodb://yig017:Gym201919@ac-lzsqbrn-shard-00-00.cfigpzh.mongodb.net:27017,ac-lzsqbrn-shard-00-01.cfigpzh.mongodb.net:27017,ac-lzsqbrn-shard-00-02.cfigpzh.mongodb.net:27017/?ssl=true&replicaSet=atlas-9thc6y-shard-0&authSource=admin&retryWrites=true&w=majority");
        MongoDatabase db = mongoClient.getDatabase(dataBase);
        this.recipeCollection = db.getCollection("recipes");
    }

    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "Request Received";
        String method = httpExchange.getRequestMethod();

        try {
            if (method.equals("GET")) {
                response = String.join("xF9j", handleGet(httpExchange));
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

    private ArrayList<String> handleGet(HttpExchange httpExchange) throws IOException {
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
        String title = postData.substring(0,postData.indexOf(","));
        String details = postData.substring(postData.indexOf(",") + 1);
        String username = postData.substring(postData.indexOf(",") + 1); 
        String mealType = postData.substring(postData.indexOf(",") + 1);
        scanner.close();
        doPost(title, details, username, mealType);

        return "Did Something?";
    }

    private String handlePut(HttpExchange httpExchange) throws IOException {
        InputStream inStream = httpExchange.getRequestBody();
        BufferedReader in = new BufferedReader(new InputStreamReader(inStream));
        
        String postData = in.lines().collect(Collectors.joining("\n"));
        /* 
        String title = postData.substring(
                0,
                postData.indexOf(",")), details = postData.substring(postData.indexOf(",") + 1), 
                username  = postData.substring(postData.indexOf(",") + 1);*/

        String[] fieldsSplit = postData.split("#");

        /*
        String title = postData.substring(0, postData.indexOf("#"));

        int firstCommaIndex = postData.indexOf("#") + 1;
        int secondCommaIndex = postData.indexOf("#", firstCommaIndex);
        int thirdCommaIndex = postData.indexOf("#", secondCommaIndex);
                
        String details = postData.substring(firstCommaIndex, secondCommaIndex);
        String username = postData.substring(secondCommaIndex + 1, thirdCommaIndex);
        String mealType = postData.substring(thirdCommaIndex + 1);
        */

        String title = fieldsSplit[0];
        String details = fieldsSplit[1];
        String username = fieldsSplit[2];
        String mealType = fieldsSplit[3];

        System.out.println("0: " + fieldsSplit[0]);
        System.out.println("1: " + fieldsSplit[1]);
        System.out.println("2: " + fieldsSplit[2]);
        System.out.println("3: " + fieldsSplit[3]);

        doPost(title, details, username, mealType);
        in.close();

        return "Did Something";
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


    public ArrayList<String> getRecDetail(String title) {
        Document target = recipeCollection.find(eq("title", title)).first();
        ArrayList<String> recInfo = new ArrayList<String>();
        if (target == null) {
            recInfo.add("Does not exist");
        }
        else {
            // description will be recInfo[0]
            recInfo.add(target.getString("description"));
            // description will be recInfo[1]
            recInfo.add(target.getString("mealType"));
        }
        return recInfo;
    }

    public ArrayList<String> getRecList() {
        ArrayList<String> recList = new ArrayList<String>();
        StringBuilder response = new StringBuilder();
        List<Document> recipes = recipeCollection.find().into(new ArrayList<>());
        for (Document i : recipes) {
            response.append(i.getString("title") + "yL8z42" + i.getString("username"));
            recList.add(response.toString());
            response.setLength(0);
        }
        return recList;
    }

    public void doPost(String title, String details, String username, String mealType) {
        /* 
        Bson filter = eq("title", title);
        Bson updateDescription = com.mongodb.client.model.Updates.set("description", details);
       
        UpdateOptions options = new UpdateOptions().upsert(true);
        recipeCollection.updateOne(filter, updateDescription, options);
        Bson updateUsername = com.mongodb.client.model.Updates.set("username", username);
         recipeCollection.updateOne(filter, updateUsername, options);
         */
        /* 
        Bson filter = eq("title", title);

        System.out.println("Mealtype: ");

        Bson updateDescription = com.mongodb.client.model.Updates.set("description", details);
        Bson updateUsername = com.mongodb.client.model.Updates.set("username", username);
        Bson updateMealType = com.mongodb.client.model.Updates.set("mealType", mealType);

        Bson updateOperation = com.mongodb.client.model.Updates.combine(updateDescription, updateUsername, updateMealType);

        UpdateOptions options = new UpdateOptions().upsert(true);

        recipeCollection.updateOne(filter, updateOperation, options);

        */

        Bson filter = Filters.eq("title", title);

        // Define update operations
        Bson updateDescription = Updates.set("description", details);
        Bson updateUsername = Updates.set("username", username);
        Bson updateMealType = Updates.set("mealType", mealType);

        Bson updateOperation = Updates.combine(updateDescription, updateUsername, updateMealType);

        UpdateOptions options = new UpdateOptions().upsert(true);

        recipeCollection.updateOne(filter, updateOperation, options);


    }

    public void doDelete(String title) {
        Bson filter = eq("title", title);
        recipeCollection.deleteOne(filter);
    }

    public void clear() {
        recipeCollection.deleteMany(new Document());
    }
}