package bibtek.ui;

import bibtek.core.*;
import bibtek.json.BooksAPIHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
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

public class ViewBookTest extends ApplicationTest {

    private Parent parent;
    private ViewBookController controller;
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
        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/bibtek/ui/fxml/ViewBook.fxml"));
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

        final Label title = (Label) parent.lookup("#bookEntryTitle");
        assertNotNull(title);
        assertEquals(bookEntry.getBook().getTitle(),title.getText());

        final Label author = (Label) parent.lookup("#bookEntryAuthor");
        assertNotNull(author);
        assertEquals(bookEntry.getBook().getAuthor(),author.getText());

        final Label yearPublished = (Label) parent.lookup("#bookEntryYearPublished");
        assertNotNull(yearPublished);
        assertEquals(String.valueOf(bookEntry.getBook().getYearPublished()),yearPublished.getText());

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
