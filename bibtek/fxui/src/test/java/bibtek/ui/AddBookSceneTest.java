package bibtek.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.time.LocalDate;

import javafx.application.Platform;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import bibtek.core.Book;
import bibtek.core.BookEntry;

import bibtek.core.BookReadingState;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import bibtek.core.Library;


public class AddBookSceneTest extends ApplicationTest {

    private Parent parent;
    private AddBookController controller;

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
        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/bibtek/ui/fxml/AddBook.fxml"));
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
     * Testing that the TextFields works properly, and tests if it creates the
     * Library and stores the books correctly.
     */
    @Test
    public void createBookEntryTest() {

        // testing if the fields work correctly
        final int year2020 = 2020;
        final int month9 = 9;
        final int day30 = 30;
        LocalDate timeStamp = LocalDate.of(year2020, month9, day30);
        final TextField addBookTitleField = (TextField) parent.lookup("#addBookTitleField");
        final TextField addBookAuthorField = (TextField) parent.lookup("#addBookAuthorField");
        final TextField addBookYearPublishedField = (TextField) parent.lookup("#addBookYearPublishedField");
        final TextField addBookImagePathField = (TextField) parent.lookup("#addBookImagePathField");
        final DatePicker addBookDatePicker = (DatePicker) parent.lookup("#addBookDatePicker");
        final ComboBox<BookReadingState> addBookReadingStatusCombo = (ComboBox<BookReadingState>) parent
                .lookup("#addBookReadingStatusCombo");
        final Button addBookButton = (Button) parent.lookup("#confirmAddBookButton");
        clickOn(addBookTitleField).write("Finnegans Wake");
        clickOn(addBookAuthorField).write("James Joyce");
        clickOn(addBookYearPublishedField).write("1939");
        addBookImagePathField.setText(
                "http://books.google.com/books/content?id=FNMS7qOqRwEC&printsec=frontcover&img=1&zoom=1&source=gbs_api");
        addBookDatePicker.setValue(timeStamp);
        addBookReadingStatusCombo.setValue(BookReadingState.NOT_STARTED);
        assertEquals("Finnegans Wake", addBookTitleField.getText(), "Book Title should be \"Finnegans Wake\" ");
        assertEquals("James Joyce", addBookAuthorField.getText(), "Book Author should be \"James Joyce\" ");
        assertEquals("1939", addBookYearPublishedField.getText(), "Book Year should be \"1939\" ");
        assertEquals(
                "http://books.google.com/books/content?id=FNMS7qOqRwEC&printsec=frontcover&img=1&zoom=1&source=gbs_api",
                addBookImagePathField.getText(),
                "Book Cover image path should be \"http://books.google.com/books/content?id=FNMS7qOqRwEC&printsec=frontcover&img=1&zoom=1&source=gbs_api\"");
        assertEquals(timeStamp, addBookDatePicker.getValue(), "Book Date Acquired should be " + timeStamp);
        assertEquals(BookReadingState.NOT_STARTED, addBookReadingStatusCombo.getValue(),
                "BookReadingState should be NOT_STARTED");

        // testing if it creates a correct library object with that information
        clickOn(addBookButton);
        Library lib = controller.getUser().getLibrary();
        String expected = (new BookEntry(
                new Book(addBookTitleField.getText(), addBookAuthorField.getText(),
                        Integer.parseInt(addBookYearPublishedField.getText()), addBookImagePathField.getText()),
                addBookDatePicker.getValue(), addBookReadingStatusCombo.getValue())).toString();
        String actual = lib.getBookEntries().stream().map(a -> a.toString()).reduce("", (a, b) -> a + b);
        assertEquals(expected, actual, "Expected book entries was not equal to the actual book entries");

    }

    /**
     * Testing if the year published field does not register letters.
     */

    @Test
    public void yearPublishedFieldTest() {
        // testing if the year published field does not register letters
        final TextField addBookYearPublishedField = (TextField) parent.lookup("#addBookYearPublishedField");
        addBookYearPublishedField.setText("Hello123");
        assertEquals("123", addBookYearPublishedField.getText(),
                "This is a numbers only field, letters are not allowed");

    }

    /**
     * Close application after all tests.
     */
    @AfterAll
    public static void closeApp() { Platform.exit(); }

}
