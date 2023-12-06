package cse.project.team.Views;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.*;

public class LoginView extends BorderPane {
    private LoginHeader header;
    private LoginFooter footer;

    private Button loginButton;
    private Button createButton;
    private CheckBox checkbox;

    private TextField username;
    private PasswordField password;

    private Text login;
    private Label errorMsg;
    private Region space;

    public LoginView() {
        header = new LoginHeader();
        footer = new LoginFooter();

        login = new Text("Welcome!");
        login.getStyleClass().add("loginText");

        addLoginButtons();
        addErrorMessage();
        addLoginFields();

        VBox content = new VBox();

        // Add space so that "Login" and "Create Account" buttons are at the bottom of rectangle
        VBox.setVgrow(space = new Region(), javafx.scene.layout.Priority.ALWAYS);        
        BorderPane.setMargin(content, new javafx.geometry.Insets(20));
        
        content.setFillWidth(true);
        content.getChildren().addAll(login, username, password, checkbox, errorMsg, space, loginButton, createButton);
        content.getStyleClass().addAll("footer", "loginCenter");
        
        this.getStyleClass().add("borderPane");
        this.setTop(header);
        this.setCenter(content);
        this.setBottom(footer);
    }

    public void addLoginFields() {
        username = new TextField();
        password = new PasswordField();
        checkbox = new CheckBox("Remember my info please :)");
        
        username.getStyleClass().addAll("textBox");
        password.getStyleClass().addAll("textBox");
        checkbox.getStyleClass().add("checkbox");
        
        username.setPromptText("Your username");
        password.setPromptText("Your password");
    }

    public void addLoginButtons() {
        loginButton = new Button("Log in");
        createButton = new Button("Create account");
        
        loginButton.getStyleClass().add("footerButton");
        createButton.getStyleClass().add("footerButton");
    }

    public void addErrorMessage() {
        errorMsg = new Label();
        errorMsg.setVisible(false);
        errorMsg.setFont(new Font(13));
        errorMsg.setTextFill(Color.MAROON);
        errorMsg.setWrapText(true);
        errorMsg.setTextAlignment(TextAlignment.CENTER);
    }

    public void setCreateButton(EventHandler<ActionEvent> eventHandler) {
        createButton.setOnAction(eventHandler);
    }

    public void setLoginButton(EventHandler<ActionEvent> eventHandler) {
        loginButton.setOnAction(eventHandler);
    }

    public boolean rememberMeSeleced() {
        return checkbox.isSelected();
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

    public void resetFields(){
        username.clear();
        password.clear();
    }

    public void resetCheckbox(){
        checkbox.setSelected(false);
    }

    public void setMessageText(String text) {
        errorMsg.setText(text);
        errorMsg.setVisible(true);
    }

    public void clearErrorMessage() {
        errorMsg.setVisible(false);
    }

    public void setUsername(String user) {
        username.setText(user);
    }

    public void setPassword(String user) {
        password.setText(user);
    }
}

class LoginFooter extends VBox {
    LoginFooter() {
        this.getStyleClass().add("center"); 
        this.setPrefHeight(120);
    }
}

class LoginHeader extends HBox {
    LoginHeader() {
        Text titleText = new Text("PantryPal");
        Text smileyText = new Text(" ☺");
        Circle face = new Circle(10, Color.YELLOW);
        
        smileyText.setTranslateX(face.getCenterX() - 42);
        titleText.getStyleClass().add("titleText");
        smileyText.getStyleClass().add("smileyText");

        this.getStyleClass().add("header");
        this.setStyle("-fx-padding: 60px 40px 30px 30px;");
        this.getChildren().addAll(titleText, face, smileyText);
    }
}
