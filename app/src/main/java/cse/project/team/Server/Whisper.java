package cse.project.team.Server;

import java.io.*;
import java.net.*;
import org.json.JSONArray;
import java.net.http.*;
import org.json.JSONObject;
import org.json.JSONException;

public class Whisper implements WhisperI {
    private static final String API_ENDPOINT = "https://api.openai.com/v1/audio/transcriptions";
    private static final String TOKEN = "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";
    private static final String MODEL = "whisper-1";
    
    public String audioGen(InputStream in) throws IOException, URISyntaxException,Exception {
        // Set up HTTP connection
        URL url = new URI(API_ENDPOINT).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        // Set up request headers
        String boundary = "Boundary-" + System.currentTimeMillis();
        connection.setRequestProperty(
                "Content-Type",
                "multipart/form-data; boundary=" + boundary);
        connection.setRequestProperty("Authorization", "Bearer " + TOKEN);
        // Set up output stream to write request body
        OutputStream outputStream = connection.getOutputStream();
        // Write model parameter to request body
        writeParameterToOutputStream(outputStream, "model", MODEL, boundary);
        // Write file parameter to request body
        writeFileToOutputStream(outputStream, in, boundary);
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
        return audio_generatedText;
    }

    private static void writeParameterToOutputStream(OutputStream outputStream, String parameterName,
            String parameterValue, String boundary) throws IOException {
        outputStream.write(("--" + boundary + "\r\n").getBytes());
        outputStream.write(("Content-Disposition: form-data; name=\"" + parameterName + "\"\r\n\r\n").getBytes());
        outputStream.write((parameterValue + "\r\n").getBytes());
    }

    private static void writeFileToOutputStream(
            OutputStream outputStream,
            InputStream audio,
            String boundary) throws IOException {
        outputStream.write(("--" + boundary + "\r\n").getBytes());
        outputStream.write(
                ("Content-Disposition: form-data; name=\"file\"; filename=\"" +
                        "audio.mp3" +
                        "\"\r\n").getBytes());
        outputStream.write(("Content-Type: audio/mpeg\r\n\r\n").getBytes());

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = audio.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        audio.close();
    }

    private static String handleSuccessResponse(HttpURLConnection connection)
            throws IOException, JSONException {
        BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        JSONObject responseJson = new JSONObject(response.toString());
        String generatedText = responseJson.getString("text");
        // Print the transcription result
        // System.out.println("Transcription Result: " + generatedText);
        return generatedText;
    }

    private static void handleErrorResponse(HttpURLConnection connection)
            throws IOException, JSONException {
        BufferedReader errorReader = new BufferedReader(
                new InputStreamReader(connection.getErrorStream()));
        String errorLine;
        StringBuilder errorResponse = new StringBuilder();
        while ((errorLine = errorReader.readLine()) != null) {
            errorResponse.append(errorLine);
        }
        errorReader.close();
        String errorResult = errorResponse.toString();
        System.out.println("Error Result: " + errorResult);
    }
}
