package cse.project.team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Model {
    private HashMap<String, String> data;
    private List<String> recipeList;

    public Model() {
        data = new HashMap<String, String>();
        recipeList = new ArrayList<String>();
    }

    public List<String> getRecipeList() {
        return recipeList;
    }

    public void addToList(String title){
        recipeList.add(title);
    }

    public HashMap<String, String> getData() {
        return data;
    }

    public void addData(String title, String text) {
        data.put(title, text);
    }

    public void deleteData(String title) {
        data.remove(title);
    }

    public String getDetails(String title) {
        return data.get(title);
    }

    public void deleteFromList(String currTitle) {
        recipeList.remove(currTitle);
    }
}
