package cse.project.team;

import cse.project.team.Controller.Controller;
import cse.project.team.Model.Model;
import cse.project.team.views.DetailView;
import cse.project.team.views.GenerateView;
import cse.project.team.views.ListView;
import cse.project.team.views.LoginView;
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
        LoginView loginView = new LoginView();
        Model model = new Model();

        Controller controller = new Controller(listView, detView, genView,loginView, model, primaryStage);

        primaryStage.setTitle("PantryPal");
        primaryStage.show();
    }
}
