package cse.project.team;

import cse.project.team.Controller.Components.Filter;
import cse.project.team.Controller.Components.SortButtonsAZ;
import cse.project.team.Controller.Components.SortButtonsOF;
import cse.project.team.Controller.Components.SortButtonsZA;
import cse.project.team.Controller.Components.SortingStrategy;
import cse.project.team.Model.Model;
import cse.project.team.Model.Components.DalleMock;
import cse.project.team.Server.DBHandler;
import cse.project.team.Server.accountHandler;
import cse.project.team.Server.GPTI;
import cse.project.team.Server.GPTMock;
import cse.project.team.Server.WhisperI;
import cse.project.team.Server.WhisperMock;
import cse.project.team.Server.shareHandler;
import cse.project.team.Views.Components.RecipeList;
import cse.project.team.Model.Components.ColorPicker;
import cse.project.team.Controller.Controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.*;

class AppTest {
    final String mock_title = "Mashed potat";
    final String mock_details = "Get potatoes. Mash. Done.";

    final String other_title = "Pancakes";
    final String other_details = "Get cake. Get pan. Put cake in pan. Done.";

    private static final String SERVER_URL = "http://localhost:8100/";

    DBHandler REChandler = new DBHandler("Test_CSE110_Proj");
    accountHandler ACChandler = new accountHandler("Test_CSE110_Proj");

    final String username = "test";
    final String password = "test";

    @BeforeEach
    public void clearDatabase() {
        REChandler.clear();
        ACChandler.clear();
    }

