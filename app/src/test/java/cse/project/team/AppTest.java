package cse.project.team;

import cse.project.team.server.DBHandler;
import cse.project.team.server.accountHandler;
import cse.project.team.server.genMock;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import cse.project.team.server.genI;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
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

    /*
     * @BeforeAll
     * static void initJfxRuntime() {
     * Platform.startup(() -> {});
     * }
     */

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
        String list = REChandler.getRecList();
        String expect = "";
        assertEquals(expect, list);
    }

    @Test
    public void testViewFullList() throws Exception {
        REChandler.doPost(mock_title, mock_details, username);
        REChandler.doPost(other_title, mock_details, username);
        String list = REChandler.getRecList();
        String expect = mock_title + "%" + username + "*" + other_title + "%" + username;
        assertEquals(expect, list);
    }

    // US2: View Details of Recipe
    @Test
    public void testViewDetail() throws Exception {
        String expectedResponse_detail = "Get potatoes. Mash. Done.";
        REChandler.doPost(mock_title, mock_details, username);
        String detail = REChandler.getRecDetail(mock_title);
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
        REChandler.doPost(other_title, other_details, username);
    }

    public void editWhen(String editedDetails) {
        REChandler.doPost(other_title, editedDetails, username);
    }

    public void editThen(String editedDetails) {
        String actualDetails = REChandler.getRecDetail(other_title);
        assertEquals(editedDetails, actualDetails);
    }

    // US4: Save Recipe
    @Test
    public void testSaveNew() throws Exception {
        REChandler.doPost("apple pie", "3 apples, cinnamon, 1 cup brown sugar", username);
        String appleDetail = REChandler.getRecDetail("apple pie");
        String rhubarbDetail = REChandler.getRecDetail("rhubarb pie");
        assertEquals(appleDetail, "3 apples, cinnamon, 1 cup brown sugar");
        assertEquals(rhubarbDetail, "Does not exist");
    }

    @Test
    public void testSaveEdited() throws Exception {
        REChandler.doPost("lemon meringue", "2 lemons, butter, sugar", username);
        String outdatedDetail = REChandler.getRecDetail("lemon meringue");
        REChandler.doPost("lemon meringue", "2 lemons, butter, sugar, vanilla extract", username);
        String detail = REChandler.getRecDetail("lemon meringue");
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
        // handler.doPost(title, details);
    }

    public void deleteWhen(String title) {
        REChandler.doDelete(title);
    }

    public void deleteThen(String title, String response) {
        assertEquals(response, REChandler.getRecDetail(title));
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
    public void testEndToEnd() throws IOException, URISyntaxException, Exception {
        genI gen = new genMock();
        String newGen = gen.chatgen("dinner potato");
        String title = newGen.split("\n")[0];
        String details = newGen.substring(title.length());
        REChandler.doPost(title, details, details);
        assertEquals(details, REChandler.getRecDetail(title));
        REChandler.doPost(title, other_details, username);
        assertEquals(other_details, REChandler.getRecDetail(title));
        REChandler.doDelete(title);
        assertEquals("Does not exist", REChandler.getRecDetail(mock_title));
    }

    /*
     * US 9: Create an account
     */

    // US9: Create an account
    @Test
    public void testCreateAccount() {
        GivenNoACC();
        WhenCreateACC(username, password, "Added");
        ThenNewACC();
    }

    private void GivenNoACC() {
        // list of account is already empty
    }

    private void WhenCreateACC(String user, String pwd, String expectation) {
        assertEquals(expectation, ACChandler.doPost(user, pwd));
    }

    private void ThenNewACC() {
        assertEquals(ACChandler.getRecDetail(username), password);
    }

    // US9: Create an account fails: account name taken
    @Test
    public void testCreateAccountUsernameTaken() {
        GivenNoACC();
        WhenCreateACC(username, password, "Added");
        ThenNoNewACC("Username taken", true);
    }

    private void ThenNoNewACC(String errorMsg, boolean added) {
        if (added) {
            assertEquals(errorMsg, ACChandler.doPost(username, password));
        }
    }

    // Tries to create an account, but password field is empty
    @Test
    public void testCreateAccountEmptyPassword() {
        GivenNoACC();
        WhenCreateACC("bananajoe", "", "Empty input");
        ThenNoNewACC("Empty input", false);
    }

    /*
     * @Test
     * public void testExistingAccount() throws Exception{
     * ListView listView = new ListView();
     * DetailView detView = new DetailView();
     * GenerateView genView = new GenerateView();
     * LoginView loginView = new LoginView();
     * Model model = new Model();
     * 
     * Controller controller = new Controller(listView, detView, genView,loginView,
     * model, new Stage());
     * 
     * // Account doesn't yet exist
     * assertEquals(ACChandler.getRecDetail("Jack"), "Does not exist");
     * 
     * loginView.setUsername("Jack");
     * loginView.setPassword("pineapples101");
     * controller.handleCreateButton(new ActionEvent());
     * 
     * // New username and password should be in daatabase
     * assertEquals(ACChandler.getRecDetail("Jack"), "pineapples101");
     * 
     * loginView.setUsername("Jack");
     * loginView.setPassword("pineapples102");
     * controller.handleCreateButton(new ActionEvent());
     * 
     * // Password should not have changed, this account already exists
     * assertEquals(ACChandler.getRecDetail("Jack"), "pineapples101");
     * // Correct error message should be displayed
     * assertEquals(loginView.getMessageText(),
     * "This account already exists. Please log in!");
     * }
     */

    /*
     * US 10: Login and logout
     */

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

    private void ThenLoginFail(String respones) {
        // how to check if logged in?
        assertEquals("Wrong info", respones);
    }

    // BDD Scenario 2
    @Test
    public void testLoginFail() {
        GivenExistingACC();
        String respones = WhenUserEntersInfo(username, "fail");
        ThenLoginFail(respones);
    }

    /*
     * US 11: Display images for generated recipes
     */
    // test generating a picture and using it
    @Test
    public void testFindImage() throws IOException, URISyntaxException, Exception {
        // Assert that the printed message matches the expected output
        String currentDirectory = System.getProperty("user.dir");
        // Specify the file name to search for
        String fileName = "dalleTest.jpg";
        // Assert that the file exists
        boolean fileFound = searchFile(new File(currentDirectory), fileName);
        assertTrue(fileFound);
    }

    private boolean searchFile(File directory, String fileName) {
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && file.getName().equals(fileName)) {
                        return true; // File found
                    }
                    if (file.isDirectory()) {
                        if (searchFile(file, fileName)) {
                            return true; // File found in a subdirectory
                        }
                    }
                }
            }
        }
        return false;
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
     public void testRefresh() throws IOException, URISyntaxException, Exception{
        genI gen = givenGen();
        String result = whenGen(gen);
        thenGen(result);
        result = whenGen(gen);
        thenGen(result);
     }



}