package cse.project.team;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Collections;
import java.util.Comparator;

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

    File autoLogInfile = new File("autoLogIn.txt");

    final int HEIGHT = 750;
    final int WIDTH = 360;

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
        setLoginScene();

        this.detView.setBackButton(this::handleBackButton);
        this.detView.setSaveButton(this::handleSaveButton);
        this.detView.setDeleteButton(this::handleDeleteButton);
        this.detView.setRefreshButton(this::handleRefreshButton);

        this.listView.setRecipeButtons(this::handleRecipeButtons);
        this.listView.setGenerateButton(this::handleGenerateButton);
        this.listView.SetSortA_ZButton(this::handleSetSortA_ZButtonn);
        this.listView.SetSortZ_AButton(this::handleSetSortZ_AButtonn);
        this.listView.SetSortE_LButton(this::handleSetSortE_LButtonn);
        this.listView.SetSortL_EButton(this::handleSetSortL_EButtonn);
        this.listView.SetLogOutButton(this::handleLogOutButton);
        this.listView.setFilter(this::handleFilterSelection);

        this.genView.setBackButton(this::handleGenerateBackButton);
        this.genView.setStartButton(this::handleGenerateStartButton);

        this.loginView.setCreateButton(this::handleCreateButton);
        this.loginView.setLoginButton(this::handleLoginButton);
        this.loginView.setAutoButton(this::handleAutoButton);

        this.detView.setShareButton(this::handleShareButton);
    }

    private void loadRecipeList() {
        listView.getRecipeList().getChildren().clear();
        String[] rlist = model.dBRequest("GET", null, null, null, null, null).split("xF9j");

        for (String i : rlist) {
            if (i.length() == 0)
                continue;
            String[] info = i.split("yL8z42");

            if (info.length == 1)
                break;

            if (info[1].equals(loginView.getUsername())) {
                String[] recDets = model.dBRequest("GET", null, null, null, null, info[0]).split("xF9j");
                Recipe recipe = new Recipe(info[0], recDets[1]);
                listView.getRecipeList().getChildren().add(0, recipe);
            }
        }
        listView.setRecipeButtons(this::handleRecipeButtons);
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
        detView.getTitleTextArea().setEditable(false);
        stage.setScene(detailScene);
    }

    private void setLoginScene() {
        try {
            // Check if the file is empty
            if (autoLogInfile.length() > 1) {
                FileReader fileReader = new FileReader(autoLogInfile);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String line = bufferedReader.readLine();
                bufferedReader.close();
                System.out.println(line);
                String[] parts = line.split(",");
                String username = parts[0];
                String password = parts[1];
                loginView.setUsername(username);
                loginView.setPassword(password);
                String response = model.accountRequest("PUT", username, password, null);
                if (response.equals("Login")) {
                    setListScene();
                } else {
                    stage.setScene(loginScene);
                    loginView.setMessageText(
                            "Looks like the pantry is locked from our end... \nPlease try again later!");
                }
            } else {
                stage.setScene(loginScene);
            }
        } catch (IOException e) {
            stage.setScene(loginScene);
            loginView.setMessageText("Looks like the pantry is locked from our end... \nPlease try again later!");
        }

        // stage.setScene(loginScene);
    }

    private void handleRecipeButtons(MouseEvent event) {
        String recipeTitle = ((Recipe) event.getSource()).getTitle();
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
                                missingMealType();
                            } else if (wordCount == 1) {
                                missingingredient();
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

    private void missingMealType() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                genView.setRecordingLabel("Make sure you say breakfast, lunch, or dinner!");
                genView.showRecLabel();
                genView.enableBackButton();
                genView.enableStartButton();
            }
        });

    }

    private void missingingredient() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                genView.setRecordingLabel("Make sure you say ingredient");
                genView.showRecLabel();
                genView.enableBackButton();
                genView.enableStartButton();
            }
        });

    }

    // if more than one mealtype mentioned, returns in order of Brk, Lun, Din
    private String extractMealType(String audioText) {
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

    public void handleCreateButton(ActionEvent event) {
        String username = loginView.getUsername();
        String password = loginView.getPassword();
        String response = model.accountRequest("POST", username, password, null);

        switch (response) {
            case "Username taken":
                loginView.setMessageText("This username is already taken!");
                break;
            case "Empty input":
                loginView.setMessageText("Please enter a username and password!");
                break;
            case "Added":
                setListScene();
                break;
            default:
                loginView.setMessageText(
                        "Looks like the pantry is locked from our end... \nPlease try again later!");
        }
    }

    public void clearUsernamwPwdFields() {
        loginView.setUsername("");
        loginView.setPassword("");
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

        System.out.println(response);
        switch (response) {
            case "Empty input":
                loginView.setMessageText("Please enter a username and password!");
                break;
            case "Wrong info":
                loginView.setMessageText("Incorrect username or password. Please try again!");
                clearUsernamwPwdFields();
                deleteAutoLogin();
                break;
            case "Login":
                setListScene();
                break;
            default:
                loginView.setMessageText(
                        "Looks like the pantry is locked from our end... \nPlease try again later!");
        }
    }

    private void handleLogOutButton(ActionEvent event) {
        deleteAutoLogin();
        loginView.setUsername("");
        loginView.setPassword("");
        setLoginScene();
    }

    private void handleAutoButton(ActionEvent event) {
        try {
            FileWriter writer = new FileWriter(autoLogInfile);
            writer.write(loginView.getUsername() + "," + loginView.getPassword());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleSetSortA_ZButtonn(ActionEvent event) {
        sortingStrat = new SortButtonsAZ();
        RecipeList recList = listView.getRecipeList();
        sortingStrat.sort(recList);
    }

    private void handleSetSortZ_AButtonn(ActionEvent event) {
        sortingStrat = new SortButtonsZA();
        RecipeList recList = listView.getRecipeList();
        sortingStrat.sort(recList);
    }

    private void handleSetSortE_LButtonn(ActionEvent event) {
        listView.emptyList();
        loadRecipeList();

        sortingStrat = new SortButtonsEL();
        RecipeList recList = listView.getRecipeList();
        sortingStrat.sort(recList);
    }

    private void handleSetSortL_EButtonn(ActionEvent event) {
        listView.emptyList();
        loadRecipeList();
    }

    public void handleShareButton(ActionEvent event) {
        detView.setLinkText("Sharing is caring. Please wait....");
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
                            }
                        });
                    }
                });

        t.start();
    }

    public void handleFilterSelection(ActionEvent event) {
        loadRecipeList();
        String selection = listView.getFilterValue();
        if (selection != "All") {
            RecipeList recipeList = listView.getRecipeList();
            
            List<Recipe> filterRecipes = recipeList.getChildren().stream()
                    .filter(node -> node instanceof Recipe)
                    .map(node -> (Recipe) node)
                    .filter(Recipe -> Recipe.getMealType().equalsIgnoreCase(selection))
                    .collect(Collectors.toList());

            
            recipeList.getChildren().clear();
            recipeList.getChildren().addAll(filterRecipes);
        }
    }
}