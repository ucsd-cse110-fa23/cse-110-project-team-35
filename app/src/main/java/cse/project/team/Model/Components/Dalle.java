package cse.project.team.Model.Components;

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

public class Dalle implements DalleI {
    private static final String DALLE_API_ENDPOINT = "https://api.openai.com/v1/images/generations";
    private static final String DALLE_API_KEY = "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";
    private static final String DALLE_MODEL = "dall-e-2";

    public String generateDalle(String title) {
        // DALLE starts
        int n = 1;
        // Create a request body which you will pass into request object
        JSONObject dalle_requestBody = new JSONObject();
        dalle_requestBody.put("model", DALLE_MODEL);
        dalle_requestBody.put("prompt", title);
        dalle_requestBody.put("n", n);
        dalle_requestBody.put("size", "256x256");

        // Create the HTTP client
        HttpClient dalle_client = HttpClient.newHttpClient();

        // Create the request object
        HttpRequest dalle_request = HttpRequest
                .newBuilder()
                .uri(URI.create(DALLE_API_ENDPOINT))
                .header("Content-Type", "application/json")
                .header("Authorization", String.format("Bearer %s", DALLE_API_KEY))
                .POST(HttpRequest.BodyPublishers.ofString(dalle_requestBody.toString()))
                .build();

        // Send the request and receive the response
        HttpResponse<String> dalle_response = null;
        try {
            dalle_response = dalle_client.send(
                    dalle_request,
                    HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        // Process the response
        String dalle_responseBody = dalle_response.body();
        JSONObject dalle_responseJson = new JSONObject(dalle_responseBody);
        String generatedImageURL = dalle_responseJson.getJSONArray("data").getJSONObject(0).getString("url");
        System.out.println("DALL-E Response:");
        System.out.println(generatedImageURL);

        return generatedImageURL;

    }

    public void downloadImage(String generatedImageURL, String title) {

        // Download the Generated Image to the Current Directory
        try (InputStream in = new URI(generatedImageURL).toURL().openStream()) {
            Files.copy(in, Paths.get(title + ".jpg"));
            System.out.println("Image downloaded successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean fileExists(String title) {
        String currentDirectory = System.getProperty("user.dir");
        String fileName = title + ".jpg";
        File imageFile = new File(currentDirectory, fileName);
        return imageFile.exists();
    }
}
