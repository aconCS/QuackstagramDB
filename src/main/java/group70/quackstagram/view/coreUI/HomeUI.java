package group70.quackstagram.view.coreUI;

import group70.quackstagram.Session;
import group70.quackstagram.controller.*;
import group70.quackstagram.model.Post;
import group70.quackstagram.model.User;
import group70.quackstagram.services.FileServices;
import group70.quackstagram.view.components.*;

import javax.swing.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.awt.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class HomeUI extends UIBase {
    private final int width = this.getWidth();

    private final int imageWidth = width - 100; // Width for the image posts
    private final int imageHeight = imageWidth; // Height for the image posts

    private final JPanel homePanel;
    private final UserController userController;
    private final PostController postController;

    public HomeUI() {
        setTitle("Quakstagram Home");

        userController = new UserController();
        postController = new PostController();
        homePanel = new JPanel(new BorderLayout());

        buildUI();

        add(homePanel, BorderLayout.CENTER);
        add(new HeaderPanel("Home"), BorderLayout.NORTH);
        add(new NavigationPanel(this), BorderLayout.SOUTH);
    }

    private void buildUI() {
        // Content Scroll Panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS)); // Vertical box layout
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER); // Never allow horizontal scrolling
        List<Post> filteredPosts = fetchPosts();
        populateContentPanel(contentPanel, filteredPosts);
        add(scrollPane, BorderLayout.CENTER);

        // Set up the home panel
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        homePanel.add(scrollPane, BorderLayout.CENTER);
    }

    private void populateContentPanel(JPanel panel, List<Post> filteredPosts) {

        for (Post post: filteredPosts) {
            JPanel itemPanel = new JPanel();
            itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.Y_AXIS));
            itemPanel.setBackground(Color.WHITE); // Set the background color for the item panel
            itemPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
            itemPanel.setAlignmentX(CENTER_ALIGNMENT);
            JLabel nameLabel = new JLabel(post.getOwner());
            nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel imageLabel = new JLabel();
            imageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            imageLabel.setPreferredSize(new Dimension(imageWidth, imageHeight));
            imageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Add border to image label
            ImageIcon imageIcon = FileServices.createScaledIcon(post.getPictureUrl(), imageWidth, imageHeight);
            if (imageIcon != null) {
                imageLabel.setIcon(imageIcon);
            }else{
                imageLabel.setText("Image not found");
            }

            JLabel descriptionLabel = new JLabel(post.getDescription());
            descriptionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JPanel likePanel = new LikeButton(post.getPostId());

            itemPanel.add(nameLabel);
            itemPanel.add(imageLabel);
            itemPanel.add(descriptionLabel);
            itemPanel.add(likePanel);

            panel.add(itemPanel);

            // Make the image clickable
            imageLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    JFrame currFrame = (JFrame) SwingUtilities.getWindowAncestor(imageLabel);
                    NavigationController.getInstance().navigate(currFrame, new PostUI(post)); // Call a method to switch to the image view
                }
            });

            // Grey spacing panel
            JPanel spacingPanel = new JPanel();
            spacingPanel.setPreferredSize(new Dimension(width - 10, 5)); // Set the height for spacing
            spacingPanel.setBackground(new Color(230, 230, 230)); // Grey color for spacing
            panel.add(spacingPanel);
        }
    }

    private List<Post> fetchPosts() {
        User loggedInUser = Session.getInstance().getCurrentUser();
        FollowController followController = new FollowController();
        List<User> followedUsers = followController.getFollowing(loggedInUser.getUsername());
        ArrayList<Post> filteredPosts = new ArrayList<>();

        for(User user: followedUsers) {
            List<Post> userPosts = postController.getFilteredPosts(user.getUsername(), true);
            filteredPosts.addAll(userPosts);
        }

        return filteredPosts;
    }



}