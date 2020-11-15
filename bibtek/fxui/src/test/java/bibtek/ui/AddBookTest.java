package bibtek.ui;

import bibtek.core.BookReadingState;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.VerticalDirection;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class AddBookTest extends ApplicationTest {

    private static final int WRITE_ROBOT_WAIT_MILLIS = 1;

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
        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/bibtek/ui/AddBook.fxml"));
        this.parent = fxmlLoader.load();
        this.controller = fxmlLoader.getController();
        stage.setScene(new Scene(parent));
        stage.show();

    }

    private static void ensureVisible(ScrollPane scrollPane, Node node) {
        Bounds viewport = scrollPane.getViewportBounds();
        double contentHeight = scrollPane.getContent().localToScene(scrollPane.getContent().getBoundsInLocal()).getHeight();
        double nodeMinY = node.localToScene(node.getBoundsInLocal()).getMinY();
        double nodeMaxY = node.localToScene(node.getBoundsInLocal()).getMaxY();

        double vValueDelta = 0;
        double vValueCurrent = scrollPane.getVvalue();

        if (nodeMaxY < 0) {
            // currently located above (remember, top left is (0,0))
            vValueDelta = (nodeMinY - viewport.getHeight()) / contentHeight;
        } else if (nodeMinY > viewport.getHeight()) {
            // currently located below
            vValueDelta = (nodeMinY + viewport.getHeight()) / contentHeight;
        }
        scrollPane.setVvalue(vValueCurrent + vValueDelta);
    }

    /**
     * Testing that the TextFields works properly, and tests if it creates the
     * Library and stores the books correctly.
     */
    @Test
    public void createBookEntryTest() {

        final ScrollPane scrollPane = (ScrollPane) parent.lookup("ScrollPane");

        final TextField addBookTitleField = (TextField) parent.lookup("#addBookTitleField");
        clickOn(addBookTitleField).write("Finnegans Wake", WRITE_ROBOT_WAIT_MILLIS);

        assertEquals("Finnegans Wake", addBookTitleField.getText(), "Book Title should be \"Finnegans Wake\" ");

        final TextField addBookAuthorField = (TextField) parent.lookup("#addBookAuthorField");
        clickOn(addBookAuthorField).write("James Joyce", WRITE_ROBOT_WAIT_MILLIS);
        assertEquals("James Joyce", addBookAuthorField.getText(), "Book Author should be \"James Joyce\" ");

        final DigitsField addBookYearPublishedField = (DigitsField) parent.lookup("#addBookYearPublishedField");
        clickOn(addBookYearPublishedField).write("1939", WRITE_ROBOT_WAIT_MILLIS);
        assertEquals(1939, addBookYearPublishedField.getInputAsInt(), "Book Year should be " + 1939);

        final TextField addBookImagePathField = (TextField) parent.lookup("#addBookImagePathField");
        addBookImagePathField.setText("http://books.google.com/books/content?id=FNMS7qOqRwEC&printsec=frontcover&img=1&zoom=1&source=gbs_api");
        assertEquals("http://books.google.com/books/content?id=FNMS7qOqRwEC&printsec=frontcover&img=1&zoom=1&source=gbs_api", addBookImagePathField.getText(),
                "Book Cover image path should be \"http://books.google.com/books/content?id=FNMS7qOqRwEC&printsec=frontcover&img=1&zoom=1&source=gbs_api\"");

        final LocalDate startDate = LocalDate.of(2020, 9, 21);
        final LocalDate targetDate = LocalDate.of(2020, 9, 30);
        final DatePicker addBookDatePicker = (DatePicker) parent.lookup("#addBookDatePicker");
        addBookDatePicker.setValue(startDate);
        final TextField addBookDatePickerField = (TextField) parent.lookup("#addBookDatePickerField");
        clickOn(addBookDatePickerField)
                .sleep(500)
                .type(KeyCode.DOWN).sleep(500) // 21.08.2020 -> 28.08.2020
                .type(KeyCode.RIGHT).sleep(500) // 28.08.2020 -> 29.08.2020
                .type(KeyCode.RIGHT).sleep(500) // 29.08.2020 -> 30.08.2020
                .type(KeyCode.ENTER); // Pick date
        clickOn(addBookTitleField); // Change focus to hide datepicker
        assertEquals(targetDate, addBookDatePicker.getValue(), "Book date acquired should be " + targetDate);

        final ComboBox<BookReadingState> addBookReadingStatusCombo = (ComboBox<BookReadingState>) parent.lookup("#addBookReadingStatusCombo");
        clickOn(addBookReadingStatusCombo)
                .sleep(500)
                .press(KeyCode.DOWN)
                .sleep(500)
                .press(KeyCode.ENTER); // Select second element
        assertEquals(BookReadingState.READING, addBookReadingStatusCombo.getValue(),
                "BookReadingState should be READING");

        // TODO: Following requires integration test
        /*// Create book entry
        clickOn(addBookButton);

        // Testing if it has created a correct library object with the given input
        final String expected = new BookEntry(
                new Book(
                        "Finnegans Wake",
                        "James Joyce",
                        1939,
                        "http://books.google.com/books/content?id=FNMS7qOqRwEC&printsec=frontcover&img=1&zoom=1&source=gbs_api"
                ),
                LocalDate.of(2020, 9, 30),
                BookReadingState.READING
        ).toString();
        final Library library = controller.getUser().getLibrary();
        final String actual = library.getBookEntries().stream().map(BookEntry::toString).reduce("", (a, b) -> a + b);
        assertEquals(expected, actual, "Expected book entries was not equal to the actual book entries");*/

    }

    /**
     * Make sure ISBN field is accessible
     */
    @Test
    public void isbnFieldDisplayTest() {

        clickOn("#isbnButton").sleep(500);

        // Make sure popup container is visible
        assertTrue(parent.lookup("#addBookISBNContainer").isManaged());

        // Make sure field is accessible and digits-only
        final DigitsField addBookISBNField = (DigitsField) parent.lookup("#addBookISBNField");
        clickOn(addBookISBNField).write("9rty7807xz6539Q486kj6", WRITE_ROBOT_WAIT_MILLIS);
        assertEquals("9780765394866", addBookISBNField.getText(), "ISBN input should be 9780765394866");

    }

    /**
     * Testing if the year published field does not register letters.
     */
    @Test
    public void yearPublishedFieldTest() {
        // testing if the year published field does not register letters
        final TextField addBookYearPublishedField = (TextField) parent.lookup("#addBookYearPublishedField");
        addBookYearPublishedField.setText("Hello123World");
        assertEquals("123", addBookYearPublishedField.getText(),
                "This is a numbers only field, letters are not allowed");

    }

    /**
     * Make sure date picker text field displays same date as in date picker
     */
    @Test
    public void datePickerDisplayFieldTest() {

        final LocalDate timeStamp = LocalDate.of(2020, 9, 30);
        final DatePicker addBookDatePicker = (DatePicker) parent.lookup("#addBookDatePicker");
        addBookDatePicker.setValue(timeStamp);
        final TextField addBookDatePickerField = (TextField) parent.lookup("#addBookDatePickerField");
        assertEquals("30.09.2020", addBookDatePickerField.getText(), "Book date acquired displayed in text field should be 30.09.2020");

    }

    /**
     * Make sure all possible values are represented in dropdown
     */
    @Test
    public void bookReadingStateDropdownTest() {

        final ComboBox<BookReadingState> addBookReadingStatusCombo = (ComboBox<BookReadingState>) parent.lookup("#addBookReadingStatusCombo");
        assertEquals(List.of(BookReadingState.values()), List.of(addBookReadingStatusCombo.getItems().toArray()));

    }

    /**
     * Close application after all tests.
     */
    @AfterAll
    public static void closeApp() {
        Platform.exit();
    }

}
