package cse.project.team;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;

public class DetailView extends BorderPane{
    private detailHeader header;
    private detailFooter footer;
    private ScrollPane scrollPane;
    private Button back;
    private Button editButton;
    private Button saveButton;
    private Button deleteButton;
    private Text titleText;
    private TextArea detailText;
    private String currTitle;
    private Boolean newRec;

    public DetailView() {
        newRec = false;
        header = new detailHeader();
        footer = new detailFooter();

        // Create the "back" button and add it to the footer
        back = new Button("Back");
        editButton = new Button("Enter Edit Mode");
        saveButton = new Button("Save Recipe");
        deleteButton = new Button("Delete Recipe");

        footer.getChildren().add(editButton);
        footer.getChildren().add(saveButton);
        footer.getChildren().add(back);
        footer.getChildren().add(deleteButton);

        scrollPane = new ScrollPane();
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        VBox details = new VBox();
        details.setStyle("-fx-background-color: yellow");
        titleText = new Text("Recipe title: ");
        detailText = new TextArea();
        detailText.setPrefRowCount(28);
        details.getChildren().addAll(titleText, detailText);
        detailText.setEditable(false);

        scrollPane.setContent(details);

        // Set the header at the top, the scroll pane in the center, and the footer at the bottom
        this.setTop(header);
        this.setCenter(scrollPane);
        this.setBottom(footer);

        // set button functionality
        editButton.setOnAction(event -> {
            toggleEditMode();
        });

    }

    public String getCurrTitle() {
        return currTitle;
    }

    public String getDetailText() {
        return detailText.getText();
    }

    public void addDetails(String title, String recipeDetails) {
        this.currTitle = title;
        titleText.setText("Recipe title: " + title);
        detailText.setText(recipeDetails);
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
        if ("Enter Edit Mode".equals(editButton.getText())) {
            editButton.setText("Enter View Mode");
            detailText.setEditable(true); // Enable editing in the TextArea
        } else {
            editButton.setText("Enter Edit Mode");
            detailText.setEditable(false); // Disable editing in the TextArea
        }
    }

    public Button getEditButton(){
        return editButton;
    }

    public TextArea getDetailTextArea() {
        return detailText;
    }

    public boolean getNewRec(){
        return newRec;
    }

    public void setNewRec(boolean val){
        newRec = val;
    }
    
}

class detailFooter extends HBox {
    detailFooter() {
        this.setPrefSize(500, 60);
        this.setStyle("-fx-background-color: white");
        this.setSpacing(15);
        this.setAlignment(Pos.CENTER);
    }
}

class detailHeader extends HBox {
    detailHeader() {
        this.setPrefSize(500, 60); // Size of the header
        this.setStyle("-fx-background-color: #F0F8FF;");

        Text titleText = new Text("Recipe Detail"); // Text of the Header
        titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
        this.getChildren().add(titleText);
        this.setAlignment(Pos.CENTER); // Align the text to the Center
    }
}
