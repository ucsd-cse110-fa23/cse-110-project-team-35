package cse.project.team.Server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;

import com.sun.net.httpserver.*;

public class GenHandler implements HttpHandler {
    private GPTI chat;
    private WhisperI audio;

    GenHandler(GPTI chatGen, WhisperI audioGen) {
        this.chat = chatGen;
        this.audio = audioGen;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "Request Received";
        String method = httpExchange.getRequestMethod();

        try {
            if (method.equals("GET")) {
                response = handleGet(httpExchange);
            } else if (method.equals("POST")) {
                response = handlePost(httpExchange);
            } else {
                throw new Exception("Not Valid Request Method");
            }
        } catch (Exception e) {
            response = e.toString();
            e.printStackTrace();
        }

        System.out.println("Check Response: " + response);

        // Sending back response to the client
        httpExchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream outStream = httpExchange.getResponseBody();
        outStream.write(response.getBytes());
        outStream.flush();
        outStream.close();
    }

    private String handlePost(HttpExchange httpExchange) throws IOException, URISyntaxException, Exception {
        InputStream input = httpExchange.getRequestBody();
        String audioTxt = audio.audioGen(input);
        audioTxt = audioTxt.toLowerCase();
        System.out.println("AudioTxt in GenHandler:" + audioTxt);

        return (audioTxt.contains("breakfast") ||
        audioTxt.contains("lunch") ||
        audioTxt.contains("dinner")) ? audioTxt : "Error";
    }

    private String handleGet(HttpExchange httpExchange) throws UnsupportedEncodingException {
        URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery();
        String audioTxt = URLDecoder.decode(query.substring(query.indexOf("=") + 1), "UTF-8");

        try {
            return chat.chatgen(audioTxt);
        } catch (URISyntaxException | IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return "Request Error";
    }
}
