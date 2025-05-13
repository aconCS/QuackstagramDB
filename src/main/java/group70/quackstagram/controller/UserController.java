package group70.quackstagram.controller;

import group70.quackstagram.model.User;
import group70.quackstagram.model.UserProfileData;
import group70.quackstagram.services.FileServices;
import group70.quackstagram.services.UserServices;

import java.util.List;


public class UserController {
    private final UserServices userServices;

    public UserController(){
        this.userServices = new UserServices();
    }

    public boolean register(String username, String passwordHash, String bio, String profilePictureURL){
        return userServices.register(username, passwordHash, bio, profilePictureURL);
    }

    public User login(String username, String passwordHash){
        return userServices.login(username, passwordHash);
    }

    public UserProfileData getUserProfileData(String username){
        return userServices.getUserProfileData(username);
    }

    public void changeProfilePicture(User user){
        String pictureURL = FileServices.fileChooser("Choose profile picture", "png", "jpg");
        if(pictureURL != null){
            user.setProfilePictureURL(pictureURL);
            userServices.updateUser(user);
        }
    }

    public void editBio(User user, String bio){
        user.setBio(bio);
        userServices.updateUser(user);
    }

    public List<User> getFilteredUsers(String filter){
        return userServices.getFilteredUsers(filter);
    }

    public User getUser(String username){
        return userServices.findUserByName(username);
    }
}