package bibtek.ui;

import java.io.IOException;

import bibtek.core.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public abstract class SceneChangerController {
    private User user;

    /**
     * Method to update the user field.
     *
     * @param u
     */
    public void update(final User u) {
        this.user = u;
    }

    /**
     * Only hanges the scene.
     *
     * @param stage
     * @param fxmlFilePath
     * @return the controller of the new scene.
     * @throws IOException
     */
    public SceneChangerController changeScene(final Stage stage, final String fxmlFilePath) throws IOException {
        final SceneChangerController controller;
        final Parent root;
        try {
            final FXMLLoader fxmlLoader = new FXMLLoader(SceneChangerController.class.getResource(fxmlFilePath));
            root = fxmlLoader.load();
            final Scene scene = new Scene(root);
            stage.setScene(scene);
            controller = fxmlLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("There was an error setting the new scene");
        }
        return controller;

    }

    /**
     * Changes the scene and updates the user field.
     *
     * @param stage
     * @param fxmlFilePath
     * @throws IOException
     */
    public void changeSceneAndUpdateUser(final Stage stage, final String fxmlFilePath) throws IOException {
        this.changeScene(stage, fxmlFilePath).update(user);
    }

    /**
     * Makes the User parameter accessible.
     *
     * @return the user object assiciated to this controller
     */
    public User getUser() {
        return this.user;
    }

}
