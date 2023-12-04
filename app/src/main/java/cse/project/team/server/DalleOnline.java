package cse.project.team.server;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONObject;

public class DalleOnline implements IDalle{
    private static final String DALLE_API_ENDPOINT = "https://api.openai.com/v1/images/generations";
    private static final String DALLE_API_KEY = "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";
    private static final String DALLE_MODEL = "dall-e-2";

    public String generateDalle(String input) {
        // DALLE starts
        int n = 1;
        String dalle_prompt = input;
            // Create a request body which you will pass into request object
            JSONObject dalle_requestBody = new JSONObject();
            dalle_requestBody.put("model", DALLE_MODEL);
            dalle_requestBody.put("prompt", dalle_prompt);
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
}
