package cse.project.team;

import cse.project.team.server.RequestHandler;
import cse.project.team.server.genAPI;
import cse.project.team.server.genMock;
import cse.project.team.server.server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.*;
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
    RequestHandler handler = new RequestHandler(new genMock());

    @BeforeEach
    public void clearDatabase() {
        handler.clear();
    }

    @Test
    public void testCheck() throws Exception {
        assertEquals(1 + 1, 2);
    }

    @Test
    public void testHandleEditRecipe() throws Exception {
        String expectedResponse_detail = "Get potatoes. Mash. Done.";
        handler.doPost(m_title, expectedResponse_detail);
        String detail = handler.getRecDetail(m_title);
        assertEquals(expectedResponse_detail, detail);
    }

    @Test
    public void testEmptyList() throws Exception{
        String list = handler.getRecList();
        String expect = "";
        assertEquals(expect, list);
    }

    @Test
    public void testFullList() throws Exception {
        handler.doPost(m_title, m_details);
        handler.doPost(p_title, p_details);
        String list = handler.getRecList();
        String expect = m_title + "*" + p_title;
        assertEquals(expect, list);
        handler.clear();
    }

    @Test
    public void testHandleViewDetail2() throws Exception {
        String expectedResponse_detail = "Get potatoes. Mash. Done.";
        handler.doPost(m_title, m_details);
        String detail = handler.getRecDetail(m_title);
        assertEquals(expectedResponse_detail, detail); 
    }

    @Test
    public void testSaveNew() throws Exception {
        handler.doPost("apple pie", "3 apples, cinnamon, 1 cup brown sugar");
        String appleDetail = handler.getRecDetail("apple pie");
        String rhubarbDetail = handler.getRecDetail("rhubarb pie");
        assertEquals(appleDetail, "3 apples, cinnamon, 1 cup brown sugar");
        assertEquals(rhubarbDetail, "Does not exist");
    }

    @Test
    public void testSaveEdited() throws Exception {
        handler.doPost("lemon meringue", "2 lemons, butter, sugar");
        String outdatedDetail = handler.getRecDetail("lemon meringue");
        handler.doPost("lemon meringue", "2 lemons, butter, sugar, vanilla extract");
        String detail = handler.getRecDetail("lemon meringue");
        assertEquals(detail, "2 lemons, butter, sugar, vanilla extract");
        assertNotEquals(detail, "2 lemons, butter, sugar");
        assertNotEquals(detail, outdatedDetail);
    }

    @Test
    public void testHandleDELETE() throws Exception {
        deleteGiven(m_title, m_details);
        deleteWhen(m_title);
        deleteThen(m_title, "Does not exist");
    }

    public void deleteGiven(String title, String details) {
        handler.doPost(title, details);
    }

    public void deleteWhen(String title) {
        handler.doDelete(title);
    }

    public void deleteThen(String title, String response) {
        assertEquals(response, handler.getRecDetail(title));
    }
}