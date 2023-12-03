package cse.project.team;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class Controller {
    private ListView listView;
    private DetailView detView;
    private GenerateView genView;
    private LoginView loginView;
    private Model model;
    private Stage stage;
    private Scene listScene, detailScene, generateScene, loginScene;

    final File STYLE = new File("style.css");
    final String STYLESHEET = "file:" + STYLE.getPath();

    File autoLogInfile = new File("autoLogIn.txt");

    final int HEIGHT = 650;
    final int WIDTH = 500;

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

        createDetailScene();
        createListScene();
        createGenerateScene();
        createLoginScene();
        setLoginScene();

        this.detView.setBackButton(this::handleBackButton);
        this.detView.setSaveButton(this::handleSaveButton);
        this.detView.setDeleteButton(this::handleDeleteButton);

        this.listView.setRecipeButtons(this::handleRecipeButtons);
        this.listView.setGenerateButton(this::handleGenerateButton);

        this.genView.setBackButton(this::handleGenerateBackButton);
        this.genView.setStartButton(this::handleGenerateStartButton);

        this.loginView.setCreateButton(this::handleCreateButton);
        this.loginView.setLoginButton(this::handleLoginButton);

        this.listView.SetLogOutButton(this::handleLogOutButton);

        this.loginView.setAutoButton(this::handleAutoButton);

        this.detView.setShareButton(this::handleShareButton);
    }

    private void loadRecipeList() {
        listView.getRecipeList().getChildren().clear();
        String[] rlist = model.dBRequest("GET", null, null, null, null).split("\\*");

        for (String i : rlist) {
            if (i.length() == 0)
                continue;
            String[] info = i.split("\\%");

            if (info.length == 1) {
                break;
            }

            if (info[1].equals(loginView.getUsername())) {
                Recipe recipe = new Recipe(info[0]);
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
        // listView.getRecipeList().getChildren().clear();
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
            if (autoLogInfile.length() != 0) {
                FileReader fileReader = new FileReader(autoLogInfile);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String line = bufferedReader.readLine();
                bufferedReader.close();
                System.out.println(line);
                String[] parts = line.split(",");
                loginView.setUsername(parts[0]);
                loginView.setPassword(parts[1]);
                setListScene();
            } else {
                stage.setScene(loginScene);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // stage.setScene(loginScene);
    }

    private void handleRecipeButtons(ActionEvent event) {
        String recipeTitle = ((Button) event.getSource()).getText();
        String details = model.dBRequest("GET", null, null, null, recipeTitle);
        String imagePath = new String(recipeTitle + ".jpg");

        model.generateImage(recipeTitle);
        detView.addDetails(recipeTitle, details.trim(), imagePath);
        setDetailScene();
    }

    private void handleBackButton(ActionEvent event) {
        detView.stopTextAnim();
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

                            // Prompt user to specify meal type if missing
                            if (audioTxt.equals("Error")) {
                                missingMealType();
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

    private void createRecipeAndImage(String audioTxt) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                // Generate recipe through ChatGPT
                String recipe = model.genRequest("GET", audioTxt);
                String[] recipeTitles = recipe.split("\n");

                // Generate image based on recipe title through DALL-E
                model.generateImage(recipeTitles[0]);

                // Save image on local computer
                String imagePath = new String(recipeTitles[0] + ".jpg");

                // Show image and recipe details
                detView.addDetails(recipe.split("\n")[0],
                        recipe.substring(recipe.split("\n")[0].length()).trim(),
                        imagePath);
                detView.disableButtons(false);

                setDetailScene();
                genView.reset();
            }
        });

    }

    private void handleGenerateButton(ActionEvent event) {
        setGenerateScene();
    }

    private void handleSaveButton(ActionEvent event) {
        model.dBRequest("PUT", detView.getCurrTitle(), detView.getDetailText(), loginView.getUsername(), null);
        detView.stopTextAnim();
        setListScene();
    }

    private void handleDeleteButton(ActionEvent event) {
        model.dBRequest("DELETE", null, null, loginView.getUsername(), detView.getCurrTitle());
        detView.stopTextAnim();
        setListScene();
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

    public void handleShareButton(ActionEvent event){
        String title = detView.getCurrTitle();
        String detail = detView.getDetailText();
        System.out.println("getText: "+detail);
        String response = model.shareRequest("POST", title, detail, null);
    }
}
