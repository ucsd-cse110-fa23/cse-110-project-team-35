import java.io.IOException;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;

public class DetailView {
    private VBox details;
    private Button back;
    private Text t;

    public DetailView() {
        details = new VBox();
        details.setStyle("-fx-background-color: blue");
        t = new Text("Recipe title: ");
        back = new Button("Back");
        details.getChildren().addAll(t, back);
    }

    public VBox getDetails() {
        return details;
    }

    public void addDetails(String title) {
        t.setText("Recipe title: " + title);
    }

    public void setBackButton(EventHandler<ActionEvent> eventHandler) {
        back.setOnAction(eventHandler);
    }
}