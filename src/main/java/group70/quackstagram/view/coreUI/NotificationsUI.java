package group70.quackstagram.view.coreUI;

import group70.quackstagram.Session;
import group70.quackstagram.controller.NotificationController;
import group70.quackstagram.controller.UserController;
import group70.quackstagram.model.Notification;
import group70.quackstagram.model.User;
import group70.quackstagram.model.UserProfileData;
import group70.quackstagram.utils.TimeStampFormatter;
import group70.quackstagram.view.components.HeaderPanel;
import group70.quackstagram.view.components.NavigationPanel;
import group70.quackstagram.view.components.UIBase;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class NotificationsUI extends UIBase {

    private final UserController userController;
    private final NotificationController notificationController;

    /**
     * Constructor for NotificationsUI.
     *
     * This constructor initializes the NotificationsUI components containing
     * the header panel, navigation panel and the content panel containing the
     * actual notifications.
     * */
    public NotificationsUI() {
        setTitle("Notifications");
        this.userController = new UserController();
        this.notificationController = new NotificationController();
        buildUI();
    }

    /*
     * Builds the user interface for the Notifications screen.
     *
     * This method sets up the header panel, navigation panel, and content panel
     * for displaying notifications. It reads notifications from a file and
     * formats them for display.
     */
    private void buildUI() {
        JPanel headerPanel = new HeaderPanel("Notifications");
        JPanel navigationPanel = new NavigationPanel(this);

        // Content Panel for notifications
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        User currentUser = Session.getInstance().getCurrentUser();
        List<Notification> notificationList = notificationController.getNotifications(currentUser.getUsername());


        for (Notification notification : notificationList) {
            // Adds the notification to the panel
            JPanel notificationPanel = new JPanel(new BorderLayout());
            notificationPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

            // Adds profile icon (if available) and timestamp
            UserProfileData userProfileData = userController.getUserProfileData(notification.getRelatedUserID());
            ImageIcon profileIcon = new ImageIcon(userProfileData.profile_pic());
            profileIcon.setImage(profileIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
            JLabel scaledIcon = new JLabel(profileIcon);

            String timestampMessage = TimeStampFormatter.getElapsedTimestamp(notification.getDate());

            JLabel timestampLabel = new JLabel(timestampMessage);
            timestampLabel.setFont(new Font("Arial", Font.PLAIN, 10));
            notificationPanel.add(scaledIcon, BorderLayout.WEST);
            notificationPanel.add(timestampLabel, BorderLayout.EAST);

            JLabel notificationLabel = new JLabel(notification.getMessage());
            notificationLabel.setFont(new Font("Arial", Font.BOLD, 12));
            notificationPanel.add(notificationLabel, BorderLayout.CENTER);

            contentPanel.add(notificationPanel);
        }
        // Add panels to frame
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(navigationPanel, BorderLayout.SOUTH);

    }
}