package bibtek.ui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import bibtek.core.User;
import bibtek.json.StorageHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class LoginPageController implements Initializable {
    @FXML
    Button logInButton;

    @FXML
    TextField userNameInput;

    @FXML
    Label createNewUserLabel;

    @FXML
    Label errorLabel;

    private StorageHandler storageHandler;

    private User user;

    /**
     * Initializes the scene.
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        createNewUserLabel.setOnMouseClicked((agent) -> this.createNewUser());
        try {
            storageHandler = new StorageHandler("target/user.json");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Checks if there is a user of written user name and logs them in. Then sends
     * the user to the library page.
     */
    @FXML
    public void logIn() {
        List<String> usernames = storageHandler.getAllUserNamesFromServer();
        LibraryController controller;
        String name = userNameInput.getText();
        if (!usernames.contains(name)) {
            errorLabel.setText("No user with given username");
            errorLabel.setTextFill(Color.RED);
            return;
        }
        try {
            user = storageHandler.getUserFromServer(name);
        } catch (IOException e1) {
            errorLabel.setText("There was an error retreiving user data, try again later");
            errorLabel.setTextFill(Color.RED);
            return;
        }
        final Stage stage = (Stage) logInButton.getScene().getWindow();
        final Parent root;
        try {
            final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/bibtek/ui/Library.fxml"));
            root = fxmlLoader.load();
            final Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            controller = fxmlLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        controller.update(user);

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
