package group70.quackstagram.view.coreUI;

import group70.quackstagram.controller.UserController;
import group70.quackstagram.model.User;
import group70.quackstagram.view.components.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SearchUserUI extends UIBase {

    private final UserController userController;
    private final String filter;

    /**
     * Constructor for SearchUserUI.
     *
     * This constructor initializes the SearchUserUI components which include
     * the header and navigation panel, and the body panel which contains the
     * users that match the filter.
     *
     * @param filter The user input that is used to filter the users or posts.
     */
    public SearchUserUI(String filter) {
        this.userController = new UserController();
        this.filter = filter;

        setLayout(new BorderLayout(5, 5));
        buildUI();
    }

    /**
     * Initializes the user interface components for the SearchUserUI.
     * <p>
     * This method sets up the header panel, navigation panel, and
     * body panel containing users, posts, or nothing.
     */
    private void buildUI(){
        add(new HeaderPanel("Search: " + filter), BorderLayout.NORTH);
        add(createBodyPanel(), BorderLayout.CENTER);
        add(new NavigationPanel(this), BorderLayout.SOUTH);
    }

    /***
     * Creates the body panel for the SearchUserUI.
     * <p>
     * This method creates a JPanel that contains the users or posts that
     * match the filter.
     *
     * @return JPanel containing the users that match the filter.
     */
    private JPanel createBodyPanel(){
        JPanel bodyPanel = new JPanel();
        bodyPanel.setLayout(new BoxLayout(bodyPanel, BoxLayout.PAGE_AXIS));

        // Initializes a string of all usernames
        List<User> filteredUsers = userController.getFilteredUsers(filter);

        for (User user : filteredUsers) {
            bodyPanel.add(new UserNavPanel(user));
        }

        return bodyPanel;
    }


}
