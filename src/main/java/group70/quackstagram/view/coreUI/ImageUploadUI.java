package group70.quackstagram.view.coreUI;

import group70.quackstagram.Session;
import group70.quackstagram.controller.PostController;
import group70.quackstagram.controller.UserController;
import group70.quackstagram.model.Post;
import group70.quackstagram.services.FileServices;
import group70.quackstagram.view.components.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ImageUploadUI extends UIBase {

    private final int width = this.getWidth();
    private final int height = this.getHeight();

    private final PostController postController;
    private JLabel imagePreviewLabel;
    private JTextArea captionTextArea;
    private JButton uploadButton;

    /**
     * Constructor for ImageUploadUI.
     * <p>
     * This constructor initializes the ImageUploadUI components, including the header panel,
     * navigation panel, image preview label, caption text area, upload button, and save button.
     */
    public ImageUploadUI() {
        setTitle("Upload Image");
        this.postController = new PostController();
        buildUI();
    }

    /**
     * Initializes the user interface components for the ImageUploadUI.
     * <p>
     * This method sets up the header panel, navigation panel, image preview label,
     * caption text area, upload button, and save button.
     */
    private void buildUI() {
        JPanel headerPanel = new HeaderPanel("Upload Image");

        JPanel navigationPanel = new NavigationPanel(this);

        // Main content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        // Image preview
        imagePreviewLabel = new JLabel();
        imagePreviewLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        imagePreviewLabel.setPreferredSize(new Dimension(width, height / 3));

        // Sets an initial empty icon to the imagePreviewLabel
        ImageIcon emptyImageIcon = new ImageIcon();
        imagePreviewLabel.setIcon(emptyImageIcon);

        contentPanel.add(imagePreviewLabel);

        // Caption-text area
        captionTextArea = new JTextArea("Enter a caption");
        captionTextArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        captionTextArea.setLineWrap(true);
        captionTextArea.setWrapStyleWord(true);
        JScrollPane captionScrollPane = new JScrollPane(captionTextArea);
        captionScrollPane.setPreferredSize(new Dimension(width - 50, height / 6));
        contentPanel.add(captionScrollPane);

        // Upload button
        uploadButton = new JButton("Upload Image");
        uploadButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        uploadButton.addActionListener(this::uploadAction);
        contentPanel.add(uploadButton);

        // Adds panels to frame
        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        add(navigationPanel, BorderLayout.SOUTH);
    }

    /**
     * Handles the action of uploading an image.
     * <p>
     * This method opens a file chooser dialog to select an image file, saves the selected image
     * to the specified directory, updates the image preview label, and changes the text of the upload button.
     *
     * @param event The action event triggered by the upload button.
     */
    private void uploadAction(ActionEvent event) {
        String selectedPath = FileServices.fileChooser("Select an image file", "png", "jpg", "jpeg");
        if (selectedPath != null) {
            File selectedFile = new File(selectedPath);
            try {
                String username = Session.getInstance().getCurrentUser().getUsername();
                String fileExtension = FileServices.getFileExtension(selectedFile);

                // Generate next post ID
                int postId = postController.getNextPostId(username);
                String newFileName = username+postId + "." + fileExtension;

                Path destPath = Paths.get("src/main/resources/img", "uploaded", newFileName);
                Files.copy(selectedFile.toPath(), destPath, StandardCopyOption.REPLACE_EXISTING);

                // Create Post object with finalized picture URL
                Post post = new Post(username, destPath.toString(), captionTextArea.getText());
                postController.createPost(post);

                // Preview update
                ImageIcon imageIcon = new ImageIcon(destPath.toString());
                if (imagePreviewLabel.getWidth() > 0 && imagePreviewLabel.getHeight() > 0) {
                    Image image = imageIcon.getImage();
                    int previewWidth = imagePreviewLabel.getWidth();
                    int previewHeight = imagePreviewLabel.getHeight();
                    double widthRatio = (double) previewWidth / image.getWidth(null);
                    double heightRatio = (double) previewHeight / image.getHeight(null);
                    double scale = Math.min(widthRatio, heightRatio);
                    int scaledWidth = (int)(scale * image.getWidth(null));
                    int scaledHeight = (int)(scale * image.getHeight(null));
                    imageIcon.setImage(image.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH));
                }
                imagePreviewLabel.setIcon(imageIcon);
                uploadButton.setText("Upload Another Image");
                JOptionPane.showMessageDialog(this, "Image uploaded, preview updated, and post created!");

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error saving image: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}