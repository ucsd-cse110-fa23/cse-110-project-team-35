package cse.project.team;

import java.util.Random;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
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
        scrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);
        scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.getStyleClass().add("scrollPane");

        this.setCenter(recipeList);
        this.setTop(header);
        this.setCenter(scrollPane);
        this.setBottom(footer);

        generateButton = footer.getGenerateButton();
    }

    public RecipeList getRecipeList() {
        return recipeList;
    }

    public void setRecipeButtons(EventHandler<ActionEvent> eventHandler) {
        for (int i = 0; i < recipeList.getChildren().size(); i++) {
            if (recipeList.getChildren().get(i) instanceof Button) {
                ((Button) recipeList.getChildren().get(i)).setOnAction(eventHandler);
            }
        }
    }

    public void setGenerateButton(EventHandler<ActionEvent> eventHandler) {
        generateButton.setOnAction(eventHandler);
    }
}

class Recipe extends Button {
    Recipe(String name) {
        this.setText(name);
        this.getStyleClass().add("textBox");
        String[] colors = { "#F26B86", "#FFDFB6", "#05AEEF", "#0BBDA9", "#C1B7EE", "#89AFE8", "#F5EBC4" };
        int randomNumber = new Random().nextInt(7);
        this.setOnMouseEntered(e -> this.setStyle("-fx-background-color: " + colors[randomNumber]));
        this.setOnMouseExited(e -> this.setStyle("-fx-background-color: white;"));
    }
}

class RecipeList extends VBox {
    RecipeList() {
        this.getStyleClass().add("center");
    }
}

class Footer extends HBox {
    private Button generateButton;

    Footer() {
        generateButton = new Button("Generate a Recipe!");
        generateButton.getStyleClass().add("footerButton");
        this.getChildren().addAll(generateButton);
        this.getStyleClass().add("footer");
    }

    public Button getGenerateButton() {
        return generateButton;
    }

}

class Header extends HBox {
    Header() {
        Text titleText = new Text("PantryPal");
        Text smileyText = new Text(" ☺");
        Circle face = new Circle(10, Color.YELLOW);
        
        smileyText.setTranslateX(face.getCenterX() - 42);
        titleText.getStyleClass().add("titleText");
        smileyText.getStyleClass().add("smileyText");

        this.getStyleClass().add("header");
        this.getChildren().addAll(titleText, face, smileyText);
    }
}
