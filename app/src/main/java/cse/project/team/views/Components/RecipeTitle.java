package cse.project.team.Views.Components;

import cse.project.team.Model.Components.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

public class RecipeTitle extends HBox {
    private Label title, type;
    private StackPane mealType;
    private Region spaceFiller;
    private ColorPicker colorPicker = new ColorPicker();

    public RecipeTitle(String name, String mt) {
        title = new Label(name);
        type = new Label(mt);

        mealType = new StackPane(type);
        mealType.getStyleClass().add("mealType");
        mealType.setStyle("-fx-font-size: 10px; -fx-background-color: " + colorPicker.tag(mt));

        HBox.setHgrow(spaceFiller = new Region(), javafx.scene.layout.Priority.ALWAYS);

        this.setOnMouseEntered(e -> this.setStyle("-fx-background-color: " + colorPicker.highlight()));
        this.setOnMouseExited(e -> this.setStyle("-fx-background-color: white;"));
        this.getStyleClass().add("textBox");
        this.setMaxSize(320, 50);
        this.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
        this.setSpacing(4);
        this.getChildren().addAll(title, spaceFiller, mealType);
    }

    public String getTitle() {
        return title.getText();
    }

    public String getMealType() {
        return type.getText();
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    public void setType(String type) {
        this.type.setText(type);
    }
}
