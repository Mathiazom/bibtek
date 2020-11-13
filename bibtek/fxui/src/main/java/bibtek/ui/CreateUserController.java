package bibtek.ui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import bibtek.core.User;
import bibtek.json.StorageHandler;
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
    TextField ageInput;

    @FXML
    CheckBox confirmCheckbox;

    @FXML
    Label termsLabel;

    @FXML
    Label errorLabel;

    /**
     * Initializes the create user page with appropriate functions.
     */
    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        ageInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                ageInput.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        termsLabel.setOnMouseClicked((agent) -> this.showTerms());

    }

    /**
     * Shows the terms and conditions to the user.
     */
    @FXML
    public void showTerms() {
        final Stage stage = new Stage();
        try {
            this.changeScene(stage, "/bibtek/ui/Terms.fxml");
        } catch (IOException e) {
            ToastUtil.makeText(stage, Toast.ToastState.ERROR, "There was an error showing the terms and conditions");
            e.printStackTrace();
        }
    }

    /**
     * Creates the and saves user.
     */
    @FXML
    public void createUser() {

        final Stage stage = (Stage) createUserButton.getScene().getWindow();

        String userName1 = userNameInput.getText();
        String userName2 = userNameConfirmInput.getText();
        if (!userName1.equals(userName2)) {
            ToastUtil.makeText(stage, Toast.ToastState.INCORRECT, "Usernames don't match");
            return;
        }
        final int age = Integer.parseInt(ageInput.getText());

        if (age < User.MINIMAL_AGE) {
            ToastUtil.makeText(stage, Toast.ToastState.INCORRECT, "You must be at least 13 \nto create an account");
            return;
        }

        if (!confirmCheckbox.isSelected()) {
            ToastUtil.makeText(stage, Toast.ToastState.INCORRECT, "You must consent to the terms \nto create an account");
            return;
        }

        try {
            User user = new User(userName1, age);
            StorageHandler storageHandler = new StorageHandler();
            storageHandler.putUser(user);
        } catch (Exception e) {
            ToastUtil.makeText(stage, Toast.ToastState.ERROR, "An error occurred");
            e.printStackTrace();
        }

        // Insert code for feedback that the user was created.

        try {
            this.changeSceneAndUpdateUser(stage, "/bibtek/ui/LoginPage.fxml");
        } catch (IOException e) {
            ToastUtil.makeText(stage, Toast.ToastState.ERROR, "There was an error when showing login page");
            e.printStackTrace();
        }

    }
}
