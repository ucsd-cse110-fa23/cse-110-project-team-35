package cse.project.team.Server;

import java.io.*;
import java.net.*;
import org.json.JSONArray;
import java.net.http.*;
import org.json.JSONObject;
import org.json.JSONException;

public class GPT implements GPTI {
    private static final String Chat_API_ENDPOINT = "https://api.openai.com/v1/completions";
    private static final String Chat_API_KEY = "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";
    private static final String Chat_MODEL = "text-davinci-003";

    public String chatgen(String audio_generatedText) throws URISyntaxException, IOException, InterruptedException {
        // chat GPT starts here
        String prompt = "Write a short title on the first line followed by a single newline character, then write a recipe with ingredients "
                + audio_generatedText;
        String number_of_token = "400";
        int maxTokens = Integer.parseInt(number_of_token);

        // Create a request body which you will pass into request object
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", Chat_MODEL);
        requestBody.put("prompt", prompt);
        requestBody.put("max_tokens", maxTokens);
        requestBody.put("temperature", 1.0);

        // Create the HTTP Client
        HttpClient client = HttpClient.newHttpClient();
        // Create the request object
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(new URI(Chat_API_ENDPOINT))
                .header("Content-Type", "application/json")
                .header("Authorization", String.format("Bearer %s", Chat_API_KEY))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                .build();
        // Send the request and receive the response
        HttpResponse<String> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofString());
        // Process the response
        String responseBody = response.body();
        JSONObject responseJson = new JSONObject(responseBody);
        JSONArray choices = responseJson.getJSONArray("choices");
        String generatedText = choices.getJSONObject(0).getString("text");

        // System.out.println("ChatGPT response: \n"+generatedText.trim());
        return generatedText.trim();
    }
}
