package group70.quackstagram.model;

public record User(int userID, String username,
                   String passwordHash, String bio,
                   String profilePictureURL) {}
