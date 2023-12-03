package cse.project.team.server;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.stream.Collectors;
import org.bson.Document;
import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.util.*;


public class shareHandler implements HttpHandler {


 private final Map<String, String> data;


 public shareHandler(Map<String, String> data) {
   this.data = data;
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
        } else if (method.equals("PUT")) {
            response = handlePut(httpExchange);
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
    String response = "Invalid GET request";
    URI uri = httpExchange.getRequestURI();
    String query = uri.getRawQuery();
    response = query;
    if (query != null) {
      String value = query.substring(query.indexOf("=") + 1);
      String year = data.get(value); // Retrieve data from hashmap
      
      if (year != null) {
        response = year;
        //System.out.println("Queried for " + value + " and found " + year);
      } else {
        response = "No data found for " + value;
      }
    }
    return response;
  }

  
    private String handlePost(HttpExchange httpExchange) throws IOException {
        InputStream inStream = httpExchange.getRequestBody();
        Scanner scanner = new Scanner(inStream);
        String postData = "";
        while (scanner.hasNextLine()) {
            postData += scanner.nextLine() + '\n';
        }
        scanner.close();
        String response = postData;
        int indexOfNewLine = response.indexOf('\n');
        // Check if newline character exists
        String title = "recipe";
        if (indexOfNewLine != -1) {
            // Get the substring before the first newline character
            title = response.substring(0, indexOfNewLine);
            title = title.replaceAll("\\s", "");
        }
        String htmlRecipe = generateHtmlRecipe(response);
        System.out.println("key:"+title);
        data.put(title, htmlRecipe);
        return htmlRecipe;
        }

    private static String generateHtmlRecipe(String recipeText) {
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

  private String handlePut(HttpExchange httpExchange) throws IOException {
    InputStream inStream = httpExchange.getRequestBody();
    Scanner scanner = new Scanner(inStream);
    String putData = scanner.nextLine();
    System.out.println(putData);
    String response;
    response = "test: " + putData;
    System.out.println(response);
    scanner.close();

    return response;
}

private String handleDelete(HttpExchange httpExchange) throws IOException {
    URI uri = httpExchange.getRequestURI();
    String query = uri.getRawQuery();
    String response;

    if (query != null) {
        String language = query.substring(query.indexOf("=") + 1);
        String year = data.remove(language);

        if (year != null) {
            response = "Deleted entry {" + language + ", " + year + "}";
        } else {
            response = "No data found for " + language;
        }
    } else {
        response = "Invalid DELETE request";
    }

    return response;
}
}
