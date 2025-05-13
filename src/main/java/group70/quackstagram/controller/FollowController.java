package group70.quackstagram.controller;

import group70.quackstagram.model.User;
import group70.quackstagram.services.FollowServices;

import java.util.List;

public class FollowController {

    private final FollowServices followServices;

    public FollowController() {
        this.followServices = new FollowServices();
    }
    public void follow(String followerName, String followeeName){
        if (followServices.followUser(followerName, followeeName)) {
            System.out.println("Now following user " + followeeName);
        }
    }

    public void unfollow(String followerName, String followeeName){
        if (followServices.unfollowUser(followerName, followeeName)) {
            System.out.println("Unfollowed user " + followeeName);
        }
    }

    public List<User> getFollowers(String username){
        return followServices.getFollowers(username);
    }

    public List<User> getFollowing(String username)  {
        return followServices.getFollowing(username);
    }

    public boolean isFollowing(String followerName, String followeeName){
        for(User follower : followServices.getFollowers(followerName)){
            if(followeeName.equals(follower.getUsername())){
                return true;
            }
        }
        return false;
    }
}
