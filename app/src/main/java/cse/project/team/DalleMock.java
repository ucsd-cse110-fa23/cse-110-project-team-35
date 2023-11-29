package cse.project.team;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.json.JSONObject;

public class DalleMock implements DalleI {
    public void generateDalle(String input) {
        String fileName = input + ".jpg";

        String currentDirectory = System.getProperty("user.dir");
        File imageFile = new File(currentDirectory, fileName);

        if (!imageFile.exists()) {
            String generatedImageURL = "https://upload.wikimedia.org/wikipedia/en/thumb/9/9a/Trollface_non-free.png/220px-Trollface_non-free.png";
            try (InputStream in = new URI(generatedImageURL).toURL().openStream()) {
                Files.copy(in, Paths.get(fileName));
                System.out.println("Image downloaded successfully.");
            } 
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
