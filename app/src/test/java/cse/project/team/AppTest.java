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
    String mock_title = "Mashed potat";
    String mock_details = "Get potatoes. Mash. Done.";
    
    String other_title = "Pancakes";
    String other_details = "Get cake. Get pan. Put cake in pan. Done.";
    
    private static final String SERVER_URL = "http://localhost:8100/";
    RequestHandler handler = new RequestHandler(new genMock());

    @BeforeEach
    public void clearDatabase() {
        handler.clear();
    }

    @Test
    public void basicTestCheck() throws Exception {
        assertEquals(1 + 1, 2);
    }

    @Test
    public void testHandleEdit() throws Exception {
        editGiven();
        editWhen(mock_details);
        editThen(mock_details);
    }

    @Test
    public void testHandleDelete() throws Exception {
        deleteGiven(mock_title, mock_details);
        deleteWhen(mock_title);
        deleteThen(mock_title, "Does not exist");
    }

    @Test
    public void testGen() throws Exception {
        genI gen = givenGen();
        String result = whenGen(gen);
        thenGen(result);
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

    public void deleteGiven(String title, String details) {
        handler.doPost(title, details);
    }

    public void deleteWhen(String title) {
        handler.doDelete(title);
    }

    public void deleteThen(String title, String response) {
        assertEquals(response, handler.getRecDetail(title));
    }

    public genI givenGen() {
        genI gen = new genMock();
        return gen;
    }

    public String whenGen(genI gen) throws IOException, URISyntaxException, Exception {
        String newGen = gen.generate();
        return newGen;
    }

    public void thenGen(String newGen) {
        assertEquals("Mashed potats?\n Take potatoe. Mash. Done. :)", newGen);
    }

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

    @Test
    public void testViewDetail() throws Exception {
        String expectedResponse_detail = "Get potatoes. Mash. Done.";
        handler.doPost(mock_title, mock_details);
        String detail = handler.getRecDetail(mock_title);
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

}