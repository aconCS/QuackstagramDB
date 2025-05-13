package group70.quackstagram.controller;

import group70.quackstagram.model.Comment;
import group70.quackstagram.model.Like;
import group70.quackstagram.model.Post;
import group70.quackstagram.services.PostServices;

import java.util.List;

public class PostController {

    private final PostServices postService;

    public PostController() {
        this.postService = new PostServices();
    }

    public void createPost(Post post) {
        postService.createNewPost(post);
    }

    public int getUserPostCount(String username) {
        return postService.getPostCount(username);
    }

    public List<Post> getFilteredPosts(String filter, boolean exactMatch) {
        return postService.getFilteredPosts(filter, exactMatch);
    }

    public void addComment(Comment comment) {
        postService.addComment(comment);
    }

    public boolean isPostLiked(Like like) {
        return postService.isPostLiked(like);
    }

    public void likeAction(Like like) {
        if (isPostLiked(like)){
            postService.removeLike(like);
        }else{
            postService.addLike(like);
        }
    }

    public int getNextPostId(String username) {
        return postService.getNextPostId(username);
    }

    public List<Comment> getComments(int postId) {
        return postService.getComments(postId);
    }

    public Post getPost(int postId) {
        return postService.getPost(postId);
    }
}

