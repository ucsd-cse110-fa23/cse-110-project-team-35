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

        createDetailScene();
        this.detView.setBackButton(this::handleBackButton);
        this.detView.setSaveButton(this::handleSaveButton);
        this.detView.setDeleteButton(this::handleDeleteButton);
        createListScene();
        setListScene();

        Recipe r1 = new Recipe("Pizza");
        Recipe r2 = new Recipe("Pasta");
        RecipeList recipeList = listView.getRecipeList();
        recipeList.getChildren().add(0, r1);
        recipeList.getChildren().add(0, r2);

        // this.listView.createRecipeButtons(recipeTitles);
        this.listView.setRecipeButtons(this::handleRecipeButtons);
        this.listView.setGenerateButton(this::handleGenerateButton);
        setListScene();
    }

    private void createListScene() {
        listScene = new Scene(listView, 500, 600);
    }

    private void createDetailScene() {
        detailScene = new Scene(this.detView, 500, 600);
    }

    private void setListScene() {
        stage.setScene(listScene);
    }

    private void setDetailScene() {
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
        Recipe recipe = new Recipe("yogurt");
        RecipeList recipeList = listView.getRecipeList();
        recipeList.getChildren().add(0, recipe);
        listView.setRecipeButtons(this::handleRecipeButtons);
    }

    private void handleSaveButton(ActionEvent event) {
        model.addData(detView.getCurrTitle(), detView.getDetailText());
        setListScene();
    }

    private void handleDeleteButton(ActionEvent event) {
        listView.removeRecipe(detView.getCurrTitle());
        model.deleteData(detView.getCurrTitle());
        setListScene();
    }
}
