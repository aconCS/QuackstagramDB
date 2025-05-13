package group70.quackstagram.services;

import group70.quackstagram.controller.UserController;
import group70.quackstagram.dao.CommentDAO;
import group70.quackstagram.dao.LikeDAO;
import group70.quackstagram.dao.PostDAO;
import group70.quackstagram.model.Comment;
import group70.quackstagram.model.Like;
import group70.quackstagram.model.Post;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PostServices {

    private final PostDAO postDao;
    private final CommentDAO commentDao;
    private final LikeDAO likeDao;

    public PostServices() {
        this.likeDao = new LikeDAO();
        this.postDao = new PostDAO();
        this.commentDao = new CommentDAO();
    }

    public void createNewPost(Post post) {
        try {
            postDao.createPost(post);
        } catch (SQLException e) {
            throw new RuntimeException("Couldn't create post: ", e);
        }
    }

    public int getPostCount(String username) {
        try {
            return postDao.countPostsByUser(username);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to count posts", e);
        }
    }

    public List<Post> getFilteredPosts(String filter, boolean exactMatch) {
        try {
            return postDao.getFilteredPosts(filter, exactMatch);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to filter posts", e);
        }
    }

    public void addComment(Comment comment) {
        try{
            commentDao.insertComment(comment);
        }catch (SQLException e){
            throw new RuntimeException("Failed to add comment", e);
        }
    }

    public boolean isPostLiked(Like like) {
        return likeDao.checkLike(like);
    }

    public void addLike(Like like) {
        likeDao.insertLike(like);
    }

    public void removeLike(Like like) {
        likeDao.removeLike(like);
    }

    public int getNextPostId(String username) {
        try{
            return postDao.getNextPostId(username);
        }catch (SQLException e){
            throw new RuntimeException("Failed to get next post id", e);
        }

    }

    public List<Comment> getComments(int postId) {
        try{
            return commentDao.getCommentsForPost(postId);
        }catch (SQLException e){
            throw new RuntimeException("Failed to get next comments", e);
        }

    }

    public Post getPost(int postId) {
        return postDao.getPostById(postId);
    }
}
