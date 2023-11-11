package cse.project.team;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.text;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Model {
    private HashMap<String, String> data;
    private List<String> recipeList;
    private audioRec audio;
    private genAPI generation;
    private MongoCollection<Document> recipeCollection;

    public Model() {
        data = new HashMap<String, String>();
        recipeList = new ArrayList<String>();
        audio = new audioRec();
        generation = new genAPI();
        

        String uri = "mongodb://Jack:uJ1K500()urmom@ac-plzsahh-shard-00-00.zjt0s3q.mongodb.net:27017,ac-plzsahh-shard-00-01.zjt0s3q.mongodb.net:27017,ac-plzsahh-shard-00-02.zjt0s3q.mongodb.net:27017/?ssl=true&replicaSet=atlas-byp6sl-shard-0&authSource=admin&retryWrites=true&w=majority";

        MongoClient mongoClient = MongoClients.create(uri);
        MongoDatabase db = mongoClient.getDatabase("cse110_project");
        this.recipeCollection = db.getCollection("recipes");
    }

    public void startRec(){
        audio.startRecording();
    }

    public void stopRec(){
        audio.stopRecording();
    }

    public String genRecipe(){
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

    public List<String> getRecipeList() {
        return recipeList;
    }

    public HashMap<String, String> getData() {
        return data;
    }

    public void addData(String title, String text) {
        data.put(title, text);
        recipeList.add(title);
    }

    public void putData(String title, String text) {
        Bson filter = eq(title);
        Bson updateOperation = com.mongodb.client.model.Updates.push(title, text);
        UpdateOptions options = new UpdateOptions().upsert(true);
        UpdateResult updateResult = recipeCollection.updateOne(filter, updateOperation, options);
        data.put(title, text);
    }

    public void deleteData(String title) {
        data.remove(title);
        recipeList.remove(title);
    }

    public String getDetails(String title) {
        return data.get(title);
    }

    public MongoCollection<Document> getMongoCollection() {
        return this.recipeCollection;
    }
}