    @AfterEach
    public void clean(){
        String currentDirectory = System.getProperty("user.dir");
        Path currentPath = Paths.get(currentDirectory);
        String fileName = mock_title + ".jpg";
        Path file = currentPath.resolve(fileName);
        if(fileExists(mock_title)){
            try {
                Files.delete(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void basicTestCheck() throws Exception {
        assertEquals(1 + 1, 2);
    }

    // US 1: View list of saved recipes
    @Test
    public void testViewEmptyList() throws Exception {
        ArrayList<String> list = REChandler.getRecList();
        assertEquals(list.size(), 0);
    }

    @Test
    public void testViewFullList() throws Exception {
        REChandler.doPost(mock_title, mock_details, username, "Breakfast");
        REChandler.doPost(other_title, mock_details, "Lea", "Lunch");
        ArrayList<String> list = REChandler.getRecList();
        assertEquals(mock_title + "yL8z42" + username, list.get(0));
        assertEquals(other_title + "yL8z42" + "Lea", list.get(1));
    }

    // US 2: View Details of Recipe
    @Test
    public void testViewDetail() throws Exception {
        String expectedResponse_detail = "Get potatoes. Mash. Done.";
        REChandler.doPost(mock_title, mock_details, username, "Breakfast");
        String detail = REChandler.getRecDetail(mock_title).get(0);
        String detail_1 = detail.split("%")[0];
        assertEquals(expectedResponse_detail, detail_1);
    }

    // US 3: Edit Recipe
    @Test
    public void testHandleEdit() throws Exception {
        editGiven();
        editWhen(mock_details);
        editThen(mock_details);
    }

    public void editGiven() {
        REChandler.doPost(other_title, other_details, username, "Lunch");
    }

    public void editWhen(String editedDetails) {
        REChandler.doPost(other_title, editedDetails, username, "Lunch");
    }

    public void editThen(String editedDetails) {
        String actualDetails = REChandler.getRecDetail(other_title).get(0);
        String actual_Details = actualDetails.split("%")[0];
        assertEquals(editedDetails, actual_Details);
    }

    // US 4: Save Recipe
    @Test
    public void testSaveNew() throws Exception {
        REChandler.doPost("apple pie", "3 apples, cinnamon, 1 cup brown sugar", username, "Dinner");
        String appleDetail = REChandler.getRecDetail("apple pie").get(0);
        String rhubarbDetail = REChandler.getRecDetail("rhubarb pie").get(0);
        String apple_Detail = appleDetail.split("%")[0];
        String rhubarb_Detail = rhubarbDetail.split("%")[0];
        assertEquals(apple_Detail, "3 apples, cinnamon, 1 cup brown sugar");
        assertEquals(rhubarb_Detail, "Does not exist");
    }

    @Test
    public void testSaveEdited() throws Exception {
        REChandler.doPost("lemon meringue", "2 lemons, butter, sugar", username, "Dinner");
        String outdatedDetail = REChandler.getRecDetail("lemon meringue").get(0);
        String outdated_Detail = outdatedDetail.split("%")[0];
        REChandler.doPost("lemon meringue", "2 lemons, butter, sugar, vanilla extract", username, "Dinner");
        String detail = REChandler.getRecDetail("lemon meringue").get(0);
        String detail_1 = detail.split("%")[0];
        assertEquals(detail_1, "2 lemons, butter, sugar, vanilla extract");
        assertNotEquals(detail_1, "2 lemons, butter, sugar");
        assertNotEquals(detail_1, outdated_Detail);
    }

    // US 5: Delete Recipe
    @Test
    public void testHandleDelete() throws Exception {
        deleteGiven(mock_title, mock_details);
        deleteWhen(mock_title);
        deleteThen(mock_title, "Does not exist");
    }

    public void deleteGiven(String title, String details) {
        // handler.doPost(title, details);
    }

    public void deleteWhen(String title) {
        REChandler.doDelete(title);
    }

    public void deleteThen(String title, String response) {
        assertEquals(response, REChandler.getRecDetail(title).get(0));
    }

    // US 6: Return to main page - Not tested here due to being a GUI component

    // US 7 + US 8
     @Test
    public void testGen() throws Exception {
        GPTI gen = givenGen();
        WhisperI whisper = givenAudio();
        String result = whenGen(gen, whisper);
        thenGen(result);
    }

    public GPTI givenGen() {
        GPTI gen = new GPTMock();
        WhisperI whisper = new WhisperMock();
        return gen;
    }

    public WhisperI givenAudio() {
        WhisperI whisper = new WhisperMock();
        return whisper;
    }

    public String whenGen(GPTI gen, WhisperI audio) throws IOException, URISyntaxException, Exception {
        String whisper = audio.audioGen(new ByteArrayInputStream("MOCK Audio Bytes".getBytes()));
        String newGen = gen.chatgen(whisper);
        return newGen;
    }

    public void thenGen(String newGen) {
        assertEquals("Mashed potat\n Take potatoe. Mash. Done. :)", newGen);
    }

    // End to End Scnario Test MS1
    @Test
    public void testEndToEnd() throws IOException, URISyntaxException, Exception {
        GPTI gen = new GPTMock();
        String newGen = gen.chatgen("dinner potato");
        String title = newGen.split("\n")[0];
        String details = newGen.substring(title.length());
        
        REChandler.doPost(title, details, details, "Dinner");
        
        String detail1 = REChandler.getRecDetail(title).get(0);
        String detail_1 = detail1.split("%")[0];
        assertEquals(details, detail_1);
        
        REChandler.doPost(title, other_details, username, "Dinner");
        
        String detail2 = REChandler.getRecDetail(title).get(0);
        String detail_2 = detail2.split("%")[0];
        assertEquals(other_details, detail_2);
        
        REChandler.doDelete(title);
        
        assertEquals("Does not exist", REChandler.getRecDetail(mock_title).get(0));
    }

    // End to End Scenario Test MS2
    @Test
    public void testEndToEnd2() throws Exception{
        // User Creates an Account
        ACChandler.doPost("yiming105", "pineapple123");

        // Database should be storing this account and no others
        assertEquals(ACChandler.getRecDetail("yiming105"), "pineapple123");
        assertEquals(ACChandler.getRecDetail("jkissinger"), "Does not exist");

        // No new accounts should be able to be made with this username
        String postResult = ACChandler.doPost("yiming105", "gao1125");
        assertEquals(postResult, "Username taken");

        // User should be able to generate new recipes for this account
        String audioText = "dinner potato";
        GPTI gen = new GPTMock();
        String newGen = gen.chatgen(audioText); 

        String title1 = newGen.split("\n")[0]; // mashed potat
        String description1 = newGen.substring(title1.length());
        String mealType1 = Controller.extractMealType(audioText);
        
        REChandler.doPost(title1, description1, "yiming105", 
                mealType1);
        
        // its details are correct in the database
        ArrayList<String> detail1 = REChandler.getRecDetail(title1);
        String description_1 = detail1.get(0);
        String mealType_1 = detail1.get(1);
        assertEquals(description1, description_1);
        assertEquals(mealType1, mealType_1);

        // let's do some more
        REChandler.doPost("Tomato Soup", "Chop 3 basil leaves and 2 tomatoes...", 
                "yiming105", "Lunch");
        REChandler.doPost("Acai Bowl", "Ingredients: yogurt, acai berries, granola", 
                "yiming105", "Breakfast");
        REChandler.doPost("Xmas Ham", "Preheat oven to 350 degrees...", 
                "yiming105", "Dinner");
        
        /*
        RecipeList recList = new RecipeList();

        recList.addRecipe(0, title1, "Dinner");
        recList.addRecipe(1, "Tomato Soup", "Lunch");
        recList.addRecipe(2, "Acai Bowl", "Breakfast");
        recList.addRecipe(3, "Xmas Ham", "Dinner");
        

        // Now, the user can sort as they wish
        SortingStrategy sorterAZ = new SortButtonsAZ();
        sorterAZ.sort(recList);

        ArrayList<String> expectationAZ = new ArrayList<String>(); 
        expectationAZ.add("Acai Bowl");
        expectationAZ.add("mashed potat");
        expectationAZ.add("Tomato Soup");
        expectationAZ.add("Xmas Ham");

        assertEquals(expectationAZ, recList.getTitles());
        */
    }

    // US 9
    // Scenario 1
    @Test
    public void testCreateAccount() {
        GivenNoACC();
        WhenCreateACC();
        ThenNewACC();
    }

    private void GivenNoACC() {
        // list of accounts is already empty
    }

    private void WhenCreateACC() {
        assertEquals("Added", ACChandler.doPost(username, password));
    }

    private void ThenNewACC() {
        assertEquals(ACChandler.getRecDetail(username), password);
    }

    // Scenario 2
    @Test
    public void testCreateAccountUsernameTaken() {
        GivenNoACC();
        WhenCreateACC();
        ThenNoNewACC();
    }

    private void ThenNoNewACC() {
        assertEquals("Username taken", ACChandler.doPost(username, password));
    }

    // US 10
    // BDD Scenario 1
    @Test
    public void testLogin() {
        GivenExistingACC();
        String respones = WhenUserEntersInfo(username, password);
        ThenLogin(respones);
    }

    private void GivenExistingACC() {
        assertEquals("Added", ACChandler.doPost(username, password));
    }

    private String WhenUserEntersInfo(String user, String pass) {
        // check if username and password entered by user exist in the database
        return ACChandler.doPut(user, pass);
    }

    private void ThenLogin(String respones) {
        // how to check if logged in?
        assertEquals("Login", respones);
    }

    // BDD Scenario 2
    @Test
    public void testLoginFail() {
        GivenExistingACC();
        String respones = WhenUserEntersInfo(username, "fail");
        ThenLoginFail(respones);
    }

    private void ThenLoginFail(String respones) {
        // how to check if logged in?
        assertEquals("Wrong info", respones);
    }

    // US 11
    
    // BDD Scenario 2
    @Test
    public void testDalle() throws IOException, URISyntaxException, Exception {
        String genResponse = givenDallePrompt();
        String urlImage = whenDalle(genResponse);
        thenImage(genResponse, urlImage);
        
    }

    // BDD Scenario 1
    @Test
    public void testDalle2() throws IOException, URISyntaxException, Exception {
        String savedRecipe = givenRecipe();
        String urlImage = whenDalle(savedRecipe);
        thenImage(savedRecipe, urlImage);
        
    }

    private String givenRecipe() {
        return mock_title;
    }

    private void thenImage(String genResponse, String urlImage) {
        //test to be sure url is returned
        String expectedResponse = "https://upload.wikimedia.org/wikipedia/en/thumb/9/9a/Trollface_non-free.png/220px-Trollface_non-free.png";
        assertEquals(expectedResponse, urlImage);
        assertEquals(fileExists(genResponse), true);

    }

    private String whenDalle(String genResponse) {
        DalleMock dalle = new DalleMock();
        String dalleResponse = dalle.generateDalle(genResponse);
        dalle.downloadImage(dalleResponse, genResponse);
        return dalleResponse;
    }

    private String givenDallePrompt() throws IOException, URISyntaxException, Exception {
        return whenGen(new GPTMock(), new WhisperMock()).split("\n")[0];
    }

    private boolean fileExists(String title){
        String currentDirectory = System.getProperty("user.dir");
        Path currentPath = Paths.get(currentDirectory);
        String fileName = title + ".jpg";
        Path fileInParent = currentPath.resolve(fileName);
        return Files.exists(fileInParent);
    }


    /*
     * US12:
     * BDD Scenario 1
     */

    @Test
    public void testRefresh() throws IOException, URISyntaxException, Exception {
        GPTI gen = givenGen();
        WhisperI audio = givenAudio();
        String result = whenGen(gen, audio);
        thenGen(result);
        result = whenGen(gen, audio);
        thenGen(result);
    }

    // US 13
    @Test
    public void testServerNotRunning() {
        Model model = givenNoServer();
        String response = whenNoServer(model);
        thenNoServer(response);
    }

    public Model givenNoServer() {
        return new Model();
    }

    public String whenNoServer(Model model) {
        return model.accountRequest("PUT", "", "", null);

    }

    public void thenNoServer(String response) {
        assertNotEquals("Login", response);
    }

    // US 14
    // Scenario 1:
    @Test
    public void testTag(){
        String expected_mealtype = "Breakfast";
        REChandler.doPost(mock_title, mock_details, username, "Breakfast");
        String detail = REChandler.getRecDetail(mock_title).get(1);
        String mealtype = detail.split("%")[0];
        assertEquals(expected_mealtype, mealtype);
    }

    // US 15
    public void testSortedAtoZ() {
        RecipeList mock = new RecipeList();
        givenRecipeList(mock);
        whenSortedAtoZ(mock);
        thenSortedAtoZ(mock);
    }

    private void testSortedZtoA() {
        RecipeList mock = new RecipeList();
        givenRecipeList(mock);
        whenSortedZtoA(mock);
        thenSortedZtoA(mock);
    }

    private void testSortedOtoF() {
        RecipeList mock = new RecipeList();
        givenRecipeList(mock);
        whenSortedOtoF(mock);
        thenSortedOtoF(mock);
    }

    private void givenRecipeList(RecipeList mock) {
        mock.addRecipe(0, "B", "breakfast");
        mock.addRecipe(1, "C", "lunch");
        mock.addRecipe(2, "A", "dinner");
    }

    private void whenSortedAtoZ(RecipeList mock) {
        SortingStrategy sorter = new SortButtonsAZ();
        sorter.sort(mock);
    }

    private void whenSortedZtoA(RecipeList mock) {
        SortingStrategy sorter = new SortButtonsZA();
        sorter.sort(mock);
    }

    private void whenSortedOtoF(RecipeList mock) {
        SortingStrategy sorter = new SortButtonsOF();
        sorter.sort(mock);
    }

    private void thenSortedAtoZ(RecipeList mock) {
        List<String> expected = new ArrayList<>();
        expected.add(0,"A");
        expected.add(1,"B");
        expected.add(2,"C");
        assertEquals(expected, mock.getTitles());
    }

    private void thenSortedZtoA(RecipeList mock) {
        List<String> expected = new ArrayList<>();
        expected.add(0,"C");
        expected.add(1,"B");
        expected.add(2,"A");
        assertEquals(expected, mock.getTitles());
    }

    private void thenSortedOtoF(RecipeList mock) {
        List<String> expected = new ArrayList<>();
        expected.add(0,"A");
        expected.add(1,"C");
        expected.add(2,"B");
        assertEquals(expected, mock.getTitles());
    }

    // US 16
    //BDD Scenerio 1

    public void testfilterbreakfast(){
        RecipeList mock = new RecipeList();
        givenRecipeList(mock);
        whenFilter("breakfast", mock);
        thenBreakfast(mock);
    }
    

    private void thenBreakfast(RecipeList mock) {
        List<String> expected = new ArrayList<>();
        expected.add(1,"B");
        assertEquals(expected, mock.getTitles());
    }

    private void thenLunch() {
    }

    private void thenDinner() {
    }

    private void whenFilter(String mealType, RecipeList mock) {
        Filter.filterSelection(mealType, mock);
    }

    // US 17
    // Scenario 1
    @Test
    public void testSharedRecipe() {
        shareHandler share = givenSharedRecipe();
        whenSharedRecipe(share);
        thenSharedRecipe(share);
    }

    // Scenario 2
    @Test
    public void testSharedRecipeDeleted() throws UnsupportedEncodingException {
        shareHandler share = givenSharedRecipe();
        givenSharedRecipe(share);
        whenSharedRecipeDeleted(share);
        thenSharedRecipeDeleted(share);
    }

    private void thenSharedRecipeDeleted(shareHandler share) {
        assertEquals("Not found", share.doGet(mock_title));
    }

    private void whenSharedRecipeDeleted(shareHandler share) throws UnsupportedEncodingException {
        share.doDelete(mock_title);
    }

    private void givenSharedRecipe(shareHandler share) {
        share.doPost(mock_title + "\n" + mock_details);
    }

    private void thenSharedRecipe(shareHandler share) {
        assertNotEquals("Not found", share.doGet(mock_title));
    }

    private void whenSharedRecipe(shareHandler share) {
        share.doPost(mock_title + "\n" + mock_details);
    }

    private shareHandler givenSharedRecipe() {
        shareHandler share = new shareHandler(new DalleMock());
        return share;
    }

    // Miscellaneous Unit Tests
    @Test
    public void testValidMealtypeColors() {
        ColorPicker picker = new ColorPicker();
        String breakfastColor = picker.tag("Breakfast");
        String lunchColor = picker.tag("Lunch"); 
        String dinnerColor = picker.tag("Dinner");
        assertEquals(breakfastColor, "#58B56B");
        assertEquals(lunchColor, "#FF99C8");
        assertEquals(dinnerColor, "#009FFD");
    }

    @Test
    public void testInvalidMealtypeColor() {
        ColorPicker picker = new ColorPicker();
        String invalidForColor = picker.tag("not a mealtype");
        assertEquals(invalidForColor, "#009FFD");
    }

    @Test
    public void testExtractValidMTs() {
        String breakfastType = Controller
                .extractMealType("breakfast recipe with mango, yogurt, and granola");
        String lunchType = Controller
                .extractMealType("cheese, bell peppers, and black beans for lunch");
        String dinnerType = Controller
                .extractMealType("dinner with pork, green chile, and sour cream");
        assertEquals(breakfastType, "Breakfast");
        assertEquals(lunchType, "Lunch");
        assertEquals(dinnerType, "Dinner");
    }

    @Test
    public void testExtractValidMTsWeirdCapitalization() {
        String breakfastType = Controller
                .extractMealType("BrEakFasT recipe with cherries, apples, and goat cheese");
        assertEquals(breakfastType, "Breakfast");
    }

    @Test
    public void testExtractNoMTs() {
        String noType = Controller
                .extractMealType("dessert with peaches, granola, and chocolate");
        assertEquals(noType, "N/A");
    }

    @Test
    public void testExtractMTsMultipleListed() {
        String multiType1 = Controller
                .extractMealType("dinner. breakfast. lunch");
        // breakfast should be prioritized
        assertEquals(multiType1, "Breakfast");

        String multiType2 = Controller
                .extractMealType("lunch and dinner recipe");
        // lunch should be prioritized over dinner
        assertEquals(multiType2, "Lunch");
    }
}