import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

class Contact extends HBox {
    private Label index;
    private Label contactImage;
    private ImageView view;
    private Image image = null;
    private Texts texts;
    private Button editButton;
    private Button deleteButton;

    class Texts extends VBox {
        private TextField name;
        private TextField number;
        private TextField email;
}

class ContactList extends VBox {

}

class Header extends HBox {
}

class Footer extends HBox {
    private Button addButton;
    private Button saveButton;
    private Button sortButton;
}


class AppFrame extends BorderPane {
    private Header header;
    private Footer footer;
    private ContactList contactList;
    private ScrollPane scrollPane;
    //private ImageUploaderApp imageUploader;

    private Button addButton;
    private Button saveButton;
    private Button sortButton;

    AppFrame() {
        // Initialise the header Object
        header = new Header();

        // Create a ContactList Object to hold the contacts
        contactList = new ContactList();
        
        // Initialise the Footer Object
        footer = new Footer();

        scrollPane = new ScrollPane(contactList);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        // Add header to the top of the BorderPane
        this.setTop(header);
        // Add scroller to the centre of the BorderPane
        this.setCenter(scrollPane);
        // Add footer to the bottom of the BorderPane
        this.setBottom(footer);

    }

    }
}

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

    }
    public static void main(String[] args) {
        launch(args);
    }
}
