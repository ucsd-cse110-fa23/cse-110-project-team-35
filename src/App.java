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

        Texts(){
            this.setPrefSize(300, 30);
            this.setStyle("-fx-background-color: #03D3FC; -fx-border-width: 0; -fx-font-weight: bold;"); 
            name = new TextField();
            name.setPrefSize(300, 20);
            name.setStyle("-fx-background-color: #03D3FC; -fx-border-width: 0;");
            index.setTextAlignment(TextAlignment.LEFT);
            name.setPadding(new Insets(10, 0, 10, 0));
            this.getChildren().add(name);

            number = new TextField();
            number.setPrefSize(200, 5);
            number.setStyle("-fx-background-color: #03D3FC; -fx-border-width: 0;");
            index.setTextAlignment(TextAlignment.LEFT);
            number.setPadding(new Insets(10, 0, 10, 0));
            this.getChildren().add(number);

            email = new TextField();
            email.setPrefSize(200, 5);
            email.setStyle("-fx-background-color: #03D3FC; -fx-border-width: 0;");
            index.setTextAlignment(TextAlignment.LEFT);
            email.setPadding(new Insets(10, 0, 10, 0));
            this.getChildren().add(email);
        }
    }

    Contact() {
        this.setPrefSize(500, 20); // sets size of contact
        this.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0; -fx-font-weight: bold;"); // sets background color of contact

        index = new Label();
        index.setText(""); // create index label
        index.setPrefSize(40, 20); // set size of Index label
        index.setTextAlignment(TextAlignment.CENTER); // Set alignment of index label
        index.setPadding(new Insets(10, 0, 10, 0)); // adds some padding to the contact
        this.getChildren().add(index); // add index label to contact

        contactImage = new Label();
        this.getChildren().add(contactImage);
        view = new ImageView(new Image("default-user-image.png"));
        view.setFitHeight(60);
        view.setPreserveRatio(true);
        contactImage.setGraphic(view);

        texts = new Texts();
        this.getChildren().add(texts);
        this.setAlignment(Pos.CENTER);
        this.setSpacing(5);

        editButton = new Button(" Edit\nImage");
        editButton.setPrefSize(100, 20);
        editButton.setPrefHeight(Double.MAX_VALUE);
        editButton.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 5;"); // sets style of button

        deleteButton = new Button("Delete");
        deleteButton.setPrefSize(100, 20);
        deleteButton.setPrefHeight(Double.MAX_VALUE);
        deleteButton.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 5;"); // sets style of button

        this.getChildren().add(editButton);
        this.getChildren().add(deleteButton);
    }

    public void setContactIndex(int num) {
        this.index.setText(" " + num + " "); // num to String
        this.texts.name.setPromptText("Name");
        this.texts.number.setPromptText("Phone #");
        this.texts.email.setPromptText("Email");
    }

    public TextField getName() {
        return this.texts.name;
    }

    public void setImageView(Image image) {
        this.image = image;
        this.view = new ImageView(image);
    }

    public Image getImage() {
        return this.image;
    }

    public ImageView getImageView() {
        return this.view;
    }

    public TextField getNumber() {
        return this.texts.number;
    }

    public TextField getEmail() {
        return this.texts.email;
    }

    public Button getEditButton() {
        return this.editButton;
    }

    public Button getDeleteButton() {
        return this.deleteButton;
    }

    public Label getContactImage() {
        return this.contactImage;
    }
}

class ContactList extends VBox {
    ContactList() {
        this.setSpacing(5); // sets spacing between contacts
        this.setPrefSize(500, 560);
        this.setStyle("-fx-background-color: #F0F8FF;");
    }

    public void updateContactIndices() {
        int index = 1;
        for (int i = 0; i < this.getChildren().size(); i++) {
            if (this.getChildren().get(i) instanceof Contact) {
                ((Contact) this.getChildren().get(i)).setContactIndex(index);
                index++;
            }
        }
    }

    public void deleteContact(Contact contact) {
        this.getChildren().remove(contact);
        this.updateContactIndices();
    }

    /*
     * Save contacts to a file called 'contacts.csv'
     */
    public void saveContacts() throws IOException {
        try {
            File file = new File("contacts.csv");
            FileWriter out = new FileWriter(file);

            out.write("name,phone number,email" + '\n');

            for (int i = 0; i < this.getChildren().size(); i++) {
                if (this.getChildren().get(i) instanceof Contact) {
                    out.write(((Contact) this.getChildren().get(i)).getName().getText() + ',');
                    out.write(((Contact) this.getChildren().get(i)).getNumber().getText() + ',');
                    if (i == this.getChildren().size() - 1) {
                        out.write(((Contact) this.getChildren().get(i)).getEmail().getText());
                    }
                    else {
                        out.write(((Contact) this.getChildren().get(i)).getEmail().getText() + '\n');
                    }
                }
            }

            out.close();
        }
        catch (Exception e) {
            System.out.println("Problem encountered while saving contacts");
        }
    }

