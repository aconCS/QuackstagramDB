package group70.quackstagram.view.coreUI;

import group70.quackstagram.controller.UserController;
import group70.quackstagram.model.User;
import group70.quackstagram.view.components.*;

import java.awt.*;

public class ProfileUI extends UIBase {

    private final int width = this.getWidth();
    private final int imageSize = width / 3;

    private final UserController userController;
    private final String username;

    /**
    * Constructor for ProfileUI which creates the profile page for the given user.
    *
    * @param username The owner of the user whose profile is to be displayed.
    * */
    public ProfileUI(String username) {
        this.username = username;
        setTitle("DACS Profile");

        // Initialize the user controller which will provide information belonging
        // to the user with the given owner
        userController = new UserController();

        buildUI();
    }

    /**
    * Builds the user interface for the Profile screen.
    * <p>
    * Clears the existing components and rebuilds the UI with the appropriate
    * header for user whose owner is provided. The navigation panel is also
    * added to the bottom of the layout. An image grid is initialized and
    * added to the center
    * */
    private void buildUI() {
        getContentPane().removeAll();

        add(new ProfileHeader(username,userController), BorderLayout.NORTH);
        add(new NavigationPanel(this), BorderLayout.SOUTH);

        ImageGrid imageGridPanel = new ImageGrid(username, imageSize, true);
        add(imageGridPanel, BorderLayout.CENTER);
    }

}