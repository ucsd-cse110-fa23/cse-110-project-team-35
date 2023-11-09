package cse.project.team;
import javax.sound.sampled.*;
import java.io.*;
import java.net.*;
import org.json.JSONArray;
import java.net.http.*;
import org.json.JSONObject;
import org.json.JSONException;
public class test {
    private static final String API_ENDPOINT = "https://api.openai.com/v1/audio/transcriptions";
    private static final String TOKEN = "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";
    private static final String MODEL = "whisper-1";
    private static final String Chat_API_ENDPOINT = "https://api.openai.com/v1/completions";
    private static final String Chat_API_KEY = "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";
    private static final String Chat_MODEL = "text-davinci-003";
    //private static final String FILE_PATH = "/Users/gaoyiming/Desktop/fall23/lab4/Lab4/test.mp3";

    private static void writeParameterToOutputStream(OutputStream outputStream,String parameterName,String parameterValue,String boundary) throws IOException {
        outputStream.write(("--" + boundary + "\r\n").getBytes());
        outputStream.write(("Content-Disposition: form-data; name=\"" + parameterName + "\"\r\n\r\n").getBytes());
        outputStream.write((parameterValue + "\r\n").getBytes());
    }

    private static void writeFileToOutputStream(
        OutputStream outputStream,
        File file,
        String boundary
        ) throws IOException {
        outputStream.write(("--" + boundary + "\r\n").getBytes());
        outputStream.write(
          (
           "Content-Disposition: form-data; name=\"file\"; filename=\"" +
           file.getName() +
           "\"\r\n"
          ).getBytes()
              );
        outputStream.write(("Content-Type: audio/mpeg\r\n\r\n").getBytes());
        
        
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
             outputStream.write(buffer, 0, bytesRead);
        }
        fileInputStream.close();
        }
    private static String handleSuccessResponse(HttpURLConnection connection)
        throws IOException, JSONException {
        BufferedReader in = new BufferedReader(
            new InputStreamReader(connection.getInputStream())
        );
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        
        JSONObject responseJson = new JSONObject(response.toString());
        String generatedText = responseJson.getString("text");
        // Print the transcription result
        System.out.println("Transcription Result: " + generatedText);
        return generatedText;
    }
    private static void handleErrorResponse(HttpURLConnection connection)
        throws IOException, JSONException {
        BufferedReader errorReader = new BufferedReader(
            new InputStreamReader(connection.getErrorStream())
        );
        String errorLine;
        StringBuilder errorResponse = new StringBuilder();
        while ((errorLine = errorReader.readLine()) != null) {
            errorResponse.append(errorLine);
        }
        errorReader.close();
        String errorResult = errorResponse.toString();
        System.out.println("Error Result: " + errorResult);
    }

    public String generate() throws IOException, URISyntaxException,Exception {
        // Create file object from file path
        File file = new File("../recording.mp3");
        // Set up HTTP connection
        URL url = new URI(API_ENDPOINT).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        // Set up request headers
        String boundary = "Boundary-" + System.currentTimeMillis();
        connection.setRequestProperty(
            "Content-Type",
            "multipart/form-data; boundary=" + boundary
        );
        connection.setRequestProperty("Authorization", "Bearer " + TOKEN);
        // Set up output stream to write request body
        OutputStream outputStream = connection.getOutputStream();
        // Write model parameter to request body
        writeParameterToOutputStream(outputStream, "model", MODEL, boundary);
        // Write file parameter to request body
        writeFileToOutputStream(outputStream, file, boundary);
        // Write closing boundary to request body
        outputStream.write(("\r\n--" + boundary + "--\r\n").getBytes());
        // Flush and close output stream
        outputStream.flush();
        outputStream.close();
        // Get response code
        int responseCode = connection.getResponseCode();
        // Check response code and handle response accordingly
        String audio_generatedText = "";
        if (responseCode == HttpURLConnection.HTTP_OK) {
            audio_generatedText = handleSuccessResponse(connection);
        } else {
            handleErrorResponse(connection);
        }
        // Disconnect connection
        connection.disconnect();


        // chat GPT starts here
        String prompt = "Write an exiting title on the first line ended with a : then write a recipe with ingredients " + audio_generatedText;
        String number_of_token =  "100";
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
        HttpResponse.BodyHandlers.ofString()
        );
        // Process the response
        String responseBody = response.body();
        JSONObject responseJson = new JSONObject(responseBody);
        JSONArray choices = responseJson.getJSONArray("choices");
        String generatedText = choices.getJSONObject(0).getString("text"); 
        
        System.out.println("ChatGPT response: \n"+generatedText);
        return generatedText;
    }
}

