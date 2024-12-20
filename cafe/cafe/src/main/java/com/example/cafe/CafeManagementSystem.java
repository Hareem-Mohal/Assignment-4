package com.example.cafe;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;

public class CafeManagementSystem extends Application {
    public static int counter=0;

    ArrayList<UserDetails> user=new ArrayList<>();
    public static final String USER_FILE = "User.txt";


    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {

        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        // Background layer
        StackPane background = new StackPane();
        background.setId("background");

        background.setEffect(new GaussianBlur(10)); // Apply blur to background only

        // Foreground content
        Text txt = new Text("Welcome to the Cafe Management System");
        txt.getStyleClass().add("Text");

        Button loginBtn = new Button("Login");
        loginBtn.getStyleClass().add("button1");

        Button signupBtn = new Button("Signup");
        signupBtn.getStyleClass().add("button1");

        VBox vbox2 = new VBox(25,txt,loginBtn,signupBtn);
        vbox2.setId("vbox2");


        VBox root = new VBox(10, vbox2);
        root.setId("root");

        // Combine background and foreground
        StackPane mainPane = new StackPane();
        mainPane.getChildren().addAll(background, root);

        // Scene setup
        Scene scene = new Scene(mainPane);
        var stylesheet = getClass().getResource("/com/example/cafe/style.css");
        if (stylesheet != null) {
            scene.getStylesheets().add(stylesheet.toExternalForm());
        }
        signupBtn.setOnAction(e->{
            signupScreen(stage);
        });
        loginBtn.setOnAction(e->{
            loginScreen(stage);

        });

        // Stage setup
        stage.setScene(scene);
        stage.setWidth(screenBounds.getWidth());
        stage.setHeight(screenBounds.getHeight());
        stage.setTitle("Cafe Management System");
        stage.show();
    }
    public void loginScreen(Stage stage) {
//

        // Background Pane
        StackPane picPane = new StackPane();
        picPane.setId("loginBackground");
        picPane.setEffect(new GaussianBlur(10));

        // Login Form
        Label l1 = new Label("Username");
        l1.setId("usernameLabel");
        TextField txtForLoginuser = new TextField();
        txtForLoginuser.setPromptText("Enter Username"); // Placeholder text
        txtForLoginuser.setId("username");

        Label l2 = new Label("Password");
        l2.setId("passwordLabel");
        PasswordField passwordForLoginUser = new PasswordField();
        passwordForLoginUser.setPromptText("Enter Password");
        passwordForLoginUser.setId("password");

        Button loginbtnForMain= new Button("Login");
        loginbtnForMain.getStyleClass().add("button1");
        loginbtnForMain.setOnAction(e->{
            int index1=0;
            String Username = txtForLoginuser.getText();
            String Password=passwordForLoginUser.getText();
            try {
                loadUsersFromFile();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            for(int i=0;i<user.size();i++){
                if (user.get(i).username().equals(Username) && user.get(i).password().equals(Password)){
                     index1=i;
                }
            }
            for (int i=0;i<user.size();i++) {
                if (user.get(index1).username().equals(Username) && user.get(index1).password().equals(Password)) {
                    Menu m1 = new Menu();
                    m1.menu(stage,user.get(index1).username());
                    break;

                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Wrong Username or Password");
                    alert.showAndWait();
                    break;

                }
            }
        });

        VBox v1 = new VBox(15); // Spacing between elements
        v1.getChildren().addAll(l1, txtForLoginuser, l2, passwordForLoginUser, loginbtnForMain);
        v1.setId("vbox3");
        v1.setAlignment(Pos.CENTER); // Center the login form vertically

        // Forgot Password Link
        Hyperlink hyperlink = new Hyperlink("Forgot Password?");
        hyperlink.setId("hyperlink");
        hyperlink.setOnAction(e->{
            forgetPassword(stage);
        });

        // Back Button
        Button backButtononLogin = new Button();
        backButtononLogin.setId("backButton");
        backButtononLogin.setOnAction(e->{
            try {
                start(stage);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        // Main Container for `#pane3`
        VBox paneContent = new VBox(10); // Vertical spacing
        paneContent.getChildren().addAll(v1, hyperlink);
        paneContent.setAlignment(Pos.CENTER);
        VBox.setMargin(hyperlink, new Insets(20, 0, 0, 0)); // Add margin above the hyperlink

        Pane p3 = new Pane();
        p3.setId("pane3");
        p3.getChildren().add(paneContent);

        // Align `paneContent` to the center of `#pane3`
        StackPane.setAlignment(paneContent, Pos.CENTER);

        // Align `btn2` to the top left
        StackPane.setAlignment(backButtononLogin, Pos.TOP_LEFT);

        // Combine all panes
        StackPane mainPane = new StackPane();
        mainPane.getChildren().addAll(picPane, p3, backButtononLogin);

        // Scene setup
        Scene scene = new Scene(mainPane, 1300, 1000);
        var stylesheet = getClass().getResource("/com/example/cafe/style.css");
        if (stylesheet != null) {
            scene.getStylesheets().add(stylesheet.toExternalForm());
        }
        stage.setScene(scene);
    }

public void signupScreen(Stage stage) {
    StackPane signupImg = new StackPane();
    signupImg.setId("signImage");
    signupImg.setEffect(new GaussianBlur(10));

    Label l1 = new Label("Username");
    l1.getStyleClass().add("textDesign");
    TextField txtFornewUser = new TextField();
    txtFornewUser.setPromptText("Enter Username");
    txtFornewUser.getStyleClass().add("fieldDesign");

    Label l2 = new Label("Email");
    l2.getStyleClass().add("textDesign");
    TextField txtFornewEmail = new TextField();
    txtFornewEmail.setPromptText("Enter Email");
    txtFornewEmail.getStyleClass().add("fieldDesign");

    Label l3 = new Label("Create Password");
    l3.getStyleClass().add("textDesign");
    PasswordField passwordForNewSign = new PasswordField();
    passwordForNewSign.setPromptText("Enter Password");
    passwordForNewSign.getStyleClass().add("fieldDesign");

    Label l4 = new Label("Confirm Password");
    l4.getStyleClass().add("textDesign");
    PasswordField passwordCForNewSign = new PasswordField();
    passwordCForNewSign .setPromptText("Enter Confirm Password");
    passwordCForNewSign.getStyleClass().add("fieldDesign");

    Button btn = new Button("Signup");
    btn.getStyleClass().add("button1");
    btn.setOnAction(e -> {
        String username = txtFornewUser.getText();
        String email = txtFornewEmail.getText();
        String password1 = passwordForNewSign.getText();
        String confirmPassword = passwordCForNewSign.getText();

        if (password1.equals(confirmPassword)) {
            user.add(new UserDetails(email, password1, username));
            try {
                inputuser();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("User successfully created");
                alert.showAndWait();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } else {
           Alert alert = new Alert(Alert.AlertType.ERROR);
           alert.setTitle("Error");
           alert.setHeaderText(null);
           alert.setContentText("Passwords do not match");
           alert.showAndWait();

        }
    });

    Button btn2 = new Button("Back to Login");
    btn2.getStyleClass().add("button1");
    btn2.setOnAction(e -> {
        loginScreen(stage);
    });

    VBox v2 = new VBox(10);
    v2.getChildren().addAll(l1, txtFornewUser, l2, txtFornewEmail, l3, passwordForNewSign, l4, passwordCForNewSign, btn, btn2);
    v2.setId("vbox4");
    v2.setAlignment(Pos.CENTER);

    VBox.setMargin(btn, new Insets(10, 0, 0, 0));
    VBox.setMargin(btn2, new Insets(5, 0, 0, 0));

    StackPane mainPane1 = new StackPane();
    mainPane1.getChildren().addAll(signupImg, v2);
    Scene scene = new Scene(mainPane1);

    var stylesheet = getClass().getResource("/com/example/cafe/style.css");
    if (stylesheet != null) {
        scene.getStylesheets().add(stylesheet.toExternalForm());
    }
    stage.setScene(scene);
    stage.show();
}
public void forgetPassword(Stage stage) {
        Stage p1=new Stage();
        StackPane forgetImg=new StackPane();
        forgetImg.setId("forgetImg");
        forgetImg.setEffect(new GaussianBlur(10));
        Label l3=new Label("Email");
        l3.getStyleClass().add("textDesign");
        TextField txtToMatchEmail = new TextField();
    txtToMatchEmail.setPromptText("Enter your Email");
    txtToMatchEmail.getStyleClass().add("fieldDesign");
        Label l1 = new Label("Enter New Password");
        l1.getStyleClass().add("textDesign");
        PasswordField newPassword = new PasswordField();
    newPassword.setPromptText("Enter New Password");
    newPassword.getStyleClass().add("fieldDesign");
        Label l2 = new Label("Confirm Password");
        l2.getStyleClass().add("textDesign");
        PasswordField confirmPassword = new PasswordField();
         confirmPassword.setPromptText("Enter Confirm Password");
    confirmPassword.getStyleClass().add("fieldDesign");
        Button savePasswordBtn = new Button("Save Password");
    savePasswordBtn.getStyleClass().add("button1");
    savePasswordBtn.setOnAction(e->{
            try {
                loadUsersFromFile();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            String Email = txtToMatchEmail.getText();
            String Password = newPassword.getText();
            String ConfirmPassword = confirmPassword.getText();
            for(UserDetails u1:user){
                if(u1.Email().equals(Email)){
                    try {
                        updatepassword(Email,Password);
                        break;
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }else{
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Invalid Email");
                    errorAlert.setHeaderText("Error Alert");
                    errorAlert.setContentText("Email entered is invalid");
                    errorAlert.showAndWait();
                    break;
                }
            }

            p1.close();
        });
        VBox v3 = new VBox(10);
        v3.getChildren().addAll(l3,txtToMatchEmail,l1,newPassword,l2,confirmPassword,savePasswordBtn);
        v3.setId("vbox4");
        v3.setAlignment(Pos.CENTER);
        StackPane mainPane = new StackPane();
        mainPane.getChildren().addAll(forgetImg, v3);
        Scene scene2 = new Scene(mainPane,800,600);
    var stylesheet = getClass().getResource("/com/example/cafe/style.css");
    if (stylesheet != null) {
        scene2.getStylesheets().add(stylesheet.toExternalForm());
    }

        p1.setScene(scene2);
        p1.setTitle("Forget Password Form");
        p1.show();

}
public void inputuser() throws IOException {

        try {
            BufferedWriter r1=new BufferedWriter(new FileWriter(USER_FILE,true));


                r1.write(user.get(counter).Email() + ";" + user.get(counter).password() + ";" + user.get(counter).username()+"\n");
                counter++;
r1.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public void login(Stage stage) throws IOException {



    }
    public void updatepassword( String Email,String password1) throws IOException {

        for (int i = 0; i < user.size(); i++) {
            if (user.get(i).Email().equals(Email)) {
                user.get(i).setPassword(password1);
                break;
            }
        }
        BufferedWriter w1=new BufferedWriter(new FileWriter(USER_FILE,false));
        for(UserDetails u1:user){
            w1.write(u1.Email() + ";" + u1.password() + ";" + u1.username()+"\n");
        }

w1.close();
        Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
        errorAlert.setTitle("Password Updated");
        errorAlert.setHeaderText("password updated");
        errorAlert.setContentText("password updated Successfully");
        errorAlert.showAndWait();
    }
    public void loadUsersFromFile() throws IOException {
        user.clear(); // Clear the list before loading
        BufferedReader reader = new BufferedReader(new FileReader(USER_FILE));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(";");
            if (parts.length == 3) {
                user.add(new UserDetails(parts[0], parts[1], parts[2])); // Email, Password, Username
            }
        }
        reader.close();
    }




}







