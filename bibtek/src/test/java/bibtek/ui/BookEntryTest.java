package bibtek.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.time.LocalDate;

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
import bibtek.json.StorageHandler;

public class BookEntryTest extends ApplicationTest {

    private Parent parent;
    private AddBookController controller;

    @Override
    public void start(final Stage stage) throws Exception {
        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/bibtek/ui/AddBook.fxml"));
        parent = fxmlLoader.load();
        this.controller = fxmlLoader.getController();
        stage.setScene(new Scene(parent));
        stage.show();

        // delete json file to ensure it is empty
        try {
            File json = new File("target/testLibrary");
            json.delete();
        } catch (Exception e) {
        } // if it fails, then it doesn not matter
        StorageHandler storageHandler = new StorageHandler("target/testLibrary.json");
        this.controller.getLibrary().setStorageHandler(storageHandler);
    }

    @Test
    public void testCreateBookEntry() {

        // testing if the fields work correctly
        LocalDate timeStamp = LocalDate.now();

        final TextField addBookTitleField = (TextField) parent.lookup("#addBookTitleField");
        final TextField addBookAuthorField = (TextField) parent.lookup("#addBookAuthorField");
        final TextField addBookYearPublishedField = (TextField) parent.lookup("#addBookYearPublishedField");
        final DatePicker addBookDatePicker = (DatePicker) parent.lookup("#addBookDatePicker");
        final ComboBox<BookReadingState> addBookReadingStatusCombo = (ComboBox<BookReadingState>) parent
                .lookup("#addBookReadingStatusCombo");
        final Button addBookButton = (Button) parent.lookup("#addBookButton");
        addBookTitleField.setText("Finnegans Wake");
        addBookAuthorField.setText("James Joyce");
        addBookYearPublishedField.setText("1939");
        addBookDatePicker.setValue(timeStamp);
        addBookReadingStatusCombo.setValue(BookReadingState.NOT_STARTED);
        assertEquals("Finnegans Wake", addBookTitleField.getText(), "Book Title should be \"Finnegans Wake\" ");
        assertEquals("James Joyce", addBookAuthorField.getText(), "Book Author should be \"James Joyce\" ");
        assertEquals("1939", addBookYearPublishedField.getText(), "Book Year should be \"1939\" ");
        assertEquals(timeStamp, addBookDatePicker.getValue(), "Book Date Acquired should be " + timeStamp);
        assertEquals(BookReadingState.NOT_STARTED, addBookReadingStatusCombo.getValue(),
                "BookReadingState should be NOT_STARTED");

        // testing if it creates a correct library object with that information
        clickOn(addBookButton);
        Library lib = controller.getLibrary();
        String expected = (new BookEntry(
                new Book(addBookTitleField.getText(), addBookAuthorField.getText(),
                        Integer.parseInt(addBookYearPublishedField.getText())),
                addBookDatePicker.getValue(), addBookReadingStatusCombo.getValue())).toString();
        String actual = lib.getBookEntries().stream().map(a -> a.toString()).reduce("", (a, b) -> a + b);
        assertEquals(expected, actual, "Expected book entries was not equal to the actual book entries");

    }

    @Test
    public void testYearPublishedField() {
        // testing if the year published field does not register letters
        final TextField addBookYearPublishedField = (TextField) parent.lookup("#addBookYearPublishedField");
        addBookYearPublishedField.setText("Hello123");
        assertEquals("123", addBookYearPublishedField.getText(),
                "This is a numbers only field, letters are not allowed");

    }
}
