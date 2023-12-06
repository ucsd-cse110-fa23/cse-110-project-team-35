package cse.project.team.Views;

import cse.project.team.Views.Components.RecipeList;
import cse.project.team.Views.Components.RecipeTitle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.*;

public class ListView extends BorderPane {
    private RecipeList recipeList;
    private Header header;
    private Footer footer;
    private ScrollPane scrollPane;
    private Button generateButton, logOutButton;
    private ObservableList<String> filter, sort;
    private ComboBox<String> filterBox, sortBox;
    private Text filterText, sortText;
    private Region spacing;

    public ListView() {
        header = new Header();
        footer = new Footer();
        recipeList = new RecipeList();

        filter = FXCollections.observableArrayList(
                "Breakfast",
                "Lunch",
                "Dinner",
                "All");
        filterBox = new ComboBox<>(filter);
        filterBox.getSelectionModel().select(3);
        filterBox.getStyleClass().add("dropdown");

        sort = FXCollections.observableArrayList(
                "A to Z",
                "Z to A",
                "Oldest First",
                "Newest First");
        sortBox = new ComboBox<>(sort);
        sortBox.getSelectionModel().select(3);
        sortBox.getStyleClass().add("dropdown");

        scrollPane = new ScrollPane(recipeList);
        scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.getStyleClass().add("scrollPane");

        filterText = new Text("Filter: ");
        filterText.getStyleClass().add("dropdown");
        
        sortText = new Text("Sort: ");
        sortText.getStyleClass().add("dropdown");

        HBox filters = new HBox(new StackPane(filterText), filterBox, new StackPane(sortText), sortBox);
        filters.setAlignment(Pos.CENTER);
        filters.setSpacing(10);
        filters.getStyleClass().add("filterBg");
        filters.setMaxWidth(340);

        VBox.setVgrow(spacing = new Region(), javafx.scene.layout.Priority.ALWAYS);
        VBox center = new VBox(scrollPane, spacing, filters);
        center.setAlignment(Pos.CENTER);

        this.setTop(header);
        this.setCenter(center);
        this.setBottom(footer);
        this.getStyleClass().add("borderPane");

        generateButton = footer.getGenerateButton();
        logOutButton = footer.getLogOutButton();
    }

    public RecipeList getRecipeList() {
        return recipeList;
    }

    public void setRecipeButtons(EventHandler<MouseEvent> eventHandler) {
        for (int i = 0; i < recipeList.getChildren().size(); i++) {
            if (recipeList.getChildren().get(i) instanceof RecipeTitle) {
                recipeList.getChildren().get(i).setOnMouseClicked(eventHandler);
            }
        }
    }

    public void emptyList() {
        recipeList.getChildren().clear();
    }

    public void setGenerateButton(EventHandler<ActionEvent> eventHandler) {
        generateButton.setOnAction(eventHandler);
    }

    public void SetLogOutButton(EventHandler<ActionEvent> eventHandler) {
        logOutButton.setOnAction(eventHandler);
    }

    public String getFilterValue() {
        return filterBox.getValue();
    }

    public void setFilter(EventHandler<ActionEvent> eventHandler) {
        filterBox.setOnAction(eventHandler);
    }

    public String getSortValue() {
        return sortBox.getValue();
    }

    public void setSort(EventHandler<ActionEvent> eventHandler) {
        sortBox.setOnAction(eventHandler);
    }

}

class Footer extends VBox {
    private Button generateButton, logOutButton;
    private Region space, verticalSpace;

    Footer() {
        generateButton = new Button("Generate a Recipe!");
        generateButton.getStyleClass().add("footerButton");

        logOutButton = new Button("Log Out");
        logOutButton.getStyleClass().add("logoutButton");
        HBox.setHgrow(space = new Region(), javafx.scene.layout.Priority.ALWAYS);
        
        verticalSpace = new Region();
        verticalSpace.setPrefHeight(8);

        this.getChildren().addAll(generateButton, new HBox(space, logOutButton));
        this.getStyleClass().add("footer");
        this.setStyle("-fx-padding: 35 8 8 30");
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
