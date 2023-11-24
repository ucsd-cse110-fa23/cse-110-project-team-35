package cse.project.team;

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
    private Model model;
    private Stage stage;
    private Scene listScene, detailScene, generateScene;

    private Dalle dalle;
    
    final File STYLE = new File("style.css");
    final String STYLESHEET = "file:" + STYLE.getPath();

    final int HEIGHT = 650;
    final int WIDTH = 360;

    public Controller(ListView listView,
            DetailView detView,
            GenerateView genView,
            Model model,
            Stage stage,
            Dalle dalle) {

        this.listView = listView;
        this.detView = detView;
        this.genView = genView;
        this.model = model;
        this.stage = stage;
        this.dalle = dalle;

        createDetailScene();
        createListScene();
        createGenerateScene();

        setListScene();

        this.detView.setBackButton(this::handleBackButton);
        this.detView.setSaveButton(this::handleSaveButton);
        this.detView.setDeleteButton(this::handleDeleteButton);

        this.listView.setRecipeButtons(this::handleRecipeButtons);
        this.listView.setGenerateButton(this::handleGenerateButton);

        this.genView.setBackButton(this::handleGenerateBackButton);        
        this.genView.setStartButton(this::handleGenerateStartButton);
    }

    private void loadrecipeList() {
        listView.getRecipeList().getChildren().clear();
        String[] rlist = model.performRequest("GET",null,null,null).split("\\*");
        for (String i : rlist) {
            if(i.length() == 0)
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

    private void handleRecipeButtons(ActionEvent event) {
        String recipeTitle = ((Button) event.getSource()).getText();
        String details = model.performRequest("GET", null, null, recipeTitle);
        try {
            dalle.generateDalle(recipeTitle);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String imagePath = new String(recipeTitle + ".jpg");
        detView.addDetails(recipeTitle, details.trim(),imagePath);
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
            detView.addDetails("Magic Happening", "Generating your new recipe! Please wait...",null);
            detView.disableButtons(true);
            setDetailScene();
            Thread t = new Thread(
                    new Runnable() {
                        @Override
                        public void run() {
                            String recipe = model.performRequest("GET", null, null,"Team35110");
                            String[] recipeTitles = recipe.split("\n");
                            try {
                                dalle.generateDalle(recipeTitles[0]);
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            String imagePath = new String(recipeTitles[0]+".jpg");
                            detView.addDetails(recipe.split("\n")[0], recipe.substring(recipe.split("\n")[0].length()).trim(),imagePath);
                            detView.disableButtons(false);
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
        model.performRequest("PUT", detView.getCurrTitle(), detView.getDetailText(), null);
        detView.stopTextAnim();
        setListScene();
    }

    private void handleDeleteButton(ActionEvent event) {
        model.performRequest("DELETE",null,null,detView.getCurrTitle());
        detView.stopTextAnim();
        setListScene();
    }
}
