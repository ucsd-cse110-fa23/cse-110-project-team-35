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
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import org.bson.types.ObjectId;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Model {
    private audioRec audio;
    private genAPI generation;
    private MongoCollection<Document> recipeCollection;

    public Model() {
        audio = new audioRec();
        generation = new genAPI();
        

        String uri = "mongodb+srv://yax016:@cluster0.tqvgogm.mongodb.net/?retryWrites=true&w=majority";

        MongoClient mongoClient = MongoClients.create(uri);
        MongoDatabase db = mongoClient.getDatabase("cse110_project");
        this.recipeCollection = db.getCollection("recipes");

    }

    public void startRec() {
        audio.startRecording();
    }

    public void stopRec() {
        audio.stopRecording();
    }

    public String genRecipe() {
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

    public List<Document> getRecipeList() {
        List<Document> recipes = recipeCollection.find().into(new ArrayList<>());
        return recipes;
    }

    public void putData(String title, String text) {
        Bson filter = eq("title", title);
        Bson updateOperation = com.mongodb.client.model.Updates.set("description", text);
        UpdateOptions options = new UpdateOptions().upsert(true);
        UpdateResult updateResult = recipeCollection.updateOne(filter, updateOperation, options);
    }

    public void deleteData(String title) {
        Bson filter = eq("title", title);
        recipeCollection.deleteOne(filter);
    }

    public String getDetails(String title) {
        String result = "";
        Bson filter = eq("title", title);
        List<Document> target = recipeCollection.find(filter).into(new ArrayList<>());
        for (Document i : target) {
            result = i.getString("description");
        }
        return result;
    }
}
