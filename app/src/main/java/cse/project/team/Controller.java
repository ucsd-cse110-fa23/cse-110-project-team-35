package cse.project.team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.Action;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Controller {
    private ListView listView;
    private DetailView detView;
    private GenerateView genView;
    private Model model;
    private Stage stage;
    private Scene listScene, detailScene;
    private List<String> recipeTitles;

    public Controller(ListView listView,
            DetailView detView,
            GenerateView genView,
            Model model,
            Stage stage) {

        this.listView = listView;
        this.detView = detView;
        this.genView = genView;
        this.model = model;
        this.stage = stage;

        model.addData("Pizza", "it's Pizza!");
        model.addData("Pasta", "Mama Mia!");

        createDetailScene();
        createListScene();
        setListScene();

        this.detView.setBackButton(this::handleBackButton);
        this.detView.setSaveButton(this::handleSaveButton);
        this.detView.setDeleteButton(this::handleDeleteButton);


        this.listView.setRecipeButtons(this::handleRecipeButtons);
        this.listView.setGenerateButton(this::handleGenerateButton);
    }

    private void loadrecipeList(){
        listView.getRecipeList().getChildren().clear();
        List<String> rlist = model.getRecipeList();
        for(String i: rlist){
            Recipe recipe = new Recipe(i);
            listView.getRecipeList().getChildren().add(0, recipe);
        }
        listView.setRecipeButtons(this::handleRecipeButtons);
    }

    private void createListScene() {
        loadrecipeList();
        listScene = new Scene(listView, 500, 600);
    }

    private void createDetailScene() {
        detailScene = new Scene(this.detView, 500, 600);
    }

    private void setListScene() {
        listView.getRecipeList().getChildren().clear();
        loadrecipeList();
        stage.setScene(listScene);
    }

    private void setDetailScene() {
        detView.getEditButton().setText("Enter Edit Mode");
        detView.getDetailTextArea().setEditable(false);
        stage.setScene(detailScene);
    }

    private void handleRecipeButtons(ActionEvent event) {
        setDetailScene();
        String recipeTitle = ((Button) event.getSource()).getText();
        detView.addDetails(recipeTitle, model.getDetails(recipeTitle));
    }

    private void handleBackButton(ActionEvent event) {
        setListScene();
    }

    private void handleGenerateButton(ActionEvent event) {
        Recipe recipe = new Recipe("Yogurt");
        RecipeList recipeList = listView.getRecipeList();
        recipeList.getChildren().add(0, recipe);
        listView.setRecipeButtons(this::handleRecipeButtons);
        model.addData("Yogurt", "Gurt");
    }

    private void handleSaveButton(ActionEvent event) {
        model.putData(detView.getCurrTitle(), detView.getDetailText());
        setListScene();
    }

    private void handleDeleteButton(ActionEvent event) {
        model.deleteData(detView.getCurrTitle());
        setListScene();
    }


}
