package group70.quackstagram.view.components;

import group70.quackstagram.controller.PostController;
import group70.quackstagram.controller.UserController;
import group70.quackstagram.model.Comment;
import group70.quackstagram.model.Post;
import group70.quackstagram.model.User;
import group70.quackstagram.model.UserProfileData;
import group70.quackstagram.services.FileServices;
import group70.quackstagram.utils.TimeStampFormatter;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class CommentPanel extends JPanel{

    private final Post post;
    private final UserController userController;
    private final PostController postController;

    public CommentPanel(Post post) {
        this.post = post;
        this.userController = new UserController();
        this.postController = new PostController();
        setLayout(new BorderLayout());
        buildCommentsPanel();
    }

    public void refreshCommentsPanel(){
        removeAll();
        buildCommentsPanel();
        revalidate();
        repaint();
    }

    private void buildCommentsPanel(){
        JPanel commentsPanel = new JPanel();
        commentsPanel.setLayout(new BoxLayout(commentsPanel, BoxLayout.Y_AXIS));

        // Read comments from the file
        java.util.List<Comment> comments = postController.getComments(post.getPostId());

        for(Comment comment: comments){
            // Individual comment Panel
            JPanel commentPanel = new JPanel();
            commentPanel.setLayout(new BorderLayout(5,5));
            commentPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            commentPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

            UserProfileData commenterData = userController.getUserProfileData(post.getOwner());
            ImageIcon profileIcon = new ImageIcon(commenterData.profile_pic());
            profileIcon.setImage(profileIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
            JLabel scaledIcon = new JLabel(profileIcon);

            String timestampMessage = TimeStampFormatter.getElapsedTimestamp(comment.getCommentDate());

            JLabel commentLabel = new JLabel(comment.getOwner() + ": " + comment.getContent());
            JLabel timeStampLabel = new JLabel(timestampMessage);
            timeStampLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
            timeStampLabel.setFont(new Font("Arial", Font.ITALIC, 12));
            timeStampLabel.setForeground(Color.GRAY);

            commentPanel.add(scaledIcon, BorderLayout.WEST);
            commentPanel.add(commentLabel, BorderLayout.CENTER);
            commentPanel.add(timeStampLabel, BorderLayout.EAST);
            commentsPanel.add(commentPanel);
        }

        JScrollPane scrollPane = new JScrollPane(commentsPanel);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(1000, 150)); // Set preferred size for the scroll pane

        add(scrollPane, BorderLayout.CENTER);
    }
}
