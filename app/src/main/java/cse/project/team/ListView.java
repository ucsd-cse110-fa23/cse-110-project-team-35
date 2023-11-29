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
    private Button logOutButton;

    public ListView() {
        header = new Header();
        footer = new Footer();
        recipeList = new RecipeList();

        scrollPane = new ScrollPane(recipeList);
        scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("scrollPane");

        this.setCenter(recipeList);
        this.setTop(header);
        this.setCenter(scrollPane);
        this.setBottom(footer);
        this.getStyleClass().add("BorderPane");

        generateButton = footer.getGenerateButton();
        logOutButton = footer.getLogOutButton();
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

    public void SetLogOutButton(EventHandler<ActionEvent> eventHandler) {
        logOutButton.setOnAction(eventHandler);
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
        this.getStyleClass().add("listCenter");
    }
}

class Footer extends HBox {
    private Button generateButton;
    private Button logOutButton;


    Footer() {
        generateButton = new Button("Generate a Recipe!");
        generateButton.getStyleClass().add("footerButton");

        logOutButton = new Button("Log Out");
        logOutButton.getStyleClass().add("footerButton");

        this.getChildren().addAll(generateButton, logOutButton);
        this.getStyleClass().add("footer");
    }

    public Button getGenerateButton() {
        return generateButton;
    }

    public Button getLogOutButton() {
        return logOutButton;
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
