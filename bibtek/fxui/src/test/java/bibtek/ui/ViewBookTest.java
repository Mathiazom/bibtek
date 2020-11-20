package bibtek.ui;

import bibtek.core.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;

import static bibtek.ui.TestConstants.ROBOT_PAUSE_MS;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing if the logic connected to this fxml scene works as expected.
 */

public class ViewBookTest extends WireMockApplicationTest {

    private Parent parent;
    private ViewBookController controller;
    private Stage stage;

    private BookEntry bookEntry;

    /**
     * Starts the app to test it.
     *
     * @param stage takes the stage of the app
     */
    @Override
    public void start(final Stage stage) throws Exception {
        this.stage = stage;
        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/bibtek/ui/fxml/ViewBook.fxml"));
        parent = fxmlLoader.load();
        this.controller = fxmlLoader.getController();
        final User dummyUser = TestConstants.userDante();
        this.bookEntry = dummyUser.getLibrary().getBookEntries().iterator().next();
        controller.update(bookEntry, dummyUser);
        stage.setScene(new Scene(parent));
        stage.show();

    }

    @Test
    public void bookDetailsTest(){

        final Label title = (Label) parent.lookup("#bookEntryTitle");
        assertNotNull(title);
        assertEquals(bookEntry.getBook().getTitle(),title.getText());

        final Label author = (Label) parent.lookup("#bookEntryAuthor");
        assertNotNull(author);
        assertEquals(bookEntry.getBook().getAuthor(),author.getText());

        final Label yearPublished = (Label) parent.lookup("#bookEntryYearPublished");
        assertNotNull(yearPublished);
        assertEquals(String.valueOf(bookEntry.getBook().getYearPublished()),yearPublished.getText());

        final ComboBox<BookReadingState> bookReadingStateCombo = (ComboBox<BookReadingState>) parent.lookup("#bookReadingStateCombo");
        assertEquals(BookReadingState.NOT_STARTED, bookReadingStateCombo.getValue(),
                "Should display BookReadingState NOT_STARTED");

    }


    @Test
    public void changeReadingStateTest(){

        final ComboBox<BookReadingState> bookReadingStateCombo = (ComboBox<BookReadingState>) parent.lookup("#bookReadingStateCombo");

        // Mock request response
        stubFor(put(urlEqualTo("/bibtek/users/dante"))
                .withHeader("Accept", equalTo("application/json"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("true")
                )
        );

        clickOn(bookReadingStateCombo)
                .sleep(ROBOT_PAUSE_MS)
                .press(KeyCode.DOWN)
                .sleep(ROBOT_PAUSE_MS)
                .press(KeyCode.ENTER); // Select second element

        assertEquals(BookReadingState.READING, bookReadingStateCombo.getValue(),
                "Should display BookReadingState READING");

        assertEquals(BookReadingState.READING, bookEntry.getReadingState(),
                "BookReadingState should be READING");

    }


    /**
     * Make sure user can change their mind and go back to their library without adding a book.
     */
    @Test
    public void backToLibraryTest(){

        clickOn("#backButton");

        final AnchorPane libraryRoot = (AnchorPane) stage.getScene().getRoot().lookup("#libraryRoot");
        assertNotNull(libraryRoot);
        assertTrue(libraryRoot.isVisible());

    }
    
    @Test
    public void handleEditBookTest(){

        clickOn("#editBookButton");

        final AnchorPane editBookRoot = (AnchorPane) stage.getScene().getRoot().lookup("#editBookRoot");
        assertNotNull(editBookRoot);
        assertTrue(editBookRoot.isVisible());
        
    }

}
