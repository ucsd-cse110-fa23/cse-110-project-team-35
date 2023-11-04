import java.io.IOException;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;

public class ListView {
    private VBox recipeList;

    public ListView() {
        recipeList = new VBox();
    }

    public VBox getRecipeList() {
        return recipeList;
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
}