package group70.quackstagram.repository;

import group70.quackstagram.model.Post;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class UserRepository {

    // For writable files (store in project root)
    private final String DATA_DIR = "data/";
    private final String IMG_DIR = "img/";
    private final String followersFilePath = DATA_DIR + "following.txt";
    private final Path imageDetailsFilePath = Paths.get(IMG_DIR, "image_details.txt");
    private final Path usersFilePath = Paths.get(DATA_DIR, "users.txt");
    private final Path credentialsFilePath = Paths.get(DATA_DIR, "credentials.txt");
    private final Path uploadedImagesPath = Paths.get(IMG_DIR, "uploaded");

    public int readPostCount(String username) {
        int imageCount = 0;
        try (BufferedReader imageDetailsReader = Files.newBufferedReader(imageDetailsFilePath)) {
            String line;
            while ((line = imageDetailsReader.readLine()) != null) {
                if (line.contains("Username: " + username)) {
                    imageCount++;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading image details: " + e.getMessage());
        }
        return imageCount;
    }

    public String readLoggedInUsername() {
        try {
            if (!Files.exists(usersFilePath) || Files.size(usersFilePath) == 0) {
                return "";
            }

            try (BufferedReader reader = Files.newBufferedReader(usersFilePath)) {
                String line = reader.readLine();
                if (line != null) {
                    String[] parts = line.split(":");
                    return parts.length > 0 ? parts[0] : "";
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading logged in user: " + e.getMessage());
        }
        return "";
    }

    public void writeFollowData(String follower, String followed) {
        if (!isAlreadyFollowing(follower, followed)) {
            try (BufferedWriter writer = Files.newBufferedWriter(
                    Paths.get(followersFilePath),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND)) {
                writer.write(follower + ":" + followed);
                writer.newLine();
            } catch (IOException ex) {
                System.err.println("Error writing follow data: " + ex.getMessage());
            }
        }
    }

    public void removeFollowData(String follower, String followed) throws IOException {
        if (!isAlreadyFollowing(follower, followed)) {
            return;
        }

        Path filePath = Paths.get(followersFilePath);
        Path tempPath = Paths.get(DATA_DIR, "temp.txt");

        try (BufferedReader reader = Files.newBufferedReader(filePath);
             BufferedWriter writer = Files.newBufferedWriter(tempPath)) {

            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.equals(follower + ":" + followed)) {
                    writer.write(line);
                    writer.newLine();
                }
            }
        }

        Files.delete(filePath);
        Files.move(tempPath, filePath);
    }

    public boolean isAlreadyFollowing(String follower, String followed) {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(followersFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equals(follower + ":" + followed)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.err.println("Error checking follow status: " + e.getMessage());
        }
        return false;
    }

    public List<String> readFollowersData(String username) {
        List<String> followers = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(followersFilePath))) {
            String line;
            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                String[] parts = line.split(":");
                if (parts.length > 1 && parts[1].equals(username)) {
                    followers.add(parts[0]);
                }
            }
        } catch (IOException ex) {
            System.err.println("Error reading followers: " + ex.getMessage());
        }
        return followers;
    }

    public List<String> readFollowingData(String username) {
        List<String> following = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(followersFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length > 0 && parts[0].equals(username)) {
                    following.add(parts[1]);
                }
            }
        } catch (IOException ex) {
            System.err.println("Error reading following: " + ex.getMessage());
        }
        return following;
    }

    public String readBioData(String username) {
        String bio = "";
        try (BufferedReader bioDetailsReader = Files.newBufferedReader(credentialsFilePath)) {
            String line;
            while ((line = bioDetailsReader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length >= 3 && parts[0].equals(username)) {
                    bio = parts[2];
                    break;
                }
            }
        } catch (IOException ex) {
            System.err.println("Error reading bio: " + ex.getMessage());
        }
        return bio;
    }

    public void changeBioData(String newBio) throws IOException {
        Path tempPath = Paths.get(DATA_DIR, "temp.txt");
        String loggedInUsername = readLoggedInUsername();

        try (BufferedReader reader = Files.newBufferedReader(credentialsFilePath);
             BufferedWriter writer = Files.newBufferedWriter(tempPath)) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length >= 3 && parts[0].equals(loggedInUsername)) {
                    parts[2] = newBio;
                    writer.write(String.join(":", parts));
                } else {
                    writer.write(line);
                }
                writer.newLine();
            }
        }

        Files.delete(credentialsFilePath);
        Files.move(tempPath, credentialsFilePath);
    }

    public List<Post> readUserPosts(String currUsername) {
        List<Post> posts = new ArrayList<>();
        try (Stream<String> lines = Files.lines(imageDetailsFilePath)) {
            lines.forEach(line -> {
                if (line.contains("ImageID: ")) {
                    String[] parts = line.split(", ");
                    if (parts.length >= 5) {
                        String username = parts[1].split(": ")[1];
                        if (username.equals(currUsername)) {
                            String imageId = parts[0].split(": ")[1];
                            String caption = parts[2].split(": ")[1];
                            int likes = 0;
                            try {
                                likes = Integer.parseInt(parts[4].split(": ")[1]);
                            } catch (NumberFormatException e) {
                                System.err.println("Failed to parse likes for image ID: " + imageId);
                            }
                            String imgPath = uploadedImagesPath.resolve(imageId + ".png").toString();
                            posts.add(new Post(imgPath));
                        }
                    }
                }
            });
        } catch (IOException ex) {
            System.err.println("Error reading posts: " + ex.getMessage());
        }
        return posts;
    }

    public ArrayList<String> loadAllUsers() {
        ArrayList<String> users = new ArrayList<>();
        try (Stream<String> lines = Files.lines(credentialsFilePath)) {
            lines.forEach(line -> {
                String[] parts = line.split(":");
                if (parts.length > 0) {
                    users.add(parts[0]);
                }
            });
        } catch (IOException e) {
            System.err.println("Error loading users: " + e.getMessage());
        }
        return users;
    }
}