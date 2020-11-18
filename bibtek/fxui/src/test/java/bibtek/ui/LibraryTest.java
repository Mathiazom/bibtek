package bibtek.ui;

import java.time.LocalDate;

import bibtek.core.*;
import bibtek.json.BooksAPIHandler;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing if the logic connected to this fxml scene works as expected.
 */

public class LibraryTest extends ApplicationTest {

    private Parent parent;
    private LibraryController controller;
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
        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/bibtek/ui/Library.fxml"));
        parent = fxmlLoader.load();
        this.controller = fxmlLoader.getController();
        controller.update(dummyUser()); // Dummy user
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
