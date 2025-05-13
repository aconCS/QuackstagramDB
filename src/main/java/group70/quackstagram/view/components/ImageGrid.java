package group70.quackstagram.view.components;

import group70.quackstagram.controller.NavigationController;
import group70.quackstagram.controller.PostController;
import group70.quackstagram.model.Post;
import group70.quackstagram.services.FileServices;
import group70.quackstagram.view.coreUI.PostUI;

import javax.swing.*;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ImageGrid extends JPanel{

    private final int imageSize;
    private final boolean exactMatch;
    private String filter;


    public ImageGrid(String filter, int imageSize, boolean exactMatch) {
        setLayout(new BorderLayout(5,5));
        this.imageSize = imageSize;
        this.exactMatch= exactMatch;
        this.filter = filter;

        initializeScrollGrid();
    }

    public void setFilter(String filter){
        this.filter = filter;
    }

    public void refresh(){
        removeAll();
        initializeScrollGrid();

        revalidate();
        repaint();
    }

    private void initializeScrollGrid() {
        JPanel imageGrid = createGrid();

        JScrollPane scrollPane = new JScrollPane(imageGrid);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createGrid() {
        JPanel imageGrid = new JPanel(new GridLayout(0, 3, 2, 2));
        PostController postController = new PostController();

        // Get filtered posts
        java.util.List<Post> filteredPosts = postController.getFilteredPosts(filter, exactMatch);

        for (Post post : filteredPosts) {
            String imagePath = post.getPictureUrl();
            ImageIcon imageIcon = FileServices.createScaledIcon(imagePath, imageSize, imageSize);

            JLabel imageLabel = new JLabel(imageIcon);
            imageLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    JFrame currFrame = (JFrame) SwingUtilities.getWindowAncestor(imageLabel);
                    NavigationController.getInstance().navigate(currFrame, new PostUI(post));
                }
            });

            imageGrid.add(imageLabel);
        }
        return imageGrid;
    }

}
