package cse.project.team;

import java.util.HashMap;

public class Model {
    private HashMap<String, String> data;

    public Model() {
        data = new HashMap<String, String>();
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
}
