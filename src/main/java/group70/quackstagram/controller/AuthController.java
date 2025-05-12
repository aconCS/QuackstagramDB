package group70.quackstagram.controller;

import group70.quackstagram.model.User;
import group70.quackstagram.services.AuthServices;

public class AuthController {

    private final AuthServices authServices;

    public AuthController() {
        this.authServices = new AuthServices();
    }

    public User verifyCredentials(String username, String password) {
        return authServices.verifyCredentials(username, password);
    }

    public boolean doesUsernameExist(String username) {
        return authServices.doesUsernameExist(username);
    }

    public void saveCredentials(String username, String password, String bio) {
        authServices.saveCredentials(username, password, bio);
    }

    public boolean uploadProfilePicture(String username) {
        return authServices.uploadProfilePicture(username);
    }
}