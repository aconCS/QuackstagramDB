package group70.quackstagram.view.components;

import group70.quackstagram.controller.NavigationController;
import group70.quackstagram.controller.UserController;
import group70.quackstagram.model.User;
import group70.quackstagram.model.UserProfileData;
import group70.quackstagram.view.coreUI.ProfileUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class UserNavPanel extends JPanel {

    private final UserController userController;
    private final User user;

    public UserNavPanel(User user) {
        this.userController = new UserController();
        this.user = user;
        setLayout(new FlowLayout(FlowLayout.LEFT));

        buildUI();
    }

    private void buildUI() {
        UserProfileData userProfileData = userController.getUserProfileData(user.getUsername());
        ImageIcon profileIcon = new ImageIcon(userProfileData.profile_pic());
        profileIcon.setImage(profileIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
        JLabel scaledIcon = new JLabel(profileIcon);
        add(scaledIcon); // Add profile icon

        JLabel userName = new JLabel(user.getUsername());
        userName.setFont(new Font("Arial", Font.BOLD, 18));
        add(userName); // Add usernameLabel

        // Make the image clickable
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFrame currFrame = (JFrame) SwingUtilities.getWindowAncestor(UserNavPanel.this);
                NavigationController.getInstance().navigate(currFrame, new ProfileUI(user));
            }
        });

    }
}