    /* 
     * Sort the contacts lexicographically based on name field
     */
    public void sortContacts() {
        ArrayList<Contact> contacts = new ArrayList<Contact>();

        // deep copy each contact
        for (int i = 0; i < this.getChildren().size(); i++) {
            if (this.getChildren().get(i) instanceof Contact) {
                Contact contactCopy = new Contact();
                contactCopy.getName().setText(((Contact)this.getChildren().get(i)).getName().getText());
                contactCopy.getNumber().setText(((Contact)this.getChildren().get(i)).getNumber().getText());
                contactCopy.getEmail().setText(((Contact)this.getChildren().get(i)).getEmail().getText());
                contactCopy.setImageView(((Contact)this.getChildren().get(i)).getImage());
                contacts.add(contactCopy);
            }
        }

        contacts.sort((Contact a, Contact b)-> (a.getName().getText()).compareTo(b.getName().getText()));

        for (int i = 0; i < this.getChildren().size(); i++) {
            if (this.getChildren().get(i) instanceof Contact) {
                ((Contact) this.getChildren().get(i)).getName().setText(contacts.get(i).getName().getText());
                ((Contact) this.getChildren().get(i)).getNumber().setText(contacts.get(i).getNumber().getText());
                ((Contact) this.getChildren().get(i)).getEmail().setText(contacts.get(i).getEmail().getText());
                ((Contact) this.getChildren().get(i)).setImageView(contacts.get(i).getImage());
                ImageView imgView = ((Contact) this.getChildren().get(i)).getImageView();
                imgView.setFitHeight(60);
                imgView.setFitWidth(60);
                imgView.setPreserveRatio(true);
                ((Contact) this.getChildren().get(i)).getContactImage().setGraphic(imgView);
            }
        }
        
    }
}

class Header extends HBox {
    Header() {
        this.setPrefSize(500, 60); // Size of the header
        this.setStyle("-fx-background-color: #F0F8FF;");

        Text titleText = new Text("Contact List"); // Text of the Header
        titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
        this.getChildren().add(titleText);
        this.setAlignment(Pos.CENTER); // Align the text to the Center
    }
}

class Footer extends HBox {
    private Button addButton;
    private Button saveButton;
    private Button sortButton;

    Footer() {
        this.setPrefSize(500, 60);
        this.setStyle("-fx-background-color: #F0F8FF;");
        this.setSpacing(15);

        // set a default style for buttons - background color, font size, italics
        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";

        addButton = new Button("Add Contact"); // text displayed on add button
        addButton.setStyle(defaultButtonStyle); // styling the button

        saveButton = new Button("Save Contacts"); 
        saveButton.setStyle(defaultButtonStyle);

        sortButton = new Button("Sort Contacts (By Name)"); 
        sortButton.setStyle(defaultButtonStyle);

        this.getChildren().addAll(addButton, saveButton, sortButton); // adding buttons to footer
        this.setAlignment(Pos.CENTER); // aligning the buttons to center
    }

    public Button getAddButton() {
        return addButton;
    }

    public Button getSaveButton() {
        return saveButton;
    }

    public Button getSortButton() {
        return sortButton;
    }
}


class AppFrame extends BorderPane {
    private Header header;
    private Footer footer;
    private ContactList contactList;
    private ScrollPane scrollPane;
    private ImageUploaderApp imageUploader;

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

        // Initialise Button Variables through the getters in Footer
        addButton = footer.getAddButton();
        saveButton = footer.getSaveButton();
        sortButton = footer.getSortButton();

        // Call Event Listeners for the Buttons
        try {
            addListeners();
        }
        catch (IOException E) {
            return;
        }
    }

    public void addListeners() throws IOException {
        
        // Add button functionality
        addButton.setOnAction(e -> {
            // Create a new contact
            Contact contact = new Contact();
            // Add contact to contactlist
            contactList.getChildren().add(contact);

            Button editButton = contact.getEditButton();
            editButton.setOnAction(e1 -> {
                imageUploader = new ImageUploaderApp();
                Stage imageStage = new Stage();
                imageUploader.uploadImage(imageStage);
                Image image = imageUploader.getImage();
                if (image != null) {
                     contact.setImageView(image);
                     ImageView view = contact.getImageView();
                     view.setFitHeight(60);
                     view.setFitWidth(60);
                     view.setPreserveRatio(true);
                     contact.getContactImage().setGraphic(view);
                }
            });

            Button deleteButton = contact.getDeleteButton();
            deleteButton.setOnAction(e1 -> {
                contactList.deleteContact(contact);
            });

            // Update contact indices
            contactList.updateContactIndices();
        });

        saveButton.setOnAction(e -> {
            try {
                contactList.saveContacts();
            }
            catch (IOException E) {
                return;
            }
        });

        sortButton.setOnAction(e -> {
            contactList.sortContacts();
        });

    }
}

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        AppFrame root = new AppFrame();

        // Set the title of the app
        primaryStage.setTitle("Contact List");
        // Create scene of mentioned size with the border pane
        primaryStage.setScene(new Scene(root, 550, 600));
        // Make window non-resizable
        primaryStage.setResizable(false);
        // Show the app
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
