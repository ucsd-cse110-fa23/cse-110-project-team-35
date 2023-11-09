package cse.project.team;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Model {
    private HashMap<String, String> data;
    private List<String> recipeList;
    private audioRec audio;
    private test generation;

    public Model() {
        data = new HashMap<String, String>();
        recipeList = new ArrayList<String>();
        audio = new audioRec();
        generation = new test();
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
        data.put(title, text);
    }

    public void deleteData(String title) {
        data.remove(title);
        recipeList.remove(title);
    }

    public String getDetails(String title) {
        return data.get(title);
    }
}
