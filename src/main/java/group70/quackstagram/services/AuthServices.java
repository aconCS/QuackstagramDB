package group70.quackstagram.services;

import group70.quackstagram.dao.UserDAO;
import group70.quackstagram.model.User;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class AuthServices {

    private final UserDAO userDAO;
    private static final String PROFILE_PHOTO_STORAGE_PATH = "src/main/resources/img/storage/profile/";

    public AuthServices(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User verifyCredentials(String username, String password) {
        User user = userDAO.findUserByUsername(username);
        if (user != null && user.passwordHash().equals(hashPassword(password))) {
            return user;
        }
        return null;
    }

    public boolean doesUsernameExist(String username) {
        return userDAO.findUserByUsername(username) != null;
    }

    public User registerUser(String username, String password, String bio, String profilePictureUrl) {
        String hashedPassword = hashPassword(password);
        User newUser = new User(0, username, hashedPassword, bio, profilePictureUrl);
        return userDAO.insertUser(newUser);
    }

    public String uploadProfilePicture(File chosenFile, String username) {
        if (chosenFile == null) return null;

        try {
            BufferedImage image = ImageIO.read(chosenFile);
            String outputPath = PROFILE_PHOTO_STORAGE_PATH + username + ".png";
            File outputFile = new File(outputPath);
            ImageIO.write(image, "png", outputFile);
            return outputPath;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // TODO: IMPLEMENT PASSWORD-HASHING FUNCTION
    public String hashPassword(String password) {
        return null;
    }

}