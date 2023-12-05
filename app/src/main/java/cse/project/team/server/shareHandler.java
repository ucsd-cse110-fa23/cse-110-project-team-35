package cse.project.team.server;

import java.io.*;
import java.net.*;
import java.util.*;
import com.sun.net.httpserver.*;

import cse.project.team.Model.Components.DalleI;

public class shareHandler implements HttpHandler {

    private final Map<String, String> data;
    DalleI dalle;

    public shareHandler(DalleI dalle) {
        this.data = new HashMap<>();
        this.dalle = dalle;
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
            } else if (method.equals("DELETE")) {
                response = handleDelete(httpExchange);
            } else {
                throw new Exception("Not Valid Request Method");
            }
        } catch (Exception e) {
            System.out.println("An erroneous request");
            response = e.toString();
            e.printStackTrace();
        }
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream outStream = httpExchange.getResponseBody();
        outStream.write(response.getBytes());
        outStream.close();
    }

    private String handleGet(HttpExchange httpExchange) throws IOException {
        URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery();
        return doGet(query);
    }

    public String doGet(String query){
        String response = query;
        if (query != null) {
            String value = query.substring(query.indexOf("=") + 1);
            value = value.replaceAll("\\s", "");
            String year = data.get(value); // Retrieve data from hashmap

            if (year != null) {
                response = year;
                // System.out.println("Queried for " + value + " and found " + year);
            } else {
                response = "Not found";
            }
        }
        System.out.println(response);
        return response;
    }

    private String handlePost(HttpExchange httpExchange) throws IOException {
        Headers headers = httpExchange.getResponseHeaders();
        headers.set("Content-Type", "text/html");

        InputStream inStream = httpExchange.getRequestBody();
        Scanner scanner = new Scanner(inStream);
        String postData = "";
        while (scanner.hasNextLine()) {
            postData += scanner.nextLine() + '\n';
        }
        scanner.close();

        return doPost(postData);
        
    }

    public String doPost(String postData){
        String response = postData;
        int indexOfNewLine = response.indexOf('\n');
        // Check if newline character exists
        String title = "recipe";
        if (indexOfNewLine != -1) {
            // Get the substring before the first newline character
            title = response.substring(0, indexOfNewLine);
            title = title.replaceAll("\\s", "");
        }

        String imagePath = dalle.generateDalle(title);
        String htmlRecipe = generateHtmlRecipe(response, imagePath);
        System.out.println(title + ":   " +htmlRecipe);

        data.put(title, htmlRecipe);

        return title;

    }

    private static String generateHtmlRecipe(String recipeText, String imagePath) {
        StringBuilder htmlBuilder = new StringBuilder();

        htmlBuilder.append("<html>\n<head>\n<title>Recipe</title>\n<style>\n");
        htmlBuilder.append("body { font-family: Arial, sans-serif; margin: 20px; }\n");
        htmlBuilder.append("h1 { text-align: center; color: #333; }\n");
        htmlBuilder.append("h2 { color: #333; }\n");
        htmlBuilder.append("p { color: #555; }\n");
        htmlBuilder.append("ul { list-style-type: none; padding: 0; }\n");
        htmlBuilder.append("li { margin-bottom: 10px; }\n");
        htmlBuilder.append("ol { margin-top: 0; }\n");
        htmlBuilder.append("</style>\n</head>\n<body>\n");

        htmlBuilder.append("<img src=\"").append(imagePath).append("\" alt=\"Recipe Image\">\n");

        String[] sections = recipeText.split("\n\n");

        for (String section : sections) {
            String[] lines = section.split("\n");
            if (lines.length > 0) {
                String sectionTitle = lines[0];
                htmlBuilder.append("<section>\n<h2>").append(sectionTitle).append("</h2>\n");
                if (lines.length > 1) {
                    if (sectionTitle.equals("Ingredients:")) {
                        htmlBuilder.append("<ul>\n");
                    } else if (sectionTitle.equals("Instructions:")) {
                        htmlBuilder.append("<ol>\n");
                    }

                    for (int i = 1; i < lines.length; i++) {
                        if (lines[i].startsWith("-")) {
                            htmlBuilder.append("<li>").append(lines[i].substring(1)).append("</li>\n");
                        } else {
                            htmlBuilder.append("<p>").append(lines[i]).append("</p>\n");
                        }
                    }

                    if (sectionTitle.equals("Ingredients:")) {
                        htmlBuilder.append("</ul>\n");
                    } else if (sectionTitle.equals("Instructions:")) {
                        htmlBuilder.append("</ol>\n");
                    }
                }

                htmlBuilder.append("</section>\n");
            }
        }

        htmlBuilder.append("</body>\n</html>");

        return htmlBuilder.toString();
    }

    private String handleDelete(HttpExchange httpExchange) throws IOException {
        URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery();
        return doDelete(query);
    }

    public String doDelete(String query) throws UnsupportedEncodingException{
        String title = URLDecoder.decode(query.substring(query.indexOf("=") + 1), "UTF-8");
        title = title.replaceAll("\\s", "");
        String response;

        if (query != null) {
            String details = data.remove(title);

            if (details != null) {
                response = "Deleted entry {" + title + ", " + details + "}";
            } else {
                response = "No data found for " + title;
            }
        } else {
            response = "Invalid DELETE request";
        }

        return response;
    }
}
