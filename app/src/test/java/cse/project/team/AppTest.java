package cse.project.team;

import cse.project.team.server.RequestHandler;
import cse.project.team.server.genMock;
import org.junit.jupiter.api.BeforeEach;
import cse.project.team.server.genI;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.net.URISyntaxException;

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

    // US3: Edit Recipe
    @Test
    public void testHandleEditRecipe() throws Exception {
        // Given a recipe title and details are visible
        editGiven();
        // When the user edits the recipe
        editWhen(m_details);
        // then the recipe should be saved
        editThen(m_details);
    }

    public void editGiven() {
        handler.doPost(p_title, p_details);
    }

    public void editWhen(String editedDetails) {
        handler.doPost(p_title, editedDetails);
    }

    public void editThen(String editedDetails) {
        String actualDetails = handler.getRecDetail(p_title);
        assertEquals(editedDetails, actualDetails);
    }

    @Test
    public void testEmptyList() throws Exception {
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

    // Given voice input.
    // When the user use the start and stop button to record the voice input,
    // Then a general recipe will be generated based on the input voice.
    @Test
    public void TestGen() throws Exception{
        givenGen();
        whenGen();
        thenGen();
    }

    public void givenGen(){
        
    }

    public void whenGen(){
        
    }

    public void thenGen() throws IOException, URISyntaxException, Exception{
        genI gen = new genMock();
        String newGen = gen.generate();
        assertEquals("Mashed potats?\n Take potatoe. Mash. Done. :)", newGen);
    }

}