/* 
package cse.project.team.server;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.sun.net.httpserver.*;

import static com.mongodb.client.model.Filters.elemMatch;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.type;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.stream.Collectors;

import org.bson.Document;
import org.bson.conversions.Bson;

public class RequestHandler implements HttpHandler {
    private MongoCollection<Document> recipeCollection;
    private MongoCollection<Document> accountCollection;
    private genI generation;

    public RequestHandler(genI generation) {
        this.generation = generation;
        MongoClient mongoClient = MongoClients
                .create("mongodb://yig017:Gym201919@ac-lzsqbrn-shard-00-00.cfigpzh.mongodb.net:27017,ac-lzsqbrn-shard-00-01.cfigpzh.mongodb.net:27017,ac-lzsqbrn-shard-00-02.cfigpzh.mongodb.net:27017/?ssl=true&replicaSet=atlas-9thc6y-shard-0&authSource=admin&retryWrites=true&w=majority");
        MongoDatabase db = mongoClient.getDatabase("cse110_project");
        this.recipeCollection = db.getCollection("recipes");
        this.accountCollection = db.getCollection("accounts");
    }

    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "Request Received";
        String method = httpExchange.getRequestMethod();
       URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery();

        try {
            if (method.equals("GET")) {
                response = handleGet(httpExchange);

                if(query.equals("password"))
                {
                    response = handleGetAccount(httpExchange);
                }
                else
                {
                    response = handleGet(httpExchange);
                }

            } else if (method.equals("POST")) {
                response = handlePost(httpExchange);
            } else if (method.equals("DELETE")) {
                response = handleDelete(httpExchange);
            } else if (method.equals("PUT")) {
                response = handlePut(httpExchange);
                
                if(query.equals("password"))
                {
                    response = handlePutAccount(httpExchange);
                }
                else
                {
                    response = handlePut(httpExchange);
                }
            }
else {
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
    private String handleGetAccount(HttpExchange httpExchange) throws IOException {
        URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery();

        if (query.equals("password")) {
            String title = URLDecoder.decode(query.substring(query.indexOf("=") + 1), "UTF-8");
            return getAccountPassword(title);
        } else {
            return getAccountList();
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
        doPost(title, details);

        return "Did Something?";
    }

    private String handlePut(HttpExchange httpExchange) throws IOException {
        InputStream inStream = httpExchange.getRequestBody();
        BufferedReader in = new BufferedReader(new InputStreamReader(inStream));
        String postData = in.lines().collect(Collectors.joining("\n"));
        String title = postData.substring(
                0,
                postData.indexOf(",")), details = postData.substring(postData.indexOf(",") + 1);
        doPost(title, details);
        in.close();

        return "Did Something";

    }

    private String handlePutAccount(HttpExchange httpExchange) throws IOException {
        InputStream inStream = httpExchange.getRequestBody();
        BufferedReader in = new BufferedReader(new InputStreamReader(inStream));
        String postData = in.lines().collect(Collectors.joining("\n"));
        String title = postData.substring(
                0,
                postData.indexOf(",")), details = postData.substring(postData.indexOf(",") + 1);
        accountPut(title, details);
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

    public String getRecDetail(String title) {
        if (title.equals("Team35110")) {
            String genResponse = genRecipe();
            return genResponse;
        } else {
            Document target = recipeCollection.find(eq("title", title)).first();
            return target.getString("description");
        }
    }

    public String getAccountPassword(String account) {
        Document target = accountCollection.find(eq("account", account)).first();
        return target.getString("password"); 
    }

    public String getRecList() {
        StringBuilder response = new StringBuilder();
        List<Document> recipes = recipeCollection.find().into(new ArrayList<>());
        for (Document i : recipes) {
            response.append("*" + i.getString("title"));
        }
        response.delete(0, 1);
        return response.toString();
    }

    public String getAccountList() {
        StringBuilder response = new StringBuilder();
        List<Document> account = accountCollection.find().into(new ArrayList<>());
        for (Document i : account) {
            response.append("*" + i.getString("account"));
        }
        response.delete(0, 1);
        return response.toString();
    }


    public void doPost(String title, String details) {
        Bson filter = eq("title", title);
        Bson updateOperation = com.mongodb.client.model.Updates.set("description", details);
        UpdateOptions options = new UpdateOptions().upsert(true);
        recipeCollection.updateOne(filter, updateOperation, options);
    }

    public void accountPut(String account, String password) {
        Document document = new Document("username", account).append("password", password);
        accountCollection.insertOne(document);
    }



    public void doDelete(String title) {
        Bson filter = eq("title", title);
        recipeCollection.deleteOne(filter);
    }

    public void clear() {
        recipeCollection.deleteMany(new Document());
    }
}
*/

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
    private MongoCollection<Document> recipeCollection;
    private MongoCollection<Document> accountCollection;
    private genI generation;

    public RequestHandler(genI generation) {
        this.generation = generation;
        MongoClient mongoClient = MongoClients
                .create("mongodb://yig017:Gym201919@ac-lzsqbrn-shard-00-00.cfigpzh.mongodb.net:27017,ac-lzsqbrn-shard-00-01.cfigpzh.mongodb.net:27017,ac-lzsqbrn-shard-00-02.cfigpzh.mongodb.net:27017/?ssl=true&replicaSet=atlas-9thc6y-shard-0&authSource=admin&retryWrites=true&w=majority");
        MongoDatabase db = mongoClient.getDatabase("cse110_project");
        this.recipeCollection = db.getCollection("recipes");
        this.accountCollection = db.getCollection("accounts");
    }

    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "Request Received";
        String method = httpExchange.getRequestMethod();
        try {
            if (method.equals("GET")) {
                URI uri = httpExchange.getRequestURI();
                String query = uri.getRawQuery();

                if(query != null && query.equals("password"))
                {
                    response = handleGetAccount(httpExchange);
                    return;
                }
                else{
                    response = handleGet(httpExchange);

                }
            } else if (method.equals("POST")) {

                response = handlePost(httpExchange);
            } else if (method.equals("DELETE")) {
                response = handleDelete(httpExchange);
            } else if (method.equals("PUT")) {
                 URI uri = httpExchange.getRequestURI();
                 String query = uri.getRawQuery();
               if(query != null && query.equals("password"))
                {
                    response = handlePutAccount(httpExchange);
                    return;
                }
                else{
                
                    response = handlePut(httpExchange);
                }
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
        doPost(title, details);

        return "Did Something?";
    }

    private String handlePut(HttpExchange httpExchange) throws IOException {
        InputStream inStream = httpExchange.getRequestBody();
        BufferedReader in = new BufferedReader(new InputStreamReader(inStream));
        String postData = in.lines().collect(Collectors.joining("\n"));
        String title = postData.substring(
                0,
                postData.indexOf(",")), details = postData.substring(postData.indexOf(",") + 1);
        doPost(title, details);
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

    public String getRecDetail(String title) {
        if (title.equals("Team35110")) {
            String genResponse = genRecipe();
            return genResponse;
        } else {
            Document target = recipeCollection.find(eq("title", title)).first();
            return target.getString("description");
        }
    }

    public String getRecList() {
        StringBuilder response = new StringBuilder();
        List<Document> recipes = recipeCollection.find().into(new ArrayList<>());
        for (Document i : recipes) {
            response.append("*" + i.getString("title"));
        }
        response.delete(0, 1);
        return response.toString();
    }

    public void doPost(String title, String details) {
        Bson filter = eq("title", title);
        Bson updateOperation = com.mongodb.client.model.Updates.set("description", details);
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

    public String getAccountPassword(String account) {
        Document target = accountCollection.find(eq("username", account)).first();
        return target.getString("password"); 
    }

     private String handleGetAccount(HttpExchange httpExchange) throws IOException {
        URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery();

        if (query.equals("password")) {
            String title = URLDecoder.decode(query.substring(query.indexOf("=") + 1), "UTF-8");
            return getAccountPassword(title);
        } else {
            return getAccountList();
        }
    }
    
    public String getAccountList() {
        StringBuilder response = new StringBuilder();
        List<Document> account = accountCollection.find().into(new ArrayList<>());
        for (Document i : account) {
            response.append("*" + i.getString("username"));
        }
        response.delete(0, 1);
        return response.toString();
    }
    private String handlePutAccount(HttpExchange httpExchange) throws IOException {
        InputStream inStream = httpExchange.getRequestBody();
        BufferedReader in = new BufferedReader(new InputStreamReader(inStream));
        String postData = in.lines().collect(Collectors.joining("\n"));
        String title = postData.substring(
                0,
                postData.indexOf(",")), details = postData.substring(postData.indexOf(",") + 1);
        accountPut(title, details);
        in.close();

        return "Did Something";

    }

    public void accountPut(String account, String password) {
        Document document = new Document("username", account).append("password", password);
        accountCollection.insertOne(document);
    }

}
