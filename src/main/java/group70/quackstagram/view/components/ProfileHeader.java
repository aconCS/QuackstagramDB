package group70.quackstagram.view.components;

import group70.quackstagram.Session;
import group70.quackstagram.controller.FollowController;
import group70.quackstagram.controller.NavigationController;
import group70.quackstagram.controller.UserController;
import group70.quackstagram.model.User;
import group70.quackstagram.model.UserProfileData;
import group70.quackstagram.services.FileServices;
import group70.quackstagram.view.coreUI.EditProfileUI;
import org.w3c.dom.ls.LSOutput;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.net.URL;

public class ProfileHeader extends JPanel {
    private static final int PROFILE_IMAGE_SIZE = 80;
    private JPanel statsPanel;
    private JPanel statsFollowPanel;
    private JTextArea profileBio;
    private JButton profileButton;

    private final UserController userController;
    private final FollowController followController;

    private final String username;
    private final String loggedInUsername = Session.getInstance().getCurrentUser().getUsername();

    public ProfileHeader(String username, UserController userController) {
        this.username = username;
        this.userController = userController;
        this.followController = new FollowController();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.GRAY);
        buildHeaderPanel();
    }

    public void refresh() {
        UserProfileData userProfileData = userController.getUserProfileData(username);


        statsPanel.removeAll();
        statsPanel.add(createStatLabel(Integer.toString(userProfileData.postCount()), "Posts"));
        statsPanel.add(createStatLabel(Integer.toString(userProfileData.followerCount()), "Followers"));
        statsPanel.add(createStatLabel(Integer.toString(userProfileData.followingCount()), "Following"));
        profileBio.setText(userProfileData.bio());
        statsPanel.revalidate();
        statsPanel.repaint();

        statsFollowPanel.remove(profileButton);
        profileButton = createProfileButton();
        statsFollowPanel.add(profileButton);
        statsFollowPanel.revalidate();
        statsFollowPanel.repaint();

        revalidate();
        repaint();
    }



    private void buildHeaderPanel() {
        UserProfileData userProfileData = userController.getUserProfileData(username);

        // Top Part of the Header (Profile Image, Stats, Follow Button)
        JPanel topHeaderPanel = new JPanel(new BorderLayout(10, 0));
        topHeaderPanel.setBackground(new Color(249, 249, 249));

        // Profile image
        String profilePicPath = userProfileData.profile_pic();
        ImageIcon profileIcon = FileServices.createScaledIcon(profilePicPath, PROFILE_IMAGE_SIZE, PROFILE_IMAGE_SIZE);

        JLabel profileImage = new JLabel(profileIcon);
        profileImage.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topHeaderPanel.add(profileImage, BorderLayout.WEST);

        // Stats Panel
        statsPanel = new JPanel();
        statsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        statsPanel.setBackground(new Color(249, 249, 249));
        statsPanel.add(createStatLabel(Integer.toString(userProfileData.postCount()), "Posts"));
        statsPanel.add(createStatLabel(Integer.toString(userProfileData.followerCount()), "Followers"));
        statsPanel.add(createStatLabel(Integer.toString(userProfileData.followingCount()), "Following"));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(25, 0, 10, 0));

        // Profile Button (Edit or Follow/Unfollow)
        profileButton = createProfileButton();

        // Combine stats and button
        statsFollowPanel = new JPanel();
        statsFollowPanel.setLayout(new BoxLayout(statsFollowPanel, BoxLayout.Y_AXIS));
        statsFollowPanel.add(statsPanel);
        statsFollowPanel.add(profileButton);

        topHeaderPanel.add(statsFollowPanel, BorderLayout.CENTER);
        add(topHeaderPanel);

        // Profile Name and Bio Panel
        JPanel profileNameAndBioPanel = new JPanel(new BorderLayout());
        profileNameAndBioPanel.setBackground(new Color(249, 249, 249));
        JLabel profileNameLabel = new JLabel(username);
        profileNameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        profileNameLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

        profileBio = new JTextArea(userProfileData.bio());
        profileBio.setEditable(false);
        profileBio.setFont(new Font("Arial", Font.PLAIN, 12));
        profileBio.setBackground(new Color(249, 249, 249));
        profileBio.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        profileNameAndBioPanel.add(profileNameLabel, BorderLayout.NORTH);
        profileNameAndBioPanel.add(profileBio, BorderLayout.CENTER);

        add(profileNameAndBioPanel);
    }

    private JLabel createStatLabel(String number, String text) {
        JLabel label = new JLabel("<html><div style='text-align: center;'>"
                + number + "<br/>" + text + "</div></html>", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setForeground(Color.BLACK);
        return label;
    }

    private JButton createProfileButton() {
        boolean isCurrentUser = loggedInUsername.equals(username);
        JButton profileButton = new JButton("null");
        if (!isCurrentUser) {
            boolean isFollowing = followController.isFollowing(username, loggedInUsername);
            System.out.println(isFollowing);
            String buttonText = isFollowing ? "Unfollow" : "Follow";
            profileButton.setText(buttonText);
            profileButton.addActionListener(e -> {
                if(isFollowing) {
                    followController.unfollow(loggedInUsername, username);
                    profileButton.setText("Follow");
                    refresh();
                } else {
                    followController.follow(loggedInUsername, username);
                    profileButton.setText("Unfollow");
                    refresh();
                }
            });
        }else{
            profileButton.setText("Edit Profile");
            profileButton.addActionListener(e -> {
                JFrame currFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
                NavigationController.getInstance().navigate(currFrame, new EditProfileUI(userController));
            });
        }

        profileButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        profileButton.setFont(new Font("Arial", Font.BOLD, 12));
        profileButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, profileButton.getMinimumSize().height)); // Make the button fill the horizontal space
        profileButton.setBackground(new Color(225, 228, 232)); // A soft, appealing color that complements the UI
        profileButton.setForeground(Color.BLACK);
        profileButton.setOpaque(true);
        profileButton.setBorderPainted(false);
        profileButton.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Add some vertical padding

        return profileButton;
    }
}