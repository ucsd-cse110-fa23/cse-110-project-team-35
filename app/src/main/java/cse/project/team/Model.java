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

    public HashMap<String, String> getData() {
        return data;
    }

    public void addData(String title, String text) {
        data.put(title, text);
        recipeList.add(title);
    }

    public void deleteData(String title) {
        data.remove(title);
        recipeList.remove(title);
    }

    public String getDetails(String title) {
        return data.get(title);
    }
}
