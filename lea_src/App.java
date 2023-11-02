package lea_src;

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

        View view = new View();
        Model model = new Model();
        Controller controller = new Controller(view, model, primaryStage);

        //Scene scene = new Scene(view.recipeList(), 500, 600);
        //primaryStage.setScene(scene);
        primaryStage.setTitle("PantryPal");
        primaryStage.show();
    }
}