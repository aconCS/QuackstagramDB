package group70.quackstagram.view.components;

import group70.quackstagram.Session;
import group70.quackstagram.controller.NavigationController;
import group70.quackstagram.controller.PostController;
import group70.quackstagram.model.Comment;
import group70.quackstagram.model.Post;
import group70.quackstagram.model.User;
import group70.quackstagram.view.coreUI.PostUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CommentButton extends JPanel {
    private static final Color COMMENT_BUTTON_COLOR = new Color(175, 248, 170, 255); // Color for the like button
    private final PostController postController;
    private final boolean navigateToPost;
    private final CommentPanel commentPanel;
    private final Post post;

    public CommentButton(Post post, CommentPanel commentPanel, boolean navigateToPost){
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setAlignmentX(Component.LEFT_ALIGNMENT);

        this.post = post;
        this.navigateToPost = navigateToPost;
        this.commentPanel = commentPanel;
        this.postController = new PostController();
        buildCommentButton();
    }

    private void refresh(){
        // Refresh the view
        removeAll();
        buildCommentButton();

        revalidate();
        repaint();
    }

    private void buildCommentButton() {
        JLabel commentLabel = new JLabel(postController.getComments(post.getPostId()).size() + " comments");
        commentLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        commentLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        JButton commentButton = new JButton("\uD83D\uDCAC");
        commentButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        commentButton.setBackground(COMMENT_BUTTON_COLOR); // Set the background color for the like button
        commentButton.setOpaque(true);
        commentButton.setBorderPainted(false);
        commentButton.addActionListener(e -> {
            if(navigateToPost){
                handlePostNavigation();
                handleCommentAction();
            }else{
                handleCommentAction();
            }
        });

        add(commentButton);
        add(commentLabel);
    }

    private void handlePostNavigation(){
        JFrame currFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        NavigationController.getInstance().navigate(currFrame, new PostUI(post));
    }

    private void handleCommentAction() {
        // Create a dialog for user to enter comment
        JDialog commentDialog = new JDialog((Frame) null, "Add CommentDAO", true);
        commentDialog.setLayout(new BorderLayout());
        commentDialog.setSize(300, 150);

        // Text field for entering comment
        JTextField commentField = new JTextField();
        commentDialog.add(commentField, BorderLayout.CENTER);

        // Submit button
        JButton submitButton = getSubmitButton(commentField, commentDialog);
        commentDialog.add(submitButton, BorderLayout.SOUTH);

        // Show the dialog
        commentDialog.setLocationRelativeTo(null);
        commentDialog.setVisible(true);
    }

    private JButton getSubmitButton(JTextField commentField, JDialog commentDialog) {
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String content = commentField.getText();
                if (!content.isEmpty()) {
                    User loggedInUser = Session.getInstance().getCurrentUser();
                    Comment comment = new Comment(post.getPostId(), loggedInUser.getUsername(), content);
                    postController.addComment(comment);
                    commentPanel.refreshCommentsPanel();
                    commentDialog.dispose();
                    refresh();

                }
            }
        });
        return submitButton;
    }
}
