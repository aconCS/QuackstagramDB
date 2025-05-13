package group70.quackstagram.view.components;

import group70.quackstagram.Session;
import group70.quackstagram.controller.PostController;
import group70.quackstagram.model.Like;
import group70.quackstagram.model.Post;

import javax.swing.*;
import java.awt.*;

public class LikeButton extends JPanel{

    private static final Color  UNLIKED_BUTTON_COLOR = new Color(255, 153, 153, 255);
    private static final Color  LIKED_BUTTON_COLOR =  new Color(255, 90, 95);
    private final PostController postController;
    private final int postId;

    public LikeButton(int postId){
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setAlignmentX(Component.LEFT_ALIGNMENT);
        this.postController = new PostController();
        this.postId = postId;

        buildLikeButton();
    }

    public void refresh(){
        // Refresh the view
        removeAll();
        buildLikeButton();

        revalidate();
        repaint();
    }

    private void buildLikeButton() {
        Post post = postController.getPost(postId);
        JLabel likesLabel = new JLabel(post.getLikes() + " likes");
        likesLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        likesLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        JButton likeButton = new JButton("❤");
        likeButton.setHorizontalAlignment(SwingConstants.CENTER);
        likeButton.setBorderPainted(false); // Remove border
        likeButton.setOpaque(true);

        String loggedInUsername = Session.getInstance().getCurrentUser().getUsername();
        Like like = new Like(post.getPostId(), loggedInUsername);
        if(postController.isPostLiked(like)){
            likeButton.setBackground(LIKED_BUTTON_COLOR);
        }else{
            likeButton.setBackground(UNLIKED_BUTTON_COLOR);
        }

        likeButton.addActionListener(e -> {
            postController.likeAction(like);
            refresh(); // Refresh the view
        });

        add(likeButton);
        add(likesLabel);
    }
}
