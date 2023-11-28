package cse.project.team;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.stream.Collectors;

public class Model {
    private audioRec audio;

    public Model() {
        audio = new audioRec();
    }

    public void startRec() {
        audio.startRecording();
    }

    public void stopRec() {
        audio.stopRecording();
    }

    public String dBRequest(String method, String title, String details, String query) {
        try {
            String urlString = "http://localhost:8100/db/";
            if (query != null) {
                urlString += "?=" + URLEncoder.encode(query, "UTF-8");
            }

            URL url = new URI(urlString).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.setDoOutput(true);

            if (method.equals("POST") || method.equals("PUT")) {
                OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
                out.write(title + "," + details);
                out.flush();
                out.close();
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response = in.lines().collect(Collectors.joining("\n"));
            in.close();
            return response;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "Error: " + ex.getMessage();
        }
    }

    public String genRequest(String method, String query) {
        try {
            String urlString = "http://localhost:8100/gen/";
            if (query != null) {
                urlString += "?=" + URLEncoder.encode(query, "UTF-8");
            }

            URL url = new URI(urlString).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.setDoOutput(true);
            if (method.equals("POST")) {
                writeFileToOutputStream(conn);
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response = in.lines().collect(Collectors.joining("\n"));
            in.close();
            return response;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "Error: " + ex.getMessage();
        }
    }

    private static void writeFileToOutputStream(HttpURLConnection conn) throws IOException {
        OutputStream out = conn.getOutputStream();
        File file = new File("../recording.mp3");

        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
        }
        fileInputStream.close();
        out.close();
    }
}
