package cse.project.team;

import cse.project.team.Model.Model;
import cse.project.team.Model.Components.DalleMock;
import cse.project.team.Server.DBHandler;
import cse.project.team.Server.accountHandler;
import cse.project.team.Server.genI;
import cse.project.team.Server.genMock;
import cse.project.team.Server.shareHandler;
import cse.project.team.Model.Components.ColorPicker;
import cse.project.team.Controller.Controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.text.*;
import javafx.application.Application;
import javafx.application.Platform;

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

    @Test
    public void basicTestCheck() throws Exception {
        assertEquals(1 + 1, 2);
    }

    // US1: View list of saved recipes
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

    // US2: View Details of Recipe
    @Test
    public void testViewDetail() throws Exception {
        String expectedResponse_detail = "Get potatoes. Mash. Done.";
        REChandler.doPost(mock_title, mock_details, username, "Breakfast");
        String detail = REChandler.getRecDetail(mock_title).get(0);
         String detail_1 = detail.split("%")[0];
        assertEquals(expectedResponse_detail, detail_1);
    }

    // US3: Edit Recipe
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

    // US4: Save Recipe
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

    // US5: Delete Recipe
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

    /*
     * US6: Return to main page
     * Not tested here due to being a GUI component
     */

    /*
     * US7: Generate Recipe Based on Voice Input
     * Given voice input.
     * When the user use the start and stop button to record the voice input,
     * Then a general recipe will be generated based on the input voice.
     */

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

    /*
     * US8: Prompt users to list ingredients and specify the meal type,
     * offering options like "Breakfast," "Lunch," or "Dinner."
     * 
     * Not tested here due to implementation being combined with the previous story
     */ 
      // End to End Scnario Test MS1
      
      @Test
      public void testEndToEnd() throws IOException, URISyntaxException, Exception
      {
      genI gen = new genMock();
      String newGen = gen.chatgen("dinner potato");
      String title = newGen.split("\n")[0];
      String details = newGen.substring(title.length());

      REChandler.doPost(title, details, username, "Dinner");

      String detail1 = REChandler.getRecDetail(title).get(0);
      String detail_1= detail1.split("%")[0];
      assertEquals(details, detail_1);

      REChandler.doPost(title, other_details, username, "Dinner");

      String detail2 = REChandler.getRecDetail(title).get(0);
      String detail_2 = detail2.split("%")[0];
      assertEquals(other_details, detail_2);

      REChandler.doDelete(title);

      // Deleted recipes should not be findable
      assertEquals("Does not exist", REChandler.getRecDetail(mock_title).get(0));
      }
     

    /*
     * US 9: Create an account
     */

    // US9: Create an account
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

    // US9: Create an account fails: account name taken
    @Test
    public void testCreateAccountUsernameTaken() {
        GivenNoACC();
        WhenCreateACC();
        ThenNoNewACC();
    }

    private void ThenNoNewACC() {
        assertEquals("Username taken", ACChandler.doPost(username, password));
    }

    /*
     * 10. User Story: Login and logout [High, 8 hrs, Iteration 1]
     * Narrative:
     * As a user of the app,
     * I want to be able to log in and out of the app,
     * So that I can access my account details on multiple devices.
     * BDD Scenario 1: Login to an existing account.
     * Given the user has an existing account and did not select automatic login,
     * When the application starts,
     * Then the create account/login page appears;
     * When the user fills in correct username and password information,
     * And clicks the “Login” button,
     * Then the user is logged into their account, and the recipe list page is
     * shown.
     * BDD Scenario 2: Fail to login to an existing account: incorrect name or
     * password.
     * Given the user is on the create account/login page,
     * When the user enters a username and password combination that does not exist
     * in
     * the database,
     * And clicks the “Login” button,
     * Then the user sees an error message and stays on the create account/login
     * page.
     * 
     * BDD Scenario 3: User signs up for automatic login.
     * Given the user has an existing account,
     * When the user selects the “Auto Login” checkbox on the create account/login
     * page,
     * Then the user’s login information is saved locally;
     * When the user opens the app the next time on the same device,
     * Then the user goes directly to the recipe list page in their account.
     * BDD Scenario 4: Logout by clicking the “Logout” button.
     * Given the user is on the recipe list page and is logged into their account,
     * When the “Logout” button is clicked,
     * Then the user is logged out, and the create account/login page is shown.
     * 
     * BDD Scenario 5: Logout by closing the app.
     * Given the user is logged in on the app,
     * When the application is closed,
     * Then the user is logged out of their account.
     */
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

    /*
     * US 11: Display images for generated recipes
     */
    // test generating a picture and using it

    // BDD Scenario 2: A new recipe is generated.
	// Given the user has clicked the “Generate Recipe” or “Refresh” button,
	// And a valid voice input has been provided,
	// When the new recipe is done generating,
	// Then an image of the potential dish is shown on the recipe detail page.

    @Test
    public void testDalle() throws IOException, URISyntaxException, Exception {
        
        
    }

    private String givenDallePrompt() {
        return mock_title;
    }




    /*
     * US12:
     * BDD Scenario 1: The user clicks “Refresh” on an unsaved recipe.
     * Given the user has generated a new recipe that has not yet been saved,
     * And the user is on the recipe details page,
     * When the user clicks the “Refresh” button,
     * Then a new recipe is generated based on the same voice input as the previous;
     * When the new recipe is done generating,
     * Then the new recipe details are shown on the recipe details page.
     */

    

    @Test
    public void testRefresh() throws IOException, URISyntaxException, Exception {
        genI gen = givenGen();
        String result = whenGen(gen);
        thenGen(result);
        result = whenGen(gen);
        thenGen(result);
    }

    /*
     * US13:
     * BDD Scenario 1: Server is down.
     * Given the server is not running,
     * When the app is launched,
     * Then the create account/login page is shown,
     * And an error message saying “Sorry, the server is down!” is displayed;
     * When the user enters their information and clicks the “Login” or “Create
     * Account” button,
     * Then nothing changes.
     */
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

    /*
     * US 14:
     * BDD Scenario 1: User views recipe list
     * Given the meal type of each recipe has been correctly saved in the database,
     * When the user views the recipe list page,
     * Then the meal type of each recipe is displayed next to the recipe’s name.
     */
    // Scenario 1:
    @Test
    public void testTag(){
        String expected_mealtype = "Breakfast";
        REChandler.doPost(mock_title, mock_details, username, "Breakfast");
        String detail = REChandler.getRecDetail(mock_title).get(1);
        String mealtype = detail.split("%")[0];
        assertEquals(expected_mealtype, mealtype);
    }

    /*
     * US 15:
     * BDD Scenario 1: Sort multiple recipes.
     * Given the user in on the recipe list page,
     * And the recipe list is not empty,
     * When the user selects the “A - Z” sorting option from the dropdown,
     * Then recipes will be displayed in alphabetical order from A to Z;
     * When the user selects the “Z - A” sorting option from the dropdown,
     * Then recipes will be displayed in reverse alphabetical order from Z to A;
     * When the user selects the “New - Old” sorting option from the dropdown,
     * Then recipes will be displayed in chronological order;
     * When the user selects the “Old - New” sorting option from the dropdown,
     * Then recipes will be displayed in reverse chronological order.
     * BDD Scenario 2: No sort option selected.
     * Given the user in on the recipe list page,
     * And the recipe list is not empty,
     * When the user doesn’t select any sorting options from the dropdown,
     * Then the recipe list is displayed in reverse chronological order.
     * BDD Scenario 3: Sort an empty list.
     * Given the user in on the recipe list page,
     * And the recipe list is empty,
     * When the user select a sorting option from the dropdown,
     * Then nothing changes.
     */
    // Scenario 1:
    @Test
    public void testSortFullRecipe(){
    }
    // Scenario 2:
    @Test
    public void testNoSortRecipe(){
    }
    // Scenario 3:
    @Test
    public void testSortEmptyRecipe(){
    }


    /*
     * US17: 
     * BDD Scenario 1: An existing recipe was shared.
     * Given a saved recipe that exists in the database,
     * When the user clicks the “Share” button on the recipe details page,
     * Then a web URL linking to the recipe’s details will be generated;
     * When the URL is pasted in a web browser,
     * Then the recipe details will be displayed in a web browser.
     * BDD Scenario 2: A deleted recipe was shared.
     * Given the share URL of a recipe has already been generated,
     * When the recipe is deleted,
     * And the share URL is clicked,
     * Then a web page displaying a “Recipe not found, sorry!” error is opened.
     */
    //Scenario 1
    @Test
    public void testSharedRecipe() {
        shareHandler share = givenSharedRecipe();
        whenSharedRecipe(share);
        thenSharedRecipe(share);
    }
    //Scenario 2
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