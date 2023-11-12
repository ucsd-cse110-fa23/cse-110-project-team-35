package cse.project.team;

import java.util.List;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Controller {
    private ListView listView;
    private DetailView detView;
    private GenerateView genView;
    private Model model;
    private Stage stage;
    private Scene listScene, detailScene, generateScene;

    final int HEIGHT = 650;
    final int WIDTH = 360;

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
        createListScene();
        createGenerateScene();

        model.addData("Creamy Spinach with Chicken Dinner", "as;dlkjflk aklsdj f;lkasjdfija sdfjaisdfasdf");
        model.addData("Also a long dinner recipe with many words", "Stuff");
        model.addData("Milky chocolate-y breakfast recipe", "Stuff");

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
        List<String> rlist = model.getRecipeList();
        for (String i : rlist) {
            Recipe recipe = new Recipe(i);
            listView.getRecipeList().getChildren().add(0, recipe);
        }
        listView.setRecipeButtons(this::handleRecipeButtons);
    }

    private void createListScene() {
        loadrecipeList();
        listScene = new Scene(listView, WIDTH, HEIGHT);
        listScene.getStylesheets().add("file:app/src/main/java/cse/project/team/style.css");
    }

    private void createGenerateScene() {
        generateScene = new Scene(this.genView, WIDTH, HEIGHT);
        generateScene.getStylesheets().add("file:app/src/main/java/cse/project/team/style.css");
    }

    private void createDetailScene() {
        detailScene = new Scene(this.detView, WIDTH, HEIGHT);
        detailScene.getStylesheets().add("file:app/src/main/java/cse/project/team/style.css");
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
        detView.addDetails(recipeTitle, model.getDetails(recipeTitle));
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
            detView.addDetails("Magic Happening", "Generating your new recipe! Please wait...");
            setDetailScene();
            Thread t = new Thread(
                    new Runnable() {
                        @Override
                        public void run() {
                            String recipe = model.genRecipe();
                            detView.setNewRec(true);
                            String title = recipe.trim().split("\n")[0];
                            detView.addDetails(title, recipe.trim().substring(title.length()+2));
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
        if (detView.getNewRec()) {
            model.addData(detView.getCurrTitle(), detView.getDetailText());
            detView.setNewRec(false);
        } else
            model.putData(detView.getCurrTitle(), detView.getDetailText());
        setListScene();
    }

    private void handleDeleteButton(ActionEvent event) {
        model.deleteData(detView.getCurrTitle());
        setListScene();
    }

}
