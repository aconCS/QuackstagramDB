package group70.quackstagram;

import group70.quackstagram.model.User;

public class Session {

    private static Session instance;
    private User currentUser;

    // Private constructor to prevent instantiation
    private Session() {}

    // Public method to get the singleton instance
    public static Session getInstance() {
        if (instance == null) {
            synchronized (Session.class) {
                if (instance == null) {
                    instance = new Session();
                }
            }
        }
        return instance;
    }

    // Login a user
    public void login(User user) {
        this.currentUser = user;
    }

    // Get current logged-in user
    public User getCurrentUser() {
        return currentUser;
    }

    // Logout user
    public void logout() {
        this.currentUser = null;
    }

    // Check if a user is logged in
    public boolean isLoggedIn() {
        return currentUser != null;
    }
}

