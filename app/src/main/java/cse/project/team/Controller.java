package cse.project.team;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;

public class Controller {
    private ListView listView;
    private DetailView detView;
    private GenerateView genView;
    private LoginView loginView;
    private Model model;
    private Stage stage;
    private Scene listScene, detailScene, generateScene, loginScene;

    private Dalle dalle;

    final File STYLE = new File("style.css");
    final String STYLESHEET = "file:" + STYLE.getPath();

    final int HEIGHT = 650;
    final int WIDTH = 360;

    public Controller(ListView listView,
            DetailView detView,
            GenerateView genView,
            LoginView loginView,
            Model model,
            Stage stage,
            Dalle dalle) {

        this.listView = listView;
        this.detView = detView;
        this.genView = genView;
        this.loginView = loginView;
        this.model = model;
        this.stage = stage;
        this.dalle = dalle;

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
    }

    private void loadrecipeList() {
        listView.getRecipeList().getChildren().clear();
        String[] rlist = model.dBRequest("GET", null, null, null).split("\\*");
        for (String i : rlist) {
            if (i.length() == 0)
                continue;
            Recipe recipe = new Recipe(i);
            listView.getRecipeList().getChildren().add(0, recipe);
        }
        listView.setRecipeButtons(this::handleRecipeButtons);
    }

    private void createListScene() {
        loadrecipeList();
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
        listView.getRecipeList().getChildren().clear();
        loadrecipeList();
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
        stage.setScene(loginScene);
    }

    private void handleRecipeButtons(ActionEvent event) {
        String recipeTitle = ((Button) event.getSource()).getText();
        String details = model.dBRequest("GET", null, null, recipeTitle);
        String imagePath = new String(recipeTitle + ".jpg");

        dalle.generateDalle(recipeTitle);
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

        // Change UI to start recording
        genView.disableBackButton();
        genView.showRecLabel();
        ((Button) event.getSource()).setText("Stop");
    }

    private void stopRecording(ActionEvent event) {
        // Stop recording
        model.stopRec();

        // Change UI to stop recording
        ((Button) event.getSource()).setText("Start");
        genView.setRecordingLabel("Generating your new recipe! Please wait...");
    }

    private void missingMealType() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                genView.setRecordingLabel("Make sure you say breakfast, lunch, or dinner!");
                genView.showRecLabel();
                genView.enableBackButton();
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

                // Generate image based on that recipe through DALL-E
                dalle.generateDalle(recipeTitles[0]);

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
        model.dBRequest("PUT", detView.getCurrTitle(), detView.getDetailText(), null);
        detView.stopTextAnim();
        setListScene();
    }

    private void handleDeleteButton(ActionEvent event) {
        model.dBRequest("DELETE", null, null, detView.getCurrTitle());
        detView.stopTextAnim();
        setListScene();
    }

    private void handleCreateButton(ActionEvent event) {
        String username = loginView.getUsername();
        String new_password = loginView.getPassword();
        String password = model.accountRequest("GET", username, null, username);
        if (password.equals("Does not exist")){
            String put_message = model.accountRequest("PUT", username, new_password, null);
            setListScene();
        }
    }

    private void handleLoginButton(ActionEvent event) {
        String username = loginView.getUsername();
        String new_password = loginView.getPassword();
        String password = model.accountRequest("GET", username, null, username);
        System.out.println(password);
        if (password.equals("Does not exist")){
            System.out.println("account not exist");
        }else if(password.equals(new_password)){
            setListScene();
        }else{
            System.out.println("password not correct");
        }
    }

    private void handleLogOutButton(ActionEvent event){
        setLoginScene();
     }
}
