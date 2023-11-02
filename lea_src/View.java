package lea_src;

import java.io.IOException;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;

public class View {
    //private Header header;
    //private Footer footer;
    //private ScrollPane scrollPane;
    //private Button generateButton;
    private VBox recipeList, details;
    private Button back;
    private Text t;

    public View() {
        recipeList = new VBox();
        details = new VBox();
        details.setStyle("-fx-background-color: yellow");
        t = new Text("Recipe title: ");
        back = new Button("Back");
        details.getChildren().addAll(t, back);
    }

    public VBox getRecipeList() {
        return recipeList;
    }

    public VBox getDetails() {
        return details;
    }

    public void createRecipeButtons(List<String> rlist) {
        for (String title : rlist){
            Button recipe = new Button(title);
            recipeList.getChildren().add(recipe);
        }
    }

    public void setRecipeButtons(EventHandler<ActionEvent> eventHandler) {
        for (int i = 0; i < recipeList.getChildren().size(); i++) {
            if (recipeList.getChildren().get(i) instanceof Button) {
            	((Button)recipeList.getChildren().get(i)).setOnAction(eventHandler);
            }
    	}
    }

    public void addDetails(String title) {
        t.setText("Recipe title: " + title);
    }

    public void setBackButton(EventHandler<ActionEvent> eventHandler) {
        back.setOnAction(eventHandler);
    }
}