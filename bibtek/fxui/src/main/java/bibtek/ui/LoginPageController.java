package bibtek.ui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginPageController implements Initializable {
    @FXML
    Button logInButton;

    @FXML
    TextField userNameInput;

    @FXML
    Label createNewUserLabel;

    /**
     * Initializes the scene.
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        createNewUserLabel.setOnMouseClicked((agent) -> this.createNewUser());

    }

    /**
     * Checks if there is a user of written user name and logs them in. Then sends
     * the user to the library page.
     */
    @FXML
    public void logIn() {
        userNameInput.setText("testUser1");
        logInButton.setText("Hei");
        createNewUserLabel.setText("HAde");
    }

    /**
     * Links the user to the page that creates a new user.
     */
    @FXML
    public void createNewUser() {
        final Stage stage = (Stage) createNewUserLabel.getScene().getWindow();
        final Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/bibtek/ui/CreateUser.fxml"));
            final Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
