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

        // setListScene();
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
        try {
            dalle.generateDalle(recipeTitle);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String imagePath = new String(recipeTitle + ".jpg");
        detView.addDetails(recipeTitle, details.trim(), imagePath);
        setDetailScene();
    }

    private void handleBackButton(ActionEvent event) {
        detView.stopTextAnim();
        setListScene();
    }

    private void handleGenerateBackButton(ActionEvent event) {
        setListScene();
    }

    private void handleGenerateStartButton(ActionEvent event) {
        if (((Button) event.getSource()).getText().equals("Start")) {
            genView.disableBackButton();
            model.startRec();
            genView.toggleRecLabel();
            ((Button) event.getSource()).setText("Stop");
        } else {
            model.stopRec();
            detView.addDetails("Magic Happening", "Generating your new recipe! Please wait...", null);
            detView.disableButtons(true);
            Thread t = new Thread(
                    new Runnable() {
                        @Override
                        public void run() {
                            String audio = model.genRequest("POST", null);
                            System.out.println(audio);
                            String audioTxt = audio.toLowerCase();
                            if (audioTxt.contains("breakfast") || audioTxt.contains("lunch")
                                    || audioTxt.contains("dinner")) {
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        String recipe = model.genRequest("GET", audioTxt);
                                        String[] recipeTitles = recipe.split("\n");
                                        try {
                                            dalle.generateDalle(recipeTitles[0]);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        String imagePath = new String(recipeTitles[0] + ".jpg");
                                        detView.addDetails(recipe.split("\n")[0],
                                                recipe.substring(recipe.split("\n")[0].length()).trim(), imagePath);
                                        detView.disableButtons(false);
                                        setDetailScene();
                                    }
                                });

                            } else {
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        genView.setRecordingLabel("Breakfast received!");
                                        genView.toggleRecLabel();
                                    }
                                });
                            }
                        }
                    });

            t.start();
            genView.reset();
        }

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
        String password = model.accountRequest("GET", username, null, "checkpassword");
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
