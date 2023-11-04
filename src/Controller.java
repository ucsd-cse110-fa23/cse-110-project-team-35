//package lea_src;

import java.util.ArrayList;
import java.util.List;

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
        this.listView.setBackButton(this::handleBackButton);

        createListScene();
        setListScene();

        this.listView.createRecipeButtons(recipeTitles);
        this.listView.setRecipeButtons(this::handleRecipeButtons);
    }

    private void createListScene() {
        listScene = new Scene(this.listView.getRecipeList(), 500, 600);
    }

    private void createDetailScene() {
        detailScene = new Scene(this.listView.getDetails(), 500, 600);
    }

    private void setListScene() {
        this.stage.setScene(listScene);
        recipeTitles = new ArrayList<>();
        recipeTitles.add("Borsofozelek");
        recipeTitles.add("Tejbegriz");
    }

    private void setDetailScene() {
        this.stage.setScene(detailScene);
    }

    private void handleRecipeButtons(ActionEvent event) {
        setDetailScene();
        String recipeTitle = ((Button) event.getSource()).getText();
        this.listView.addDetails(recipeTitle);
    }

    private void handleBackButton(ActionEvent event) {
        setListScene();
    }
}
