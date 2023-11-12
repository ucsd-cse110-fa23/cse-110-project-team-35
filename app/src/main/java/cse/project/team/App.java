package cse.project.team;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        ListView listView = new ListView();
        DetailView detView = new DetailView();
        GenerateView genView = new GenerateView();
        Model model = new Model();
        Controller controller = new Controller(listView, detView, genView, model, primaryStage);

        primaryStage.setTitle("PantryPal");
        primaryStage.show();
    }
}
