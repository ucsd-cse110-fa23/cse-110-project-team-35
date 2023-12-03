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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Collections;

public class ListView extends BorderPane {
    private RecipeList recipeList;
    private Header header;
    private Footer footer;
    private ScrollPane scrollPane;
    private Button generateButton;
    private Button logOutButton;
    private Button SortA_ZButton;
    private Button SortZ_AButton;
    private Button SortE_LButton;
    private Button SortL_EButton;

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
        SortA_ZButton = footer.getSortA_ZButton();
        SortZ_AButton = footer.getSortZ_AButton();
        SortE_LButton = footer.getSortE_LButton();
        SortL_EButton = footer.getSortL_EButton();
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


    public void sortButtonsAZ() {
        // Extract buttons and sort them
        List<Button> sortedButtons = recipeList.getChildren().stream()
            .filter(node -> node instanceof Button)
            .map(node -> (Button) node)
            .sorted(Comparator.comparing(Button::getText))
            .collect(Collectors.toList());

        // Clear the list and re-add sorted buttons
        recipeList.getChildren().clear();
        recipeList.getChildren().addAll(sortedButtons);
        
    }


    public void sortButtonsZA() {
        // Extract buttons and sort them in descending order
        List<Button> sortedButtons = recipeList.getChildren().stream()
                .filter(node -> node instanceof Button)
                .map(node -> (Button) node)
                .sorted(Comparator.comparing(Button::getText).reversed())
                .collect(Collectors.toList());
    
        // Clear the current children and add the sorted buttons back
        recipeList.getChildren().clear();
        recipeList.getChildren().addAll(sortedButtons);
    
    }

    public void sortButtonsEL() {
        List<Button> buttons = recipeList.getChildren().stream()
        .filter(node -> node instanceof Button)
        .map(node -> (Button) node)
        .collect(Collectors.toList());

        Collections.reverse(buttons);
        // Clear the list and re-add buttons in their original order
        recipeList.getChildren().clear();
        recipeList.getChildren().addAll(buttons);
                
    
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

    public void SetSortA_ZButton(EventHandler<ActionEvent> eventHandler) {
        SortA_ZButton.setOnAction(eventHandler);
    }

    public void SetSortZ_AButton(EventHandler<ActionEvent> eventHandler) {
        SortZ_AButton.setOnAction(eventHandler);
    }

    public void SetSortE_LButton(EventHandler<ActionEvent> eventHandler) {
        SortE_LButton.setOnAction(eventHandler);
    }

    public void SetSortL_EButton(EventHandler<ActionEvent> eventHandler) {
        SortL_EButton.setOnAction(eventHandler);
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
    private Button SortA_ZButton;
    private Button SortZ_AButton;
    private Button SortE_LButton;
    private Button SortL_EButton;


    Footer() {
        generateButton = new Button("Generate a Recipe!");
        generateButton.getStyleClass().add("footerButton");

        logOutButton = new Button("Log Out");
        logOutButton.getStyleClass().add("footerButton");

        SortA_ZButton = new Button("Sort A - Z");
        SortA_ZButton.getStyleClass().add("footerButton");

        SortZ_AButton = new Button("Sort Z - A");
        SortZ_AButton.getStyleClass().add("footerButton");

        SortE_LButton = new Button("Sort E - L");
        SortE_LButton.getStyleClass().add("footerButton");

        SortL_EButton = new Button("Sort L - E");
        SortL_EButton.getStyleClass().add("footerButton");




        HBox row1 = new HBox(generateButton, logOutButton);
        HBox row2 = new HBox(SortA_ZButton, SortZ_AButton);
        HBox row3 = new HBox(SortE_LButton, SortL_EButton);

        // Optionally, set spacing and alignment for HBoxes
        row1.setSpacing(10); // adjust spacing as needed
        row2.setSpacing(10); // adjust spacing as needed
        row3.setSpacing(10); // adjust spacing as needed

        // Create a VBox and add the two HBoxes to it
        VBox layout = new VBox(row1, row2, row3);
        layout.setSpacing(5); // adjust spacing between rows as needed

        // Add the VBox to the footer
        this.getChildren().add(layout);
        this.getStyleClass().add("footer");

    }

    public Button getGenerateButton() {
        return generateButton;
    }

    public Button getLogOutButton() {
        return logOutButton;
    }
    public Button getSortA_ZButton() {
        return SortA_ZButton;
    }

    public Button getSortZ_AButton() {
        return SortZ_AButton;
    }

    public Button getSortE_LButton() {
        return SortE_LButton;
    }

    public Button getSortL_EButton() {
        return SortL_EButton;
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
