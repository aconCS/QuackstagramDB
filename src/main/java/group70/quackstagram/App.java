package group70.quackstagram;

import com.formdev.flatlaf.FlatLightLaf;
import group70.quackstagram.view.authenticationUI.SignInUI;

import javax.swing.*;

public class App {
    public static void main(String[] args) {
        FlatLightLaf.setup();

        JFrame frame = new SignInUI();
        frame.setVisible(true);
    }
}