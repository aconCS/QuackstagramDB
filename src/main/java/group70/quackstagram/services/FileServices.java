package group70.quackstagram.services;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class FileServices {

    public static String fileChooser(String dialogTitle, String... extensions) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(dialogTitle);
        fileChooser.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image files", extensions);
        fileChooser.addChoosableFileFilter(filter);

        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile().getPath();
        }
        return null;
    }

    public static ImageIcon createScaledIcon(String path, int width, int height) {
        ImageIcon imageIcon;
        try {
            BufferedImage originalImage = ImageIO.read(new File(path));
            if (originalImage == null) {
                System.out.println("Image not found at path: " + path);
                return null;
            }

            Image scaledImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage bufferedScaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = bufferedScaledImage.createGraphics();
            g2d.drawImage(scaledImage, 0, 0, null);
            g2d.dispose();

            BufferedImage croppedImage = bufferedScaledImage.getSubimage(0, 0, Math.min(bufferedScaledImage.getWidth(), width), Math.min(bufferedScaledImage.getHeight(), height));
            imageIcon = new ImageIcon(croppedImage);
        } catch (IOException ex) {
            System.out.println("Error reading image: " + ex.getMessage());
            return null;
        }
        return imageIcon;
    }

    public static String getFileExtension(File selectedFile) {
        return selectedFile.getName().substring(selectedFile.getName().lastIndexOf(".") + 1);
    }
}
