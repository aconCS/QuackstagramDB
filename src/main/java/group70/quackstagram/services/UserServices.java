package group70.quackstagram.services;

import group70.quackstagram.dao.FollowDAO;
import group70.quackstagram.dao.PostDAO;
import group70.quackstagram.dao.UserDAO;
import group70.quackstagram.model.User;
import group70.quackstagram.model.UserProfileData;

import java.util.List;

public class UserServices {
    private final UserDAO userDAO;
    private final FollowDAO followDAO;
    private final PostDAO postDAO;

    public UserServices() {
        this.userDAO = new UserDAO();
        this.postDAO = new PostDAO();
        this.followDAO = new FollowDAO();
    }

    public void updateUser(User user) {
        try{
            userDAO.updateUser(user);
        }catch (Exception e) {
            throw new RuntimeException("Couldn't updateUser: ", e);
        }
    }

    public boolean register(String username, String passwordHash, String bio, String profilePictureURL){
        try{
            User user = new User(username, passwordHash, bio, profilePictureURL);
            return userDAO.registerUser(user);
        }catch (Exception e) {
            throw new RuntimeException("Couldn't register: ", e);
        }
    }

    public User login(String username, String passwordHash){
        try{
            return userDAO.validateLogin(username, passwordHash);
        }catch (Exception e) {
            throw new RuntimeException("Couldn't login: ", e);
        }
    }

    public UserProfileData getUserProfileData(String username){
        try{
            User user = findUserByName(username);
            return new UserProfileData(username,
                    user.getBio(),
                    user.getProfilePictureURL(),
                    postDAO.countPostsByUser(username),
                    followDAO.getFollowers(username).size(),
                    followDAO.getFollowing(username).size());
        }catch (Exception e){
            throw new RuntimeException("Couldn't get profile data: ", e);
        }

    }

    public List<User> getFilteredUsers(String filter){
        return userDAO.getFilteredUsers(filter);
    }

    public User findUserByName(String username){
        return userDAO.findByUsername(username);
    }
}

