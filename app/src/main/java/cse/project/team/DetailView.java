package cse.project.team;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.util.Duration;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.*;

public class DetailView extends BorderPane {
    private detailHeader header;
    private detailFooter footer;

    private Button back;
    private Button editButton;
    private Button saveButton;
    private Button deleteButton;

    private TextArea titleText, detailText, mealTypeText;
    private HBox title;

    private String currTitle;
    private Boolean newRec;
    private int currentIndex;
    private Timeline timeline;

    private ImageView recipeImage;

    public DetailView() {
        newRec = false;
        header = new detailHeader();
        footer = new detailFooter();

        back = new Button("Back");
        editButton = new Button("Edit Mode");
        saveButton = new Button("Save Recipe");
        deleteButton = new Button("Delete Recipe");

        back.getStyleClass().add("footerButton");
        saveButton.getStyleClass().add("footerButton");
        editButton.getStyleClass().add("footerButton");
        deleteButton.getStyleClass().add("footerButton");

        footer.add(editButton, 0, 0);
        footer.add(saveButton, 0, 1);
        footer.add(back, 1, 1);
        footer.add(deleteButton, 1, 0);

        titleText = new TextArea();
        titleText.setWrapText(true);
        titleText.setEditable(false);
        titleText.getStyleClass().addAll("textBox", "extraPadding");

        mealTypeText = new TextArea();
        mealTypeText.setEditable(false);
        mealTypeText.getStyleClass().addAll("textBox", "extraPadding");

        title = new HBox(0);
        title.getChildren().add(titleText);
        title.getChildren().add(mealTypeText);

        detailText = new TextArea();
        detailText.setWrapText(true);
        detailText.setEditable(false);
        detailText.getStyleClass().addAll("textBox", "largeBox");

        recipeImage = new ImageView();
        recipeImage.setFitWidth(128); 
        recipeImage.setPreserveRatio(true);

        VBox details = new VBox();
        details.getChildren().addAll(title, detailText, recipeImage);
        details.getStyleClass().add("center");

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
        return mealTypeText.getText();
    }

    public void addDetails(String title, String recipeDetails, String imagePath, String mealType) {
        this.currTitle = title;
        titleText.setText(title);
        mealTypeText.setText(mealType);
        mealTypeText.setStyle("-fx-background-color: " + selectColor(mealType));
        setAnimation(recipeDetails);

        // Load and set the image
        if (imagePath != null && !imagePath.isEmpty()) {
            File selectedFile = new File(imagePath);
            Image image = new Image(selectedFile.toURI().toString());
            recipeImage.setImage(image);
        }
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

    public void toggleEditMode() {
        if ("Edit Mode".equals(editButton.getText())) {
            setEditButtonTextToView();
            titleText.setEditable(true);
            detailText.setEditable(true);
        } else {
            setEditButtonTextToEdit();
            titleText.setEditable(false);
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

    public TextArea getTitleTextArea() {
        return titleText;
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

    public String selectColor(String mealType) {
        if (mealType.equals("Breakfast")) {
            return "blue";
        }
        else if (mealType.equals("Lunch")) {
            return "yellow";
        }
        else {
            return "red";
        }
    }

}

class detailFooter extends GridPane {
    detailFooter() {
        this.getStyleClass().add("footer");
    }
}

class detailHeader extends HBox {
    detailHeader() {
        Text titleText = new Text("Recipe deets");
        titleText.getStyleClass().add("titleText");

        this.getStyleClass().add("header");
        this.getChildren().add(titleText);
    }
}
