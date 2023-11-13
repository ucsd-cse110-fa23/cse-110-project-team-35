package cse.project.team;

<<<<<<< HEAD
import cse.project.team.server.RequestHandler;
=======
>>>>>>> 4cebd11c9db823d7a8004ab39a61f01553849db9
import cse.project.team.server.genAPI;
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
        RequestHandler rh = new RequestHandler(new genAPI());
        String detail = rh.getRecDetail(m_title);
        assertEquals(expectedResponse_detail, detail);
    }

    @Test
    public void testHandleGet() throws IOException {
        String expectedResponse_title = "Mashed potat";
        String expectedResponse_detail = m_details;
        RequestHandler rh = new RequestHandler(new genAPI());
        String detail = rh.getRecDetail(m_title);
        assertEquals(expectedResponse_detail, detail);
    }

    @Test
    public void testHandlePUTandGET() throws IOException {
        String expectedResponse_title = "Mashed potat";
        String expectedResponse_detail = m_details;
        RequestHandler rh = new RequestHandler(new genAPI());
        String detail = rh.getRecDetail(m_title);
        assertEquals(expectedResponse_detail, detail);
    }

    @Test
    public void testHandlePUT() throws IOException {
        String expectedResponse_title = "Mashed potat";
        String expectedResponse_detail = m_details;
        RequestHandler rh = new RequestHandler(new genAPI());
        String detail = rh.getRecDetail(m_title);
        assertEquals(expectedResponse_detail, detail);
    }

    @Test
    public void testHandlDELETE() throws Exception {
        String expectedResponse_title = "Mashed potat";
        String expectedResponse_detail = m_details;
        RequestHandler rh = new RequestHandler(new genAPI());
        String detail = rh.getRecDetail(m_title);
        assertEquals(expectedResponse_detail, detail);
    }
}