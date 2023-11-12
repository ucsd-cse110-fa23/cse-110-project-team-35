package cse.project.team;
import cse.project.team.server.server;
import org.junit.jupiter.api.Test;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import com.sun.net.httpserver.HttpServer;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

class AppTest {
    String m_title = "Mashed potat";
    String m_details = "Get potatoes. Mash. Done.";
    String p_title = "Pancakes";
    String p_details = "Get cake. Get pan. Put cake in pan. Done.";
    
    @BeforeClass
    public static void setUp() {
        // Start the server in a separate thread
        Thread serverThread = new Thread(() -> {
            try {
                cse.project.team.server.server.main(null); // You may need to adjust this based on your server class
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        serverThread.start();
        // Add a delay to ensure the server is started before tests are executed
        try {
            Thread.sleep(2000); // Adjust the delay as needed
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
/* 
    @AfterClass
    public static void tearDown() {
        // Stop the server
        serverThread.interrupt();
    }
*/
    private static final String SERVER_URL = "http://localhost:8100/";
    // the server should be already running
    @Test
    public void testCheck() throws Exception{
        assertEquals(1+1, 2);
    }
    @Test
    public void testHandlePost() throws Exception{
        String expectedResponse_title = "Mashed potat";
        String expectedResponse_detail = m_details;
        Model model = new Model();
        String response_post = model.performRequest("POST",m_title,m_details,"m");
        String response = model.performRequest("GET",m_title,m_details,null);
        String response_detail = model.performRequest("GET", null, null, response);
        assertEquals(expectedResponse_title, response);
        assertEquals(expectedResponse_detail, response_detail);
    }

    @Test
    public void testHandleGet() throws IOException {

        String expectedResponse = "";
        Model model = new Model();
        // Perform the GET request
        //String responseDelete = model.performRequest("DELETE", null, null, "Mashed potat");
        String response = model.performRequest("GET",m_title,m_details,null);

        assertEquals(expectedResponse, response);
    }

     @Test
    public void testHandlePUTandGET() throws IOException {

        String expectedResponse_title = "Mashed potat";
        String expectedResponse_detail = m_details;
        String putdone = "Did Something?";
        Model model = new Model();
        String response_put = model.performRequest("PUT",m_title,m_details,null);
        String response = model.performRequest("GET",m_title,m_details,null);

         assertEquals(response_put, putdone);
         assertEquals(expectedResponse_title, response);

    }

    @Test
    public void testHandlePUT() throws IOException {
        String expectedResponse_title = "Mashed potat";
        String putdone = "Did Something?";
        String expectedResponse_detail = m_details;
        Model model = new Model();
        String response_put = model.performRequest("PUT",m_title,m_details,null);
         assertEquals(putdone, response_put);

    }

    @Test
    public void testHandlDELETE() throws Exception{
        String expectedResponse_title = "Mashed potat";
        String expectedResponse_detail = m_details;
        Model model = new Model();
        String response_post = model.performRequest("PUT",m_title,m_details,null);
        String response = model.performRequest("GET",m_title,m_details,null);
        String response_detail = model.performRequest("GET", null, null, response);
        String responseDelete = model.performRequest("DELETE", null, null, response);
        assertEquals(expectedResponse_title, response);
        assertEquals(expectedResponse_detail, response_detail);
    }
}