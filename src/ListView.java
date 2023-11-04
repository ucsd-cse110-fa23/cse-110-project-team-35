import java.io.IOException;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;

public class ListView extends BorderPane {
    private RecipeList recipeList;
    private Header header;
    private Footer footer;
    private ScrollPane scrollPane;
    private Button generateButton;

    public ListView() {        
        header = new Header();
        footer = new Footer();
        recipeList = new RecipeList();
        
        scrollPane = new ScrollPane(recipeList);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        
        this.setCenter(recipeList);
        this.setTop(header);
        this.setCenter(scrollPane);
        this.setBottom(footer);
        
        generateButton = footer.getGenerateButton();
    }

    public RecipeList getRecipeList() {
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

    public void setGenerateButton(EventHandler<ActionEvent> eventHandler) {
        generateButton.setOnAction(eventHandler);
    }
}

class Recipe extends Button{
    Recipe(String name){
        this.setPrefSize(500, 20); // sets size of task
        this.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0; -fx-font-weight: bold; -fx-cursor: hand;");
        this.setText(name);
    }
}

class RecipeList extends VBox{
    RecipeList() {
        this.setSpacing(5); // sets spacing between tasks
        this.setPrefSize(500, 560);
        this.setStyle("-fx-background-color: #F0F8FF;");
    }
}

class Footer extends HBox {
    private Button generateButton;

    Footer() {
        this.setPrefSize(500, 60);
        this.setStyle("-fx-background-color: #F0F8FF;");
        this.setSpacing(15);
        generateButton = new Button("Generate Recipe");

        this.getChildren().addAll(generateButton);
        this.setAlignment(Pos.CENTER);
    }

    public Button getGenerateButton() {
        return generateButton;
    }

}

class Header extends HBox {
    Header() {
        this.setPrefSize(500, 60); // Size of the header
        this.setStyle("-fx-background-color: #F0F8FF;");

        Text titleText = new Text("PantryPal"); // Text of the Header
        titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
        this.getChildren().add(titleText);
        this.setAlignment(Pos.CENTER); // Align the text to the Center
    }
}