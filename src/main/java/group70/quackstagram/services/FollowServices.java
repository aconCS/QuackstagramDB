package group70.quackstagram.services;

import group70.quackstagram.dao.FollowDAO;
import group70.quackstagram.model.User;

import java.util.List;

public class FollowServices {

    private final FollowDAO followDAO;

    public FollowServices() {
        this.followDAO = new FollowDAO();
    }

    public List<User> getFollowers(String username){
        try{
            return followDAO.getFollowers(username);
        }catch (Exception e) {
            throw new RuntimeException("Couldn't get followers: ", e);
        }

    }

    public List<User> getFollowing(String username){
        try{
            return followDAO.getFollowing(username);
        }catch (Exception e) {
            throw new RuntimeException("Couldn't get following: ", e);
        }
    }

    public boolean followUser(String followerId, String followeeId){
        try{
            return followDAO.followUser(followerId, followeeId);
        }catch (Exception e) {
            throw new RuntimeException("Couldn't follow user: ", e);
        }

    }

    public boolean unfollowUser(String followerId, String followeeId){
        try{
            return followDAO.unfollowUser(followerId, followeeId);
        }catch (Exception e) {
            throw new RuntimeException("Couldn't unfollow user: ", e);
        }
    }
}
