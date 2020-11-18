package bibtek.ui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import bibtek.core.User;
import bibtek.json.StorageHandler;
import bibtek.ui.utils.ToastUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CreateUserController extends SceneChangerController implements Initializable {

    @FXML
    Button createUserButton;

    @FXML
    TextField userNameInput;

    @FXML
    TextField userNameConfirmInput;

    @FXML
    DigitsField ageInput;

    @FXML
    CheckBox confirmCheckbox;

    @FXML
    Label termsLabel;

    /**
     * Initializes the create user page with appropriate functions.
     */
    @Override
    public void initialize(final URL location, final ResourceBundle resources) {

        termsLabel.setOnMouseClicked((agent) -> this.handleShowTerms());

    }

    /**
     * Shows the terms and conditions to the user.
     */
    @FXML
    public void handleShowTerms() {
        final Stage stage = (Stage) createUserButton.getScene().getWindow();
        try {
            this.changeScene(stage, "/bibtek/ui/fxml/Terms.fxml");
        } catch (IOException e) {
            ToastUtil.makeToast(stage, Toast.ToastState.ERROR, "There was an error showing the terms and conditions");
            e.printStackTrace();
        }
    }

    /**
     * Returns to the login page.
     */
    @FXML
    public void handleShowLogin() {
        final Stage stage = (Stage) createUserButton.getScene().getWindow();
        try {
            this.changeScene(stage, "/bibtek/ui/fxml/Login.fxml");
        } catch (IOException e) {
            ToastUtil.makeToast(stage, Toast.ToastState.ERROR, "There was an error showing the login page");
            e.printStackTrace();
        }
    }

    /**
     * Creates the and saves user.
     */
    @FXML
    public void createUser() {

        final Stage stage = (Stage) createUserButton.getScene().getWindow();

        final String username = userNameInput.getText();
        final String usernameConfirmation = userNameConfirmInput.getText();
        if (!username.equals(usernameConfirmation)) {
            ToastUtil.makeToast(stage, Toast.ToastState.INCORRECT, "Usernames don't match");
            return;
        }
        final int age;
        try {
            age = Integer.parseInt(ageInput.getText());
        } catch (NumberFormatException e) {
            ToastUtil.makeToast(stage, Toast.ToastState.INCORRECT, "You must enter a valid age");
            return;
        }

        if (age < User.MINIMAL_AGE) {
            ToastUtil.makeToast(stage, Toast.ToastState.INCORRECT, "You must be at least 13 years old");
            return;
        }

        if (!confirmCheckbox.isSelected()) {
            ToastUtil.makeToast(stage, Toast.ToastState.INCORRECT, "You must consent to the terms");
            return;
        }

        try {
            final User user = new User(username, age);
            new StorageHandler().putUser(user);
        } catch (Exception e) {
            ToastUtil.makeToast(stage, Toast.ToastState.ERROR, "Failed to create user");
            e.printStackTrace();
        }

        try {
            this.changeSceneAndUpdateUser(stage, "/bibtek/ui/fxml/Login.fxml");
            ToastUtil.makeToast(stage, Toast.ToastState.SUCCESS, "User '" + username + "' created");
        } catch (IOException e) {
            ToastUtil.makeToast(stage, Toast.ToastState.ERROR, "There was an error when showing the login page");
            e.printStackTrace();
        }

    }
}
