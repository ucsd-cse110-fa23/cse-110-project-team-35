package cse.project.team.Controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import cse.project.team.Controller.Components.SortButtonsAZ;
import cse.project.team.Controller.Components.SortButtonsOF;
import cse.project.team.Controller.Components.SortButtonsZA;
import cse.project.team.Controller.Components.SortingStrategy;
import cse.project.team.Model.Model;
import cse.project.team.Views.DetailView;
import cse.project.team.Views.GenerateView;
import cse.project.team.Views.ListView;
import cse.project.team.Views.LoginView;
import cse.project.team.Views.Components.RecipeList;
import cse.project.team.Views.Components.RecipeTitle;

public class Controller {
    private ListView listView;
    private DetailView detView;
    private String whisper;
    private GenerateView genView;
    private LoginView loginView;
    private Model model;
    private Stage stage;
    private Scene listScene, detailScene, generateScene, loginScene;
    private SortingStrategy sortingStrat;

    final File STYLE = new File("style.css");
    final String STYLESHEET = "file:" + STYLE.getPath();
    final int HEIGHT = 750;
    final int WIDTH = 380;

    File autoLogInfile = new File("autoLogIn.txt");

    public Controller(ListView listView,
            DetailView detView,
            GenerateView genView,
            LoginView loginView,
            Model model,
            Stage stage) {

        this.listView = listView;
        this.detView = detView;
        this.genView = genView;
        this.loginView = loginView;
        this.model = model;
        this.stage = stage;
        this.whisper = "";

        createDetailScene();
        createListScene();
        createGenerateScene();
        createLoginScene();
        beginLogin();

        this.detView.setBackButton(this::handleBackButton);
        this.detView.setSaveButton(this::handleSaveButton);
        this.detView.setDeleteButton(this::handleDeleteButton);
        this.detView.setRefreshButton(this::handleRefreshButton);

        this.listView.setRecipeButtons(this::handleRecipeButtons);
        this.listView.setGenerateButton(this::handleGenerateButton);
        this.listView.SetLogOutButton(this::handleLogOutButton);
        this.listView.setFilter(this::handleFilterSelection);
        this.listView.setSort(this::handleSortSelection);

        this.genView.setBackButton(this::handleGenerateBackButton);
        this.genView.setStartButton(this::handleGenerateStartButton);

        this.loginView.setCreateButton(this::handleCreateButton);
        this.loginView.setLoginButton(this::handleLoginButton);

        this.detView.setShareButton(this::handleShareButton);
    }

    private void createListScene() {
        loadRecipeList();
        listScene = new Scene(listView, WIDTH, HEIGHT);
        listScene.getStylesheets().add(STYLESHEET);
    }

    private void createGenerateScene() {
        generateScene = new Scene(this.genView, WIDTH, HEIGHT);
        generateScene.getStylesheets().add(STYLESHEET);
    }

    private void createDetailScene() {
        detailScene = new Scene(this.detView, WIDTH, HEIGHT);
        detailScene.getStylesheets().add(STYLESHEET);
    }

    private void createLoginScene() {
        loginScene = new Scene(loginView, WIDTH, HEIGHT);
        loginScene.getStylesheets().add(STYLESHEET);
    }

    private void setListScene() {
        loadRecipeList();
        stage.setScene(listScene);
    }

    private void setGenerateScene() {
        genView.startTextAnim();
        stage.setScene(generateScene);
    }

    private void setDetailScene() {
        detView.setEditButtonTextToEdit();
        detView.getDetailTextArea().setEditable(false);
        detView.resetLinkText();
        stage.setScene(detailScene);
    }

    private void loadRecipeList() {
        listView.getRecipeList().getChildren().clear();

        // Get recipe titles for the given user
        String[] rlist = model.dBRequest("GET", null, null, null, null, null).split("xF9j");

        // Parsing the list of recipe titles, for each recipe,
        // find the mealtype (second GET request) and add to recipeList
        for (String i : rlist) {
            String[] info = i.split("yL8z42");
            if (info[1].equals(loginView.getUsername())) {
                String[] recDets = model.dBRequest("GET", null, null, null, null, info[0]).split("xF9j");
                listView.getRecipeList().addRecipe(0, info[0], recDets[1]);
            }
        }
        listView.setRecipeButtons(this::handleRecipeButtons);
    }

    private void beginLogin() {
        try {
             // If auto-login saved, log in.
            if (autoLogInfile.length() > 1) {
                FileReader fileReader = new FileReader(autoLogInfile);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String line = bufferedReader.readLine();
                String[] userInfo = line.split(",");
                bufferedReader.close();

                login(userInfo[0], userInfo[1]);

             // If not, show login page.
            } else {
                setLoginScene();
            }
        } catch (IOException e) {
            setLoginScene();
            showError();
        }
    }

