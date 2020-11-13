package bibtek.ui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import bibtek.json.StorageHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class LoginPageController extends SceneChangerController implements Initializable {
    @FXML
    Button logInButton;

    @FXML
    TextField userNameInput;

    @FXML
    Label createNewUserLabel;

    @FXML
    Label errorLabel;

    private StorageHandler storageHandler;

    /**
     * Initializes the scene.
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(final URL location, final ResourceBundle resources) {

        createNewUserLabel.setOnMouseClicked((agent) -> this.createNewUser());
        storageHandler = new StorageHandler();

    }

    /**
     * Checks if there is a user of written username and logs them in. Then sends
     * the user to the library page.
     */
    @FXML
    public void logIn() {

        final String username = userNameInput.getText();
        if (!storageHandler.hasUser(username)) {
            ToastUtil.makeText(stage, Toast.ToastState.INFO, "No user with given username");
            return;
        }

        update(storageHandler.getUser(username));

        try {
            final Stage stage = (Stage) logInButton.getScene().getWindow();
            this.changeSceneAndUpdateUser(stage, "/bibtek/ui/Library.fxml");
        } catch (IOException e) {
            ToastUtil.makeText(stage, Toast.ToastState.ERROR, "There was an error when showing your library");
            e.printStackTrace();
        }
    }

    /**
     * Links the user to the page that creates a new user.
     */
    @FXML
    public void createNewUser() {

        final Stage stage = (Stage) createNewUserLabel.getScene().getWindow();
        try {
            this.changeScene(stage, "/bibtek/ui/CreateUser.fxml");
        } catch (IOException e) {
            ToastUtil.makeText(stage, Toast.ToastState.ERROR, "There was an error when showing the create user page");
            e.printStackTrace();
        }

    }
}
