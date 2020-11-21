package bibtek.ui;

import bibtek.core.Book;
import bibtek.core.BookEntry;
import bibtek.core.BookReadingState;
import bibtek.core.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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

public class EditBookTest extends WireMockApplicationTest {

    private Parent parent;
    private EditBookController controller;
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
        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/bibtek/ui/fxml/EditBook.fxml"));
        parent = fxmlLoader.load();
        this.controller = fxmlLoader.getController();
        final User user = TestConstants.userDante();
        this.bookEntry = TestConstants.fahrenheit();
        controller.update(bookEntry, user); // Dummy user
        stage.setScene(new Scene(parent));
        stage.show();

    }

    @Test
    public void editBookFieldTest(){

        final ComboBox<BookReadingState> addBookReadingStateCombo = (ComboBox<BookReadingState>) parent.lookup("#bookReadingStateCombo");
        clickOn(addBookReadingStateCombo)
                .sleep(ROBOT_PAUSE_MS)
                .press(KeyCode.DOWN)
                .sleep(ROBOT_PAUSE_MS)
                .press(KeyCode.ENTER); // Select second element
        assertEquals(BookReadingState.READING, addBookReadingStateCombo.getValue(),
                "BookReadingState should be READING");

        // Mock request response
        stubFor(put(urlEqualTo("/bibtek/users/dante"))
                .withHeader("Content-Type", equalTo("application/json"))
                .willReturn(aResponse()
                        .withStatus(204)
                )
        );

        clickOn("#confirmEditBookButton");

        assertEquals(BookReadingState.READING, bookEntry.getReadingState(),"BookReadingState should be READING after editing");

    }

    @Test
    public void bookDetailsTest(){

        final Book book = bookEntry.getBook();

        final TextField bookTitleInput = (TextField) parent.lookup("#bookTitleInput");
        assertEquals(book.getTitle(), bookTitleInput.getText(), "Book Title should be " + book.getTitle());

        final TextField bookAuthorInput = (TextField) parent.lookup("#bookAuthorInput");
        assertEquals(book.getAuthor(), bookAuthorInput.getText(), "Book Author should be " + book.getAuthor());

        final DigitsField bookYearPublishedInput = (DigitsField) parent.lookup("#bookYearPublishedInput");
        assertEquals(book.getYearPublished(), bookYearPublishedInput.getInputAsInt(), "Book Year should be " + book.getYearPublished());

        final TextField addBookImagePathInput = (TextField) parent.lookup("#bookImagePathInput");
        assertEquals(book.getImgPath(), addBookImagePathInput.getText(),
                "Book Cover image path should be " + book.getImgPath());

        final DatePicker bookDatePicker = (DatePicker) parent.lookup("#bookDatePicker");
        assertEquals(bookEntry.getDateAcquired(), bookDatePicker.getValue(), "Book date acquired should be " + bookEntry.getDateAcquired());

        final ComboBox<BookReadingState> addBookReadingStateCombo = (ComboBox<BookReadingState>) parent.lookup("#bookReadingStateCombo");
        assertEquals(bookEntry.getReadingState(), addBookReadingStateCombo.getValue(),
                "BookReadingState should be " + bookEntry.getReadingState());

    }


    /**
     * Make sure user can change their mind and go back to their library without adding a book.
     */
    @Test
    public void backToViewBookTest(){

        clickOn("#cancelButton");

        final AnchorPane viewBookRoot = (AnchorPane) stage.getScene().getRoot().lookup("#viewBookRoot");
        assertNotNull(viewBookRoot);
        assertTrue(viewBookRoot.isVisible());

    }

}
