package cse.project.team.Views;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.*;

import cse.project.team.Views.Components.RecipeTitle;

public class DetailView extends BorderPane {
    private detailHeader header;
    private detailFooter footer;
    private Button back, editButton, saveButton,
            deleteButton, shareButton, refreshButton;

    private TextArea detailText, linkText;
    private VBox details;
    private String currTitle;
    private Boolean newRec;
    private int currentIndex;
    private Timeline timeline;
    private ImageView recipeImage;
    private RecipeTitle recipeTitle;
    private Region spacing;
    private Label titleText;

    public DetailView() {
        newRec = false;
        header = new detailHeader();
        footer = new detailFooter();

        back = new Button("← Back");
        editButton = new Button("Edit Mode");
        saveButton = new Button("Save");
        shareButton = new Button("Share");
        deleteButton = new Button("Delete");
        refreshButton = new Button("\t↻ Refresh this recipe!");

        back.getStyleClass().add("logoutButton");
        saveButton.getStyleClass().add("footerButton");
        editButton.getStyleClass().add("footerButton");
        shareButton.getStyleClass().add("footerButton");
        deleteButton.getStyleClass().add("footerButton");
        refreshButton.getStyleClass().addAll("logoutButton");

        HBox footerRow1 = new HBox(editButton, saveButton);
        footerRow1.setStyle("-fx-padding: 0 20 0 20; -fx-spacing: 8;");
        footer.getChildren().add(footerRow1);

        HBox footerRow2 = new HBox(deleteButton, shareButton);
        footerRow2.setStyle("-fx-padding: 0 20 0 20; -fx-spacing: 8;");
        footer.getChildren().add(footerRow2);

        HBox.setHgrow(spacing = new Region(), javafx.scene.layout.Priority.ALWAYS);
        footer.getChildren().add(new HBox(back, spacing, refreshButton));
        refreshButton.setVisible(false);

        recipeTitle = new RecipeTitle("", "");
        recipeTitle.setMinHeight(50);
        recipeTitle.setOnMouseEntered(e -> this.setStyle("-fx-cursor: default;"));

        detailText = new TextArea();
        detailText.setWrapText(true);
        detailText.setEditable(false);
        detailText.getStyleClass().addAll("textBox", "largeBox");

        linkText = new TextArea();
        linkText.setWrapText(true);
        linkText.setEditable(false);
        linkText.getStyleClass().add("textBox");
        linkText.setStyle("-fx-min-height: 50px; -fx-padding: 10 0 0;");
        linkText.setVisible(false);

        // Set the size of the ImageView
        recipeImage = new ImageView();
        recipeImage.setFitWidth(100);
        recipeImage.setFitHeight(100);
        recipeImage.setPreserveRatio(true);

        titleText = new Label("Recipe deets");
        titleText.getStyleClass().add("titleText");
        
        VBox.setVgrow(spacing = new Region(), javafx.scene.layout.Priority.ALWAYS);
        header.getChildren().addAll(new VBox(spacing, titleText), recipeImage);

        details = new VBox();
        details.getChildren().addAll(recipeTitle, detailText, linkText);
        details.getStyleClass().add("center");

        // Create a Circle to use as a mask
        Circle circle = new Circle();
        circle.setRadius(50); // Set the radius to half of the desired width/height
        circle.setCenterX(50); // Set the center X coordinate
        circle.setCenterY(50); // Set the center Y coordinate

        // Set the clip on the ImageView
        recipeImage.setClip(circle);

        this.setTop(header);
        this.setCenter(details);
        this.setBottom(footer);
        this.getStyleClass().add("BorderPane");

        editButton.setOnAction(event -> {
            toggleEditMode();
        });

    }

