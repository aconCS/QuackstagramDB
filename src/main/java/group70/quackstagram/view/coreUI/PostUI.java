package group70.quackstagram.view.coreUI;

import group70.quackstagram.controller.UserController;
import group70.quackstagram.model.Post;
import group70.quackstagram.model.User;
import group70.quackstagram.services.FileServices;
import group70.quackstagram.view.components.*;

import javax.swing.*;
import java.awt.*;

public class PostUI extends UIBase {

    private final int width = this.getWidth();
    private final int imageDimension = width/2;

    private final Post post;
    private final CommentPanel commentPanel;
    private final UserController userController;

    public PostUI(Post post) {
        this.post = post;
        this.userController = new UserController();
        commentPanel = new CommentPanel(post);

        buildUI();
    }

    private void buildUI() {
        add(new HeaderPanel(post.getOwner() + "'s post"), BorderLayout.NORTH);
        add(createBodyPanel(), BorderLayout.CENTER);
        add(new NavigationPanel(this), BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    private JPanel createBodyPanel() {
        JPanel bodyPanel = new JPanel();
        bodyPanel.setLayout(new BoxLayout(bodyPanel, BoxLayout.Y_AXIS));

        bodyPanel.add(createImageLabel());
        bodyPanel.add(createInfoPanel());
        bodyPanel.add(commentPanel);

        return bodyPanel;
    }

    private JLabel createImageLabel(){
        // Crop the image to the fixed size
        JLabel imageLabel = new JLabel();
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        ImageIcon imageIcon = FileServices.createScaledIcon(post.getPictureUrl(), imageDimension, imageDimension);
        if (imageIcon != null) {
            imageLabel.setIcon(imageIcon);
        } else {
            imageLabel.setText("Image not found");
        }

        imageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        return imageLabel;
    }

    private JPanel createButtonsPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel likeButton = new LikeButton(post.getPostId());
        JPanel commentButton = new CommentButton(post, commentPanel, false);
        buttonPanel.add(likeButton);
        buttonPanel.add(commentButton);

        return buttonPanel;
    }

    private JPanel createInfoPanel() {
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        User postOwner = userController.getUser(post.getOwner());
        JPanel userNavPanel = new UserNavPanel(postOwner);
        infoPanel.add(userNavPanel); // User navigation panel

        JPanel captionWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel caption = new JLabel(post.getDescription());
        captionWrapper.add(caption);
        infoPanel.add(captionWrapper); // Caption

        JPanel buttonsPanel = createButtonsPanel();
        infoPanel.add(buttonsPanel); // Buttons panel

        return infoPanel;
    }
}
