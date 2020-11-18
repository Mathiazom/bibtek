package bibtek.ui;

import bibtek.core.*;
import bibtek.json.BooksAPIHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing if the logic connected to this fxml scene works as expected.
 */

public class EditBookTest extends ApplicationTest {

    private Parent parent;
    private EditBookController controller;
    private Stage stage;

    private BookEntry bookEntry;

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
        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/bibtek/ui/EditBook.fxml"));
        parent = fxmlLoader.load();
        this.controller = fxmlLoader.getController();
        final User user = dummyUser();
        this.bookEntry = user.getLibrary().getBookEntries().iterator().next();
        controller.update(bookEntry, user); // Dummy user
        stage.setScene(new Scene(parent));
        stage.show();

    }

    // TODO: Replace with dummy from rebase
    private User dummyUser(){

        final Library library = new Library();

        final int danteBigBoyAge = 800;
        final User dummyUser = new User("dante", danteBigBoyAge, library);

        final int dummyBookYear = 1953;
        final int dummyBookYear2 = 1948;

        library.addBookEntry(
                new BookEntry(
                        new Book(
                                "Fahrenheit 451",
                                "Ray Bradbury",
                                dummyBookYear,
                                "https://s2982.pcdn.co/wp-content/uploads/2017/09/fahrenheit-451-flamingo-edition.jpg"
                        ),
                        LocalDate.now(),
                        BookReadingState.READING
                )
        );
        library.addBookEntry(
                new BookEntry(
                        new Book(
                                "1984",
                                "George Orwell",
                                dummyBookYear2
                        ),
                        LocalDate.now(),
                        BookReadingState.COMPLETED
                )
        );

        library.addBookEntry(
                new BookEntry(
                        new BooksAPIHandler().fetchBook("9780241242643"),
                        LocalDate.now(),
                        BookReadingState.ABANDONED
                )
        );

        library.addBookEntry(
                new BookEntry(
                        new BooksAPIHandler().fetchBook("9780765394866"),
                        LocalDate.now(),
                        BookReadingState.NOT_STARTED
                )
        );

        return dummyUser;

    }


    @Test
    public void bookDetailsTest(){

        final Book book = bookEntry.getBook();

        final TextField addBookTitleField = (TextField) parent.lookup("#bookTitleInput");
        assertEquals(book.getTitle(), addBookTitleField.getText(), "Book Title should be " + book.getTitle());

        final TextField addBookAuthorField = (TextField) parent.lookup("#bookAuthorInput");
        assertEquals(book.getAuthor(), addBookAuthorField.getText(), "Book Author should be " + book.getAuthor());

        final DigitsField addBookYearPublishedField = (DigitsField) parent.lookup("#bookYearPublishedInput");
        assertEquals(book.getYearPublished(), addBookYearPublishedField.getInputAsInt(), "Book Year should be " + book.getYearPublished());

        final TextField addBookImagePathInput = (TextField) parent.lookup("#bookImagePathInput");
        assertEquals(book.getImgPath(), addBookImagePathInput.getText(),
                "Book Cover image path should be " + book.getImgPath());

        final DatePicker addBookDatePicker = (DatePicker) parent.lookup("#bookDatePicker");
        assertEquals(bookEntry.getDateAcquired(), addBookDatePicker.getValue(), "Book date acquired should be " + bookEntry.getDateAcquired());

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

        final AnchorPane libraryRoot = (AnchorPane) stage.getScene().getRoot().lookup("#viewBookRoot");
        assertNotNull(libraryRoot);
        assertTrue(libraryRoot.isVisible());

    }

}