    private void login(String username, String password) {
        loginView.setUsername(username);
        loginView.setPassword(password);

        String response = model.accountRequest("PUT", username, password, null);
        if (response.equals("Login")) {
            setListScene();
        } else {
            setLoginScene();
            showError();
        }
    }

    private void setLoginScene() {
        stage.setScene(loginScene);
    }

    private void showError() {
        loginView.setMessageText("Looks like the pantry is locked from our end... \nPlease try again later!");
    }

    private void handleRecipeButtons(MouseEvent event) {
        String recipeTitle = ((RecipeTitle) event.getSource()).getTitle();
        String[] recInfo = model.dBRequest("GET", null, null, null, null, recipeTitle).split("xF9j");
        String details = recInfo[0];
        String mealType = recInfo[1];
        String imagePath = new String(recipeTitle + ".jpg");

        setDetailScene();
        detView.addDetails(recipeTitle, details.trim(), mealType);

        // generate detail view image and set it
        // needed for smooth transition and not freezing
        Thread t = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        model.generateImage(recipeTitle);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                detView.setImage(imagePath);
                            }
                        });
                    }
                });

        t.start();
    }

    private void handleBackButton(ActionEvent event) {
        detView.reset();
        setListScene();
    }

    private void handleGenerateBackButton(ActionEvent event) {
        setListScene();
        genView.reset();
    }

    private void handleGenerateStartButton(ActionEvent event) {
        if (((Button) event.getSource()).getText().equals("Start")) {
            startRecording(event);
        } else {
            stopRecording(event);

            // Generate recipe and image
            Thread t = new Thread(
                    new Runnable() {
                        @Override
                        public void run() {
                            String audioTxt = model.genRequest("POST", null);
                            System.out.println(audioTxt);

                            String[] words = audioTxt.split("\\s+");
                            int wordCount = words.length;

                            // Prompt user to specify meal type if missing
                            if (audioTxt.equals("Error")) {
                                missingItem("mealtype");
                            } else if (wordCount == 1) {
                                missingItem("ingredient");
                            } else {
                                createRecipeAndImage(audioTxt);
                            }
                        }
                    });

            t.start();
        }

    }

    private void startRecording(ActionEvent event) {
        // Begin recording
        model.startRec();

        // Change UI to show recording has started
        genView.disableBackButton();
        genView.showRecLabel();
        ((Button) event.getSource()).setText("Stop");
    }

    private void stopRecording(ActionEvent event) {
        // Stop recording
        model.stopRec();

        // Change UI to show recording has stopped
        ((Button) event.getSource()).setText("Start");
        genView.disableStartButton();
        genView.setRecordingLabel("Generating your new recipe! Please wait...");
    }

    private void missingItem(String item) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (item.equalsIgnoreCase("mealtype"))
                    genView.setRecordingLabel("Make sure you say breakfast, lunch, or dinner!");
                
                if (item.equalsIgnoreCase("ingredient"))
                    genView.setRecordingLabel("Make sure you list ingredients!");
                
                    genView.showRecLabel();
                genView.enableBackButton();
                genView.enableStartButton();
            }
        });

    }

    // If more than one mealtype mentioned, returns in order of Brk, Lun, Din
    public static String extractMealType(String audioText) {
        audioText = audioText.toLowerCase();
        if (audioText.contains("breakfast")) {
            return "Breakfast";
        } else if (audioText.contains("lunch")) {
            return "Lunch";
        } else if (audioText.contains("dinner")) {
            return "Dinner";
        } else {
            return "N/A";
        }
    }

    private void createRecipeAndImage(String audioTxt) {
        String mealType = extractMealType(audioTxt);
        this.whisper = audioTxt;
        // Generate recipe through ChatGPT
        String recipe = model.genRequest("GET", audioTxt);
        String[] recipeTitles = recipe.split("\n");
        String title = recipeTitles[0].replaceAll("[^a-zA-Z0-9\\s]", "");

        // Generate image based on recipe title through DALL-E
        model.generateImage(title);

        // Save image on local computer
        String imagePath = new String(title + ".jpg");

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                // Show image and recipe details
                detView.addDetails(recipe.split("\n")[0],
                        recipe.substring(recipe.split("\n")[0].length()).trim(),
                        mealType);
                detView.disableButtons(false);
                detView.setImage(imagePath);

                setDetailScene();
                detView.showRefreshButton();
                genView.reset();
            }
        });

    }

    private void handleGenerateButton(ActionEvent event) {
        setGenerateScene();
    }

    private void handleSaveButton(ActionEvent event) {
        System.out.println(detView.getMealTypeText());
        model.dBRequest("PUT", detView.getCurrTitle(), detView.getDetailText(), loginView.getUsername(),
                detView.getMealTypeText(), null);
        System.out.println("Trying to save");
        // detView.stopTextAnim();
        detView.reset();
        setListScene();
    }

    private void handleDeleteButton(ActionEvent event) {
        model.dBRequest("DELETE", null, null, loginView.getUsername(), null, detView.getCurrTitle());
        // detView.stopTextAnim();
        model.shareRequest("DELETE", null, null, detView.getCurrTitle());
        detView.reset();
        setListScene();
    }

    private void handleRefreshButton(ActionEvent event) {
        final String text = this.whisper;
        this.detView.setRefreshText();
        Thread t = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        createRecipeAndImage(text);
                    }
                });

        t.start();
    }

    public void rememberMe() {
        if (loginView.rememberMeSeleced()) {
            try {
                FileWriter writer = new FileWriter(autoLogInfile);
                writer.write(loginView.getUsername() + "," + loginView.getPassword());
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void handleCreateButton(ActionEvent event) {
        String username = loginView.getUsername();
        String password = loginView.getPassword();
        String response = model.accountRequest("POST", username, password, null);

        switch (response) {
            case "Username taken":
                loginView.setMessageText("Sorry, this username is already taken!");
                break;
            case "Empty input":
                loginView.setMessageText("Please enter a username and password!");
                break;
            case "Added":
                rememberMe();
                setListScene();
                break;
            default:
                loginView.setMessageText(
                        "Looks like the pantry is locked from our end... \nPlease try again later!");
        }
    }

    public void deleteAutoLogin() {
        try {
            if (autoLogInfile.exists() && autoLogInfile.length() > 0) { // deletes from autoLogin
                FileWriter writer = new FileWriter(autoLogInfile);
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleLoginButton(ActionEvent event) {
        String username = loginView.getUsername();
        String password = loginView.getPassword();
        String response = model.accountRequest("PUT", username, password, null);

        System.out.println("Response:" + response);
        switch (response) {
            case "Empty input":
                loginView.setMessageText("Please enter a username and password!");
                break;
            case "Wrong info":
                loginView.setMessageText("Whoops, you've entered an incorrect username or password. Please try again!");
                loginView.resetFields();
                break;
            case "Login":
                rememberMe();
                setListScene();
                break;
            default:
                loginView.setMessageText(
                        "Looks like the pantry is locked from our end... \nPlease try again later!");
        }
    }

    private void handleLogOutButton(ActionEvent event) {
        deleteAutoLogin();
        loginView.resetFields();
        loginView.resetCheckbox();
        loginView.clearErrorMessage();
        setLoginScene();
    }

    private void handleSortSelection(ActionEvent event) {
        RecipeList recList = listView.getRecipeList();
        switch (listView.getSortValue()) {
            case "A to Z":
                sortingStrat = new SortButtonsAZ();
                sortingStrat.sort(recList);
                break;
            case "Z to A":
                sortingStrat = new SortButtonsZA();
                sortingStrat.sort(recList);
                break;
            case "Oldest First":
                filterSelection();
                sortingStrat = new SortButtonsOF();
                sortingStrat.sort(recList);
                break;
            default:
                filterSelection();
        }
    }

    public void handleShareButton(ActionEvent event) {
        detView.setLinkText("Sharing is caring. Please wait...");
        detView.disableBackButton(true);
        Thread t = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        String title = detView.getCurrTitle();
                        String detail = detView.getDetailText();
                        String response = model.shareRequest("POST", title, detail, null);
                        String trim_title = title.replaceAll("\\s", "");
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                detView.setLinkText("http://localhost:8100/share/?key=" + trim_title);
                                detView.disableBackButton(false);
                            }
                        });
                    }
                });

        t.start();
    }

    public void handleFilterSelection(ActionEvent event) {
        filterSelection();
    }

    public void filterSelection() {
        loadRecipeList();
        String selection = listView.getFilterValue();
        if (selection != "All") {
            RecipeList recipeList = listView.getRecipeList();

            List<RecipeTitle> filterRecipes = recipeList.getChildren().stream()
                    .filter(node -> node instanceof RecipeTitle)
                    .map(node -> (RecipeTitle) node)
                    .filter(RecipeTitle -> RecipeTitle.getMealType().equalsIgnoreCase(selection))
                    .collect(Collectors.toList());

            recipeList.getChildren().clear();
            recipeList.getChildren().addAll(filterRecipes);
        }
    }
}