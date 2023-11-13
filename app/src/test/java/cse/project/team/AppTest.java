package cse.project.team;

import cse.project.team.server.RequestHandler;
import cse.project.team.server.genAPI;
import cse.project.team.server.server;
import org.junit.jupiter.api.Test;
import org.junit.AfterClass;
import org.junit.Before;
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

    @Before
    public void clearDatabase() {
        RequestHandler handler = new RequestHandler(new genAPI());
        handler.clear();
    }

    // the server should be already running
    @Test
    public void testCheck() throws Exception{
        assertEquals(1+1, 2);
    }

    @Test
    public void testHandleEditRecipe() throws Exception{
        String expectedResponse_detail = "Get potatoes. Mash. Done.";
        RequestHandler rh = new RequestHandler(new genAPI());
        rh.clear();
        rh.doPost(m_title, expectedResponse_detail);
        String detail = rh.getRecDetail(m_title);
        assertEquals(expectedResponse_detail, detail);


        
    }


    @Test
    public void testEmptyList() throws Exception{
        RequestHandler rh = new RequestHandler(new genAPI());
        rh.clear();
        String list = rh.getRecList();
        String expect = "";
        assertEquals(expect,list);
    }

    @Test
    public void testFullList() throws Exception{
        RequestHandler rh = new RequestHandler(new genAPI());
        rh.clear();
        rh.doPost(m_title, m_details);
        rh.doPost(p_title, p_details);
        String list = rh.getRecList();
        String expect = m_title+"*"+p_title;
        assertEquals(expect,list);
        rh.clear();
    }

    @Test
    public void testHandleViewDetail2() throws Exception{
        String expectedResponse_detail = "Get potatoes. Mash. Done.";
        RequestHandler rh = new RequestHandler(new genAPI());
        rh.clear();
        rh.doPost(m_title, m_details);
        String detail = rh.getRecDetail(m_title);
        assertEquals(expectedResponse_detail, detail); 
    }

}