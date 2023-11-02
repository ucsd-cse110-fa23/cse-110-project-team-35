package lea_src;

import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Controller {
    private View view;
    private Model model;
    private Stage stage;
    private Scene listScene, detailScene;
    private List<String> recipeTitles;

    public Controller(View view, Model model, Stage stage) {
        this.view = view;
        this.model = model;
        this.stage = stage;

        createDetailScene();
        this.view.setBackButton(this::handleBackButton);

        createListScene();
        setListScene();

        this.view.createRecipeButtons(recipeTitles);
        this.view.setRecipeButtons(this::handleRecipeButtons);
    }

    private void createListScene() {
        listScene = new Scene(this.view.getRecipeList(), 500, 600);
    }

    private void createDetailScene() {
        detailScene = new Scene(this.view.getDetails(), 500, 600);
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
        String recipeTitle = ((Button)event.getSource()).getText();
        this.view.addDetails(recipeTitle);
    }

    private void handleBackButton(ActionEvent event) {
        setListScene();
    }
}
