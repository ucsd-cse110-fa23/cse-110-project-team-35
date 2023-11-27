package cse.project.team;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import javafx.event.*;

class GenerateView extends BorderPane {
    private Button startButton;
    private Button backButton;
    private Label recordingLabel;
    private genFooter footer;
    private genHeader header;
    private VBox textBox;

    private int currentIndex;
    private Timeline timeline;

    GenerateView() {
        footer = new genFooter();
        header = new genHeader();
        textBox = new VBox();

        TextFlow promptFlow = new TextFlow();
        Text prompt = new Text("Press start! " +
                "For best results, list your prefered " +
                "meal type--breakfast, lunch, or dinner--" +
                "then state the ingredients you have available. " +
                "When you are finished recording, click stop! " +
                "Then just wait for the magic to brew.");
        prompt.setWrappingWidth(300);
        setAnimation(prompt, promptFlow);

        startButton = new Button("Start");
        backButton = new Button("Back");
        recordingLabel = new Label("Recording...");

        textBox.getStyleClass().add("center");
        promptFlow.getStyleClass().addAll("textBox", "largeBox");
        startButton.getStyleClass().add("footerButton");
        backButton.getStyleClass().add("footerButton");

        recordingLabel.getStyleClass().add("textBox");
        recordingLabel.setVisible(false);

        footer.getChildren().addAll(startButton, backButton);
        textBox.getChildren().addAll(promptFlow, recordingLabel);

        this.setCenter(textBox);
        this.setBottom(footer);
        this.setTop(header);
        this.getStyleClass().add("BorderPane");
    }

    public void setRecordingLabel(String input) {
        recordingLabel.setText(input);
    }

    public void setStartButton(EventHandler<ActionEvent> eventHandler) {
        startButton.setOnAction(eventHandler);
    }

    public void setBackButton(EventHandler<ActionEvent> eventHandler) {
        backButton.setOnAction(eventHandler);
    }

    public void toggleRecLabel() {
        if (recordingLabel.isVisible())
            recordingLabel.setVisible(false);
        else
            recordingLabel.setVisible(true);
    }

    public void startTextAnim() {
        timeline.play();
    }

    public void setAnimation(Text prompt, TextFlow promptFlow) {
        currentIndex = 0;
        timeline = new Timeline(
                new KeyFrame(Duration.seconds(5.0 / prompt.getText().length()), event -> {
                    if (currentIndex < prompt.getText().length()) {
                        char nextChar = prompt.getText().charAt(currentIndex);
                        Text nextText = new Text(String.valueOf(nextChar));
                        promptFlow.getChildren().add(nextText);
                        currentIndex++;
                    } else {
                        timeline.stop();
                    }
                }));

        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    public void reset() {
        recordingLabel.setVisible(false);
        startButton.setText("Start");
        enableBackButton();
    }

    public void disableBackButton() {
        backButton.setDisable(true);
    }

    public void enableBackButton() {
        backButton.setDisable(false);
    }
}

class genFooter extends HBox {
    genFooter() {
        this.getStyleClass().add("footer");
    }
}

class genHeader extends HBox {
    genHeader() {
        Text titleText = new Text("Generate a recipe!");
        titleText.getStyleClass().add("titleText");
        this.getStyleClass().add("header");
        this.getChildren().add(titleText);
    }
}