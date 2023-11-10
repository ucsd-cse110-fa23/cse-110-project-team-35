package cse.project.team;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import java.io.*;
import java.net.URISyntaxException;

import javax.sound.sampled.*;
import javafx.event.*;

class GenerateView extends BorderPane{
    private Button startButton;
    private Button backButton;
    private Label recordingLabel;
    private genFooter footer;
    private genHeader header;
    private VBox textBox;
    
    // Set a default style for buttons and fields - background color, font size,
    // italics
    String defaultButtonStyle = "-fx-border-color: #000000; -fx-font: 13 arial; -fx-pref-width: 175px; -fx-pref-height: 50px;";
    String defaultLabelStyle = "-fx-font: 13 arial; -fx-pref-width: 175px; -fx-pref-height: 50px; -fx-text-fill: red; visibility: hidden";

    GenerateView() {
        footer = new genFooter();
        header = new genHeader();
        textBox = new VBox();

        textBox.getChildren().add(new Text("Press start!"));
        textBox.getChildren().add(new Text("Please say your prefered meal type (breakfast, lunch, dinner):"));
        textBox.getChildren().add(new Text("Then state your ingredients:"));
        textBox.getChildren().add(new Text("When you are finished recording, click stop"));

        // Add the buttons and text fields
        startButton = new Button("Start");
        startButton.setStyle(defaultButtonStyle);

        backButton = new Button("Back");
        backButton.setStyle(defaultButtonStyle);

        footer.getChildren().addAll(startButton, backButton);

        recordingLabel = new Label("Recording...");
        recordingLabel.setStyle(defaultLabelStyle);
        recordingLabel.setVisible(false);

        textBox.getChildren().add(recordingLabel);
        this.setCenter(textBox);
        this.setBottom(footer);
        this.setTop(header);
    }

    public void setStartButton(EventHandler<ActionEvent> eventHandler) {
        startButton.setOnAction(eventHandler);
    }

    public void setBackButton(EventHandler<ActionEvent> eventHandler) {
        backButton.setOnAction(eventHandler);
    }

    public void toggleRecLabel(){
        if(recordingLabel.isVisible())
            recordingLabel.setVisible(false);
        else 
            recordingLabel.setVisible(true);
    }

    public void reset() {
        recordingLabel.setVisible(false);
        startButton.setText("Start");
    }
    
    

}

class genFooter extends HBox {
    genFooter() {
        this.setPrefSize(500, 60);
        this.setStyle("-fx-background-color: white");
        this.setSpacing(15);
        this.setAlignment(Pos.CENTER);
    }
}

class genHeader extends HBox {
    genHeader() {
        this.setPrefSize(500, 60); // Size of the header
        this.setStyle("-fx-background-color: #F0F8FF;");

        Text titleText = new Text("Generate Your Recipe"); // Text of the Header
        titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
        this.getChildren().add(titleText);
        this.setAlignment(Pos.CENTER); // Align the text to the Center
    }
}