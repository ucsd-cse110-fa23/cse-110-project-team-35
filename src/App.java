//package lea_src;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    
//    private Stage stage;
//    private Scene listScene, detailScene, generateScene;

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

        //Scene scene = new Scene(view.recipeList(), 500, 600);
        //primaryStage.setScene(scene);
        primaryStage.setTitle("PantryPal");
        primaryStage.show();
    }
}