package group70.quackstagram.view.authenticationUI;

import group70.quackstagram.Session;
import group70.quackstagram.controller.NavigationController;
import group70.quackstagram.controller.UserController;
import group70.quackstagram.services.FileServices;
import group70.quackstagram.view.components.UIBase;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class SignUpUI extends UIBase {

    private final UserController userController;
    private final JTextField usernameField;
    private final JTextField passwordField;
    private final JTextField bioField;

    public SignUpUI() {
        setTitle( "Quackstagram - Register");

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        userController = new UserController();
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        bioField = new JTextField();

        new AuthUIBuilder(this)
                .addHeaderPanel("Register")
                .addTextFieldPanel("Username", usernameField)
                .addTextFieldPanel("Password", passwordField)
                .addTextFieldPanel("Bio", bioField)
                .addButton("Register", this::onRegisterClicked)
                .addButton("Login instead", this::switchButtonClicked)
                .buildUI();
    }

    /*
    * Event handler that triggers when the register button is clicked.
    * Checks if the entered owner already exists and saves credentials if it doesn't.
    * Navigates to the SignInUI.
    * */
    private void onRegisterClicked(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String bio = bioField.getText();

        String selectedPath = FileServices.fileChooser("Upload Profile Picture", "png", "jpg");

        if (selectedPath == null) {
            JOptionPane.showMessageDialog(this,
                    "Profile Picture is not selected",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        File selectedFile = new File(selectedPath);
        Path destPath;
        try {
            String fileExtension = FileServices.getFileExtension(selectedFile);
            String newFileName = username + "." + fileExtension;
            destPath = Paths.get("src/main/resources/img", "profile", newFileName);
            Files.copy(selectedFile.toPath(), destPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Username can't be empty",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        } else if (password.length() < 6) {
            JOptionPane.showMessageDialog(this,
                    "Password is too short",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        } else if (!userController.register(username, password, bio, destPath.toString())) {
            JOptionPane.showMessageDialog(this,
                    "Error registering user",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        NavigationController.getInstance().navigate(this, new SignInUI());
    }

    /*
    * Event handler that triggers when the login-instead button is clicked.
    * Navigates to the SignInUI.
    * */
    private void switchButtonClicked(ActionEvent event) {
        NavigationController.getInstance().navigate(this, new SignInUI());
    }

}
