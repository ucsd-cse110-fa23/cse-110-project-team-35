package cse.project.team;

import cse.project.team.server.DBHandler;
import cse.project.team.server.genMock;
import org.junit.jupiter.api.BeforeEach;
import cse.project.team.server.genI;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.net.URISyntaxException;

class AppTest {
    String mock_title = "Mashed potat";
    String mock_details = "Get potatoes. Mash. Done.";

    String other_title = "Pancakes";
    String other_details = "Get cake. Get pan. Put cake in pan. Done.";

    private static final String SERVER_URL = "http://localhost:8100/";
    DBHandler handler = new DBHandler();

    @BeforeEach
    public void clearDatabase() {
        handler.clear();
    }

    @Test
    public void basicTestCheck() throws Exception {
        assertEquals(1 + 1, 2);
    }

    // US1: View list of saved recipes
    @Test
    public void testViewEmptyList() throws Exception {
        String list = handler.getRecList();
        String expect = "";
        assertEquals(expect, list);
    }

    @Test
    public void testViewFullList() throws Exception {
        handler.doPost(mock_title, mock_details);
        handler.doPost(other_title, other_details);
        String list = handler.getRecList();
        String expect = mock_title + "*" + other_title;
        assertEquals(expect, list);
        handler.clear();
    }

    // US2: View Details of Recipe
    @Test
    public void testViewDetail() throws Exception {
        String expectedResponse_detail = "Get potatoes. Mash. Done.";
        handler.doPost(mock_title, mock_details);
        String detail = handler.getRecDetail(mock_title);
        assertEquals(expectedResponse_detail, detail);
    }

    // US3: Edit Recipe
    @Test
    public void testHandleEdit() throws Exception {
        editGiven();
        editWhen(mock_details);
        editThen(mock_details);
    }

    public void editGiven() {
        handler.doPost(other_title, other_details);
    }

    public void editWhen(String editedDetails) {
        handler.doPost(other_title, editedDetails);
    }

    public void editThen(String editedDetails) {
        String actualDetails = handler.getRecDetail(other_title);
        assertEquals(editedDetails, actualDetails);
    }

    // US4: Save Recipe
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

    // US5: Delete Recipe
    @Test
    public void testHandleDelete() throws Exception {
        deleteGiven(mock_title, mock_details);
        deleteWhen(mock_title);
        deleteThen(mock_title, "Does not exist");
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

    /* US6: Return to main page
       Not tested here due to being a GUI component
    */


    /* US7: Generate Recipe Based on Voice Input
     * Given voice input.
     * When the user use the start and stop button to record the voice input,
     * Then a general recipe will be generated based on the input voice. 
     * */

    @Test
    public void testGen() throws Exception {
        genI gen = givenGen();
        String result = whenGen(gen);
        thenGen(result);
    }

    public genI givenGen() {
        genI gen = new genMock();
        return gen;
    }

    public String whenGen(genI gen) throws IOException, URISyntaxException, Exception {
        String newGen = gen.chatgen("dinner, potato");
        return newGen;
    }

    public void thenGen(String newGen) {
        assertEquals("Mashed potats?\n Take potatoe. Mash. Done. :)", newGen);
    }

    /* US8: Prompt users to list ingredients and specify the meal type,
       offering options like "Breakfast," "Lunch," or "Dinner."

       Not tested here due to implementation being combined with the previous story

    // End to End Scnario Test
    @Test
    public void testEndToEnd() throws IOException, URISyntaxException, Exception {
        genI gen = new genMock();
        String newGen = gen.chatgen("dinner potato");
        String title = newGen.split("\n")[0];
        String details = newGen.substring(title.length());
        handler.doPost(title, details);
        assertEquals(details, handler.getRecDetail(title));
        handler.doPost(title, other_details);
        assertEquals(other_details, handler.getRecDetail(title));
        handler.doDelete(title);
        assertEquals("Does not exist", handler.getRecDetail(mock_title));
    }
}