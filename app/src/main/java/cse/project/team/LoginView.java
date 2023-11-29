package cse.project.team;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.*;

public class LoginView extends BorderPane {
    private loginHeader login_header;
    private loginFooter login_footer;

    private Button loginButton;
    private Button createButton;
    private Button autoButton;

    private TextArea messageArea;
    private TextField username,password;
    public Object setLoginButton;

    public LoginView() {
        login_header = new loginHeader();
        login_footer = new loginFooter();

        loginButton = new Button("Log in");
        loginButton.getStyleClass().add("footerButton");
        this.getChildren().addAll(loginButton);

        createButton = new Button("Create account");
        createButton.getStyleClass().add("footerButton");
        this.getChildren().addAll(createButton);

        login_footer.add(createButton, 0, 0);
        login_footer.add(loginButton, 0, 1);

        messageArea = new TextArea();
        messageArea.setWrapText(true);
        messageArea.setEditable(false);
        messageArea.getStyleClass().addAll("textBox", "extraPadding");

        messageArea.setText("Welcome to PantryPal, or it is an error message");

        username = new TextField();
        username.getStyleClass().addAll("textBox");
        username.setPromptText("username:");
        password = new TextField();
        password.getStyleClass().addAll("textBox");
        password.setPromptText("password:");

        autoButton = new Button("remember me");
        autoButton.getStyleClass().add("footerButton");

        VBox content = new VBox();
        content.getChildren().addAll(messageArea,username,password,autoButton);
        content.getStyleClass().add("center");

        this.setTop(login_header);
        this.setCenter(content);
        this.setBottom(login_footer);
    }

    public void setCreateButton(EventHandler<ActionEvent> eventHandler) {
        createButton.setOnAction(eventHandler);
    }

    public void setLoginButton(EventHandler<ActionEvent> eventHandler) {
        loginButton.setOnAction(eventHandler);
    }

    public void setAutoButton(EventHandler<ActionEvent> eventHandler) {
        autoButton.setOnAction(eventHandler);
    }

    public void autoLoginButton(){
        loginButton.fire();
        
    }

    public Button getlogInButton(){
        return loginButton;
    }

    public String getUsername(){
        return username.getText();
    }

    public String getPassword(){
        return password.getText();
    }

    public String getMessageText() {
        return messageArea.getText();
    }

    public void setUsername(String user){
        username.setText(user);
    }

    public void setPassword(String pwd){
        password.setText(pwd);
    }

    public void setMessageText(String text) {
        messageArea.setText(text);
    }

}

class loginFooter extends GridPane {
    loginFooter() {
        this.getStyleClass().add("footer");  
    }
}

class loginHeader extends HBox {
    loginHeader() {
        Text titleText = new Text("PantryPal Login");
        Text smileyText = new Text(" ☺");
        Circle face = new Circle(10, Color.YELLOW);
        
        smileyText.setTranslateX(face.getCenterX() - 42);
        titleText.getStyleClass().add("titleText");
        smileyText.getStyleClass().add("smileyText");

        this.getStyleClass().add("header");
        this.getChildren().addAll(titleText, face, smileyText);
    }
}
