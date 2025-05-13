package group70.quackstagram.view.coreUI;

import group70.quackstagram.Session;
import group70.quackstagram.controller.NavigationController;
import group70.quackstagram.controller.UserController;
import group70.quackstagram.model.User;
import group70.quackstagram.model.UserProfileData;
import group70.quackstagram.view.components.HeaderPanel;
import group70.quackstagram.view.components.NavigationPanel;
import group70.quackstagram.view.components.UIBase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class EditProfileUI extends UIBase {

    private static final int PROFILE_IMAGE_SIZE = 200;
    private final UserController userController;
    private final User loggedInUser = Session.getInstance().getCurrentUser();

    /**
     * Constructor for EditProfileUI which creates the UI for editing the user's profile.
     */
    public EditProfileUI() {
        this.userController = new UserController();
        setLayout( new BorderLayout());

        buildUI();
    }

    /**
     * Creates the UI components for the EditProfileUI.
     * <p>
     * The UI includes a header panel, a body panel, and a navigation panel.
     * */
    private void buildUI(){
        add(new HeaderPanel("Edit Profile"), BorderLayout.NORTH);
        add(createBodyPanel(), BorderLayout.CENTER);
        add(new NavigationPanel(this), BorderLayout.SOUTH);
    }

    /**
     * Creates the body panel for the EditProfileUI.
     * <p>
     * This panel includes the profile picture editing panel and the bio editing panel.
     * The body panel calls the helper methods createEditPicturePanel and createBioPanel,
     * adding them to the body panel.
     *
     * @return A JPanel containing the body components.
     * */
    private JPanel createBodyPanel(){
        JPanel bodyPanel = new JPanel();
        bodyPanel.setLayout(new BoxLayout(bodyPanel, BoxLayout.Y_AXIS));

        bodyPanel.add(createEditPicturePanel());
        bodyPanel.add(createBioPanel());

        return bodyPanel;
    }

    /**
     * Creates a panel for editing the profile picture.
     * <p>
     * This panel includes a label and the current profile picture. The profile picture
     * is clickable, allowing the user to change it. When clicked, the user is navigated
     * to the `ProfileUI` after changing the profile picture.
     *
     * @return A JPanel containing the profile picture editing components.
     */
    private JPanel createEditPicturePanel(){
        JPanel profilePicturePanel = new JPanel();
        profilePicturePanel.setLayout(new BoxLayout(profilePicturePanel, BoxLayout.Y_AXIS));

        // Creates the label for changing the profile picture
        JLabel editPhotoLabel = new JLabel("Change Profile Picture");
        editPhotoLabel.setFont(new Font("Arial", Font.BOLD, 16));

        // Creates the logged-in user's profile picture
        ImageIcon profileIcon = new ImageIcon(new ImageIcon(loggedInUser.getProfilePictureURL()).getImage().getScaledInstance(PROFILE_IMAGE_SIZE, PROFILE_IMAGE_SIZE, Image.SCALE_SMOOTH));
        JLabel profileImage = new JLabel(profileIcon);
        profileImage.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Makes the image clickable
        profileImage.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                userController.changeProfilePicture(loggedInUser);
                NavigationController.getInstance().navigate(EditProfileUI.this, new ProfileUI(loggedInUser.getUsername()));
            }
        });

        // Adds the components to the panel and sets their alignment
        editPhotoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        profilePicturePanel.add(editPhotoLabel);
        editPhotoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        profilePicturePanel.add(profileImage);

        return profilePicturePanel;
    }

    /**
     * Creates a panel for editing the user's bio.
     * <p>
     * This panel includes a label and a text field for the logged-in user to input their
     * new bio. The text field is created in the getBioField method. When the user presses
     * Enter, the bio is updated and the user is navigated to the `ProfileUI`.
     *
     * @return A JPanel containing the bio editing components.
     */
    private JPanel createBioPanel(){
        JPanel bioFieldPanel = new JPanel(new BorderLayout());
        bioFieldPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, this.getHeight()/2));

        JLabel bioLabel = new JLabel("Change Bio");
        bioLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JTextField bioField = getBioField(bioFieldPanel);

        bioFieldPanel.add(bioLabel, BorderLayout.NORTH);
        bioFieldPanel.add(bioField, BorderLayout.CENTER);

        return bioFieldPanel;
    }

    /**
     * Creates a text field for the user to input their new bio.
     * <p>
     * When the user presses Enter, the bio is updated and the user is navigated to the `ProfileUI`.
     *
     * @param bioFieldPanel The panel containing the bio editing components.
     *
     * @return A JTextField for the user to input their new bio.
     * */
    private JTextField getBioField(JPanel bioFieldPanel) {
        JTextField bioField = new JTextField();
        bioField.setMaximumSize(bioFieldPanel.getMaximumSize());
        bioField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                userController.editBio(loggedInUser, bioField.getText());
                NavigationController.getInstance().navigate(EditProfileUI.this, new ProfileUI(loggedInUser.getUsername()));
            }
        });
        return bioField;
    }


}
