package bibtek.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testing if the logic connected to this fxml scene works as expected.
 */

public class LibraryTest extends ApplicationTest {

    private Parent parent;
    private LibraryController controller;
    private Stage stage;


    /**
     * Starts the app to test it.
     *
     * @param stage takes the stage of the app
     */
    @Override
    public void start(final Stage stage) throws Exception {
        this.stage = stage;
        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/bibtek/ui/fxml/Library.fxml"));
        parent = fxmlLoader.load();
        this.controller = fxmlLoader.getController();
        controller.update(TestConstants.userDante()); // Dummy user
        stage.setScene(new Scene(parent));
        stage.show();
    }

    @Test
    public void openSettingsTest(){

        clickOn("#openSettingsButton");

        final AnchorPane settingsRoot = (AnchorPane) stage.getScene().getRoot().lookup("#settingsRoot");
        assertNotNull(settingsRoot);
        assertTrue(settingsRoot.isVisible());

    }

    /**
     * Testing if the handleAddBook() method works correctly.
     */
    @Test
    public void handleAddBookTest() {

        clickOn("#addBookButton");

        final AnchorPane addBookRoot = (AnchorPane) stage.getScene().getRoot().lookup("#addBookRoot");
        assertNotNull(addBookRoot);
        assertTrue(addBookRoot.isVisible());

    }

    /**
     * Make sure the user can view a book from the list
     */
    @Test
    public void viewBookTest(){

        final ListView<BookItemView> libraryList = (ListView<BookItemView>) parent.lookup("#libraryList");
        clickOn(libraryList.getItems().get(0));

        final AnchorPane viewBookRoot = (AnchorPane) stage.getScene().getRoot().lookup("#viewBookRoot");
        assertNotNull(viewBookRoot);
        assertTrue(viewBookRoot.isVisible());

    }

}
