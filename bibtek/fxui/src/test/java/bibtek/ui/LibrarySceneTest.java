package bibtek.ui;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;

import org.testfx.framework.junit5.ApplicationTest;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Testing if the logic connected to this fxml scene works as expected.
 */

public class LibrarySceneTest extends ApplicationTest {

    private Parent parent;
    private LibraryController controller;

    /**
     * Prepares the system.
     */
    // @BeforeAll
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
        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/bibtek/ui/fxml/Library.fxml"));
        parent = fxmlLoader.load();
        this.controller = fxmlLoader.getController();
        stage.setScene(new Scene(parent));
        stage.show();

        // delete json file to ensure it is empty
        try {
            File json = new File("target/testLibrary.json");
            json.delete();
        } catch (Exception e) {
        } // if it fails, then it does not matter

        // Change local storage path to temporary test file
        // this.controller.getLibrary().setStoragePath("target/testLibrary.json");

    }

    /**
     * Testing if the handleAddBook() method works correctly.
     */
    // @Test
    public void handleAddBooktest() {
        final Button button = (Button) parent.lookup("#addBookButton");
        try {
            clickOn(button);
        } catch (Exception e) {
            fail("handleAddBook() method failed or the AddBookButton does not exist");
        }

    }

}