    public void setAnimation(String details) {
        detailText.clear();
        currentIndex = 0;
        timeline = new Timeline(
                new KeyFrame(Duration.seconds(0.01), event -> {
                    if (currentIndex < details.length()) {
                        detailText.appendText(String.valueOf(details.charAt(currentIndex)));
                        currentIndex++;
                    } else {
                        timeline.stop();
                    }
                }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public void stopTextAnim() {
        timeline.stop();
    }

    public String getCurrTitle() {
        return currTitle;
    }

    public String getDetailText() {
        return detailText.getText();
    }

    public String getMealTypeText() {
        return this.recipeTitle.getMealType();
    }

    public void addDetails(String title, String recipeDetails, String mealType) {
        this.currTitle = title;
        this.recipeTitle.setTitle(title);
        this.recipeTitle.setType(mealType);

        int indexOfBackslash = recipeDetails.indexOf('%');

        if (indexOfBackslash != -1) {
            String substringBeforeBackslash = recipeDetails.substring(0, indexOfBackslash);
            detailText.setText(substringBeforeBackslash);
            // setAnimation(substringBeforeBackslash);
        } else {
            // setAnimation(recipeDetails);
            detailText.setText(recipeDetails);
        }
    }

    public void setLinkText(String input) {
        linkText.setText(input);
        linkText.setVisible(true);
    }

    public void setImage(String imagePath) {
        // Load and set the image
        if (imagePath != null && !imagePath.isEmpty()) {
            File selectedFile = new File(imagePath);
            Image image = new Image(selectedFile.toURI().toString());
            recipeImage.setImage(image);
            showImage();
        }
    }

    public void resetLinkText() {
        linkText.setText("Sharing is caring. Please wait...");
        linkText.setVisible(false);
    }

    public void setBackButton(EventHandler<ActionEvent> eventHandler) {
        back.setOnAction(eventHandler);
    }

    public void setSaveButton(EventHandler<ActionEvent> eventHandler) {
        saveButton.setOnAction(eventHandler);
    }

    public void setDeleteButton(EventHandler<ActionEvent> eventHandler) {
        deleteButton.setOnAction(eventHandler);
    }

    public void setShareButton(EventHandler<ActionEvent> eventHandler) {
        shareButton.setOnAction(eventHandler);

    }

    public void setRefreshButton(EventHandler<ActionEvent> eventHandler) {
        refreshButton.setOnAction(eventHandler);
    }

    public void toggleEditMode() {
        if ("Edit Mode".equals(editButton.getText())) {
            setEditButtonTextToView();
            // titleText.setEditable(true);
            detailText.setEditable(true);
        } else {
            setEditButtonTextToEdit();
            // titleText.setEditable(false);
            detailText.setEditable(false);
        }
    }

    public void setEditButtonTextToEdit() {
        editButton.setText("Edit Mode");
    }

    public void setEditButtonTextToView() {
        editButton.setText("View Mode");
    }

    public Button getEditButton() {
        return editButton;
    }

    public TextArea getDetailTextArea() {
        return detailText;
    }

    public boolean getNewRec() {
        return newRec;
    }

    public void setNewRec(boolean val) {
        newRec = val;
    }

    public void disableButtons(Boolean value) {
        back.setDisable(value);
        editButton.setDisable(value);
        saveButton.setDisable(value);
        deleteButton.setDisable(value);
    }

    public void disableBackButton(Boolean value) {
        back.setDisable(value);
    }

    public void setRefreshText() {
        this.recipeTitle.setTitle("Cooking up Something new...");
        this.recipeTitle.setType("");
        this.detailText.setText("Talking to the chefGPT.  Please wait :)");
    }

    public void showRefreshButton() {
        this.refreshButton.setVisible(true);
    }

    public void hideRefreshButton() {
        this.refreshButton.setVisible(false);
    }

    public void hideImage() {
        recipeImage.setVisible(false);
    }

    public void showImage() {
        recipeImage.setVisible(true);
    }

    public void reset() {
        hideRefreshButton();
        hideImage();
        setLinkText("Press share to send to a friend.");
    }

}

class detailFooter extends VBox {
    detailFooter() {
        this.getStyleClass().add("footer");
        this.setStyle("-fx-padding: 30 8 0 8;");
    }
}

class detailHeader extends HBox {
    detailHeader() {
        this.getStyleClass().add("header");
        this.setStyle("-fx-padding: 20px 40px 0 30px;");
    }
}