package bibtek.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testing if the logic connected to this fxml scene works as expected.
 */

public class SettingsTest extends ApplicationTest {

    private Parent parent;
    private Stage stage;

    /**
     * Starts the app to test it.
     *
     * @param stage takes the stage of the app
     */
    @Override
    public void start(final Stage stage) throws Exception {
        this.stage = stage;
        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/bibtek/ui/fxml/Settings.fxml"));
        parent = fxmlLoader.load();
        final SettingsController settingsController = fxmlLoader.getController();
        settingsController.update(TestConstants.userDante());
        stage.setScene(new Scene(parent));
        stage.show();
    }

    @Test
    public void logOffTest(){

        clickOn("#logOffButton");

        final AnchorPane loginRoot = (AnchorPane) stage.getScene().getRoot().lookup("#loginRoot");
        assertNotNull(loginRoot);
        assertTrue(loginRoot.isVisible());

    }

    /**
     * Make sure user can change their mind and go back to their library without adding a book.
     */
    @Test
    public void backToLibraryTest(){

        clickOn("#exitSettingsButton");

        final AnchorPane libraryRoot = (AnchorPane) stage.getScene().getRoot().lookup("#libraryRoot");
        assertNotNull(libraryRoot);
        assertTrue(libraryRoot.isVisible());

    }

}
