package bibtek.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

public class CreateUserTest extends ApplicationTest {

    private Parent parent;
    private LoginPageController controller;
    private Stage stage;

    /**
     * Prepares the system.
     */
    @BeforeAll
    public static void headless() {
        if (Boolean.parseBoolean(System.getProperty("gitlab-ci", "false"))) {
            System.setProperty("prism.verbose", "true");
            System.setProperty("java.awt.headless", "true");
            System.setProperty("testfx.robot", "glass");
            System.setProperty("testfx.headless", "true");
            System.setProperty("glass.platform", "Monocle");
            System.setProperty("monocle.platform", "Headless");
            System.setProperty("prism.order", "sw");
            System.setProperty("prism.text", "t2k");
            System.setProperty("testfx.setup.timeout", "2500");
        }
    }

    /**
     * Starts the app to test it.
     *
     * @param stage takes the stage of the app
     */
    @Override
    public void start(final Stage stage) throws Exception {
        this.stage = stage;
        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/bibtek/ui/LoginPage.fxml"));
        this.parent = fxmlLoader.load();
        this.controller = fxmlLoader.getController();
        stage.setScene(new Scene(parent));
        stage.show();

    }

    @Test
    public void openCreateUser(){
        // Clicks on create a new user and we expect createUser scene to open
        final Label createNewUserLabel = (Label) parent.lookup("#createNewUserLabel");
        // checks if createUserButton is NOT present before opening createUserPage
        Assertions.assertNull(parent.lookup("#createUserButton"));
        clickOn(createNewUserLabel);


        // Checks if the createUserButton is available
        final Button createUserButton = (Button) stage.getScene().getRoot().lookup("#createUserButton");
        Assertions.assertNotNull(createUserButton);
        Assertions.assertTrue(createUserButton.isVisible()); //checks if button is visible

    }

}
