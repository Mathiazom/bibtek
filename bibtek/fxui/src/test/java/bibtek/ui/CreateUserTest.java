package bibtek.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateUserTest extends ApplicationTest {

    private Parent parent;
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
        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/bibtek/ui/fxml/CreateUser.fxml"));
        this.parent = fxmlLoader.load();
        stage.setScene(new Scene(parent));
        stage.show();

    }

    @Test
    public void usernameMismatchTest() {

        clickOn("#userNameInput").write("heinrich");
        clickOn("#userNameConfirmInput").write("wilhelm");

        clickOn("#createUserButton");

        FxTestUtil.assertToast(Toast.ToastState.INCORRECT, "Usernames don't match", parent);

    }

    @Test
    public void badAgeTest() {

        ((TextField) parent.lookup("#userNameInput")).setText("heinrich");
        ((TextField) parent.lookup("#userNameConfirmInput")).setText("heinrich");

        // No age
        clickOn("#createUserButton");
        FxTestUtil.assertToast(Toast.ToastState.INCORRECT, "You must enter a valid age", parent);

        // Age under 13
        clickOn("#ageInput").write("8");
        clickOn("#createUserButton");
        FxTestUtil.assertToast(Toast.ToastState.INCORRECT, "You must be at least 13 years old", parent);

    }

    @Test
    public void declineTermsTest() {

        ((TextField) parent.lookup("#userNameInput")).setText("heinrich");
        ((TextField) parent.lookup("#userNameConfirmInput")).setText("heinrich");
        ((DigitsField) parent.lookup("#ageInput")).setText("17");

        clickOn("#createUserButton");
        FxTestUtil.assertToast(Toast.ToastState.INCORRECT, "You must consent to the terms", parent);

    }

    /**
     * Make sure user can view terms page
     */
    @Test
    public void showTermsTest() {

        clickOn("#termsLabel");

        final AnchorPane termsRoot = (AnchorPane) stage.getScene().getRoot().lookup("#termsRoot");
        assertNotNull(termsRoot);
        assertTrue(termsRoot.isVisible());

    }

    /**
     * Make sure user can return to login page.
     */
    @Test
    public void backToLoginTest() {

        clickOn("#cancelButton");

        final AnchorPane loginRoot = (AnchorPane) stage.getScene().getRoot().lookup("#loginRoot");
        assertNotNull(loginRoot);
        assertTrue(loginRoot.isVisible());

    }

}
