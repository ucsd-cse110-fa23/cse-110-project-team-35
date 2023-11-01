
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.TextAlignment;
import javafx.geometry.Insets;
import javafx.scene.text.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

class Recipe extends HBox{
    private Label title;

    Recipe(String name){
        this.setPrefSize(500, 20); // sets size of task
        this.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0; -fx-font-weight: bold;"); // sets background color of task
        title = new Label();
        title.setText(name); // create index label
        title.setPrefSize(40, 20); // set size of Index label
        title.setTextAlignment(TextAlignment.CENTER); // Set alignment of index label
        title.setPadding(new Insets(10, 0, 10, 0)); // adds some padding to the task
        this.getChildren().add(title); // add index label to task
    }
}

class RecipeList extends VBox{
    RecipeList() {
        this.setSpacing(5); // sets spacing between tasks
        this.setPrefSize(500, 560);
        this.setStyle("-fx-background-color: #F0F8FF;");
    }
}

class Footer extends HBox {

    Footer() {
        this.setPrefSize(500, 60);
        this.setStyle("-fx-background-color: #F0F8FF;");
        this.setSpacing(15);
    }

}

class Header extends HBox {
    Header() {
        this.setPrefSize(500, 60); // Size of the header
        this.setStyle("-fx-background-color: #F0F8FF;");

        Text titleText = new Text("PantryPal"); // Text of the Header
        titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
        this.getChildren().add(titleText);
        this.setAlignment(Pos.CENTER); // Align the text to the Center
    }
}

class AppFrame extends BorderPane{
    private RecipeList rlist;
    private Header header;
    private Footer footer;
    private ScrollPane scrollPane;

    AppFrame(){
        header = new Header();
        footer = new Footer();

        Recipe r1 = new Recipe("Pizza");
        Recipe r2 = new Recipe("Pasta");
        rlist = new RecipeList();
        rlist.getChildren().add(r1);
        rlist.getChildren().add(r2);

        scrollPane = new ScrollPane(rlist);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        this.setCenter(rlist);
        this.setTop(header);
        this.setCenter(scrollPane);
        this.setBottom(footer);
    }
}

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Setting the Layout of the Window- Should contain a Header, Footer and the TaskList
        AppFrame root = new AppFrame();

        // Set the title of the app
        primaryStage.setTitle("PantryPal");
        // Create scene of mentioned size with the border pane
        primaryStage.setScene(new Scene(root, 500, 600));
        // Make window non-resizable
        primaryStage.setResizable(false);
        // Show the app
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}