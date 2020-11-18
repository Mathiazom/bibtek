package bibtek.ui;

import bibtek.core.*;
import bibtek.json.BooksAPIHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class AddBookTest extends ApplicationTest {

    /**
     * Sleep time between each typed character
     */
    private static final int WRITE_ROBOT_PAUSE_MILLIS = 1;

    /**
     * Sleep time between operations to simulate user behaviour
     */
    private static final int ROBOT_PAUSE_MS = 300;

    private Parent parent;
    private AddBookController controller;
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
        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/bibtek/ui/AddBook.fxml"));
        this.parent = fxmlLoader.load();
        this.controller = fxmlLoader.getController();
        controller.update(dummyUser()); // Dummy user
        stage.setScene(new Scene(parent));
        stage.show();

    }

    // TODO: Replace with dummy from rebase
    private static User dummyUser() {

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
     * Testing that the TextFields works properly, and tests if it creates the
     * Library and stores the books correctly.
     */
    @Test
    public void createBookEntryTest() {

        final TextField addBookTitleInput = (TextField) parent.lookup("#bookTitleInput");
        clickOn(addBookTitleInput).write("Finnegans Wake", WRITE_ROBOT_PAUSE_MILLIS);

        assertEquals("Finnegans Wake", addBookTitleInput.getText(), "Book Title should be \"Finnegans Wake\" ");

        final TextField addBookAuthorInput = (TextField) parent.lookup("#bookAuthorInput");
        clickOn(addBookAuthorInput).write("James Joyce", WRITE_ROBOT_PAUSE_MILLIS);
        assertEquals("James Joyce", addBookAuthorInput.getText(), "Book Author should be \"James Joyce\" ");

        final DigitsField addBookYearPublishedInput = (DigitsField) parent.lookup("#bookYearPublishedInput");
        clickOn(addBookYearPublishedInput).write("1939", WRITE_ROBOT_PAUSE_MILLIS);
        assertEquals(1939, addBookYearPublishedInput.getInputAsInt(), "Book Year should be " + 1939);

        final TextField addBookImagePathInput = (TextField) parent.lookup("#bookImagePathInput");
        clickOn(addBookImagePathInput);
        addBookImagePathInput.setText("http://books.google.com/books/content?id=FNMS7qOqRwEC&printsec=frontcover&img=1&zoom=1&source=gbs_api");
        assertEquals("http://books.google.com/books/content?id=FNMS7qOqRwEC&printsec=frontcover&img=1&zoom=1&source=gbs_api", addBookImagePathInput.getText(),
                "Book Cover image path should be \"http://books.google.com/books/content?id=FNMS7qOqRwEC&printsec=frontcover&img=1&zoom=1&source=gbs_api\"");

        final LocalDate startDate = LocalDate.of(2020, 9, 21);
        final LocalDate targetDate = LocalDate.of(2020, 9, 30);
        final DatePicker addBookDatePicker = (DatePicker) parent.lookup("#bookDatePicker");
        addBookDatePicker.setValue(startDate);
        final TextField addBookDatePickerDisplay = (TextField) parent.lookup("#bookDatePickerDisplay");
        clickOn(addBookDatePickerDisplay)
                .sleep(ROBOT_PAUSE_MS)
                .type(KeyCode.DOWN).sleep(ROBOT_PAUSE_MS) // 21.08.2020 -> 28.08.2020
                .type(KeyCode.RIGHT).sleep(ROBOT_PAUSE_MS) // 28.08.2020 -> 29.08.2020
                .type(KeyCode.RIGHT).sleep(ROBOT_PAUSE_MS) // 29.08.2020 -> 30.08.2020
                .type(KeyCode.ENTER).sleep(ROBOT_PAUSE_MS) // Pick date
                .type(KeyCode.ESCAPE); // Hide datepicker
        assertEquals(targetDate, addBookDatePicker.getValue(), "Book date acquired should be " + targetDate);

        final ComboBox<BookReadingState> addBookReadingStateCombo = (ComboBox<BookReadingState>) parent.lookup("#bookReadingStateCombo");
        clickOn(addBookReadingStateCombo)
                .sleep(ROBOT_PAUSE_MS)
                .press(KeyCode.DOWN)
                .sleep(ROBOT_PAUSE_MS)
                .press(KeyCode.ENTER); // Select second element
        assertEquals(BookReadingState.READING, addBookReadingStateCombo.getValue(),
                "BookReadingState should be READING");

    }

    /**
     * Make sure ISBN field is accessible and functional.
     */
    @Test
    public void validISBNSearchTest() {

        clickOn("#isbnButton").sleep(ROBOT_PAUSE_MS);

        // Make sure popup container is visible
        assertTrue(parent.lookup("#addBookISBNContainer").isManaged());

        // Make sure field is accessible and
        final DigitsField addBookISBNInput = (DigitsField) parent.lookup("#addBookISBNInput");

        // Make sure field is digits-only
        clickOn(addBookISBNInput).write("9rty7807xz6539Q486kj6", WRITE_ROBOT_PAUSE_MILLIS);
        assertEquals("9780765394866", addBookISBNInput.getText(), "ISBN input should be 9780765394866");

        clickOn("#addBookISBNButton");

        clickOn("#isbnButton");

        // Make sure data is retrieved and loaded
        assertEquals("Ender's Game", ((TextField) parent.lookup("#bookTitleInput")).getText(), "Book Title should be Ender's Game");
        assertEquals("Orson Scott Card", ((TextField) parent.lookup("#bookAuthorInput")).getText(), "Book Author should be Orson Scott Card");
        assertEquals(2017, ((DigitsField) parent.lookup("#bookYearPublishedInput")).getInputAsInt(), "Book Year should be " + 2017);
        assertEquals(
                "http://books.google.com/books/content?id=jaM7DwAAQBAJ&printsec=frontcover&img=1&zoom=1&source=gbs_api",
                ((TextField) parent.lookup("#bookImagePathInput")).getText(),
                "Book Cover image path should be \"http://books.google.com/books/content?id=jaM7DwAAQBAJ&printsec=frontcover&img=1&zoom=1&source=gbs_api\""
        );

    }

    @Test
    public void invalidISBNSearchTest() {

        clickOn("#isbnButton").sleep(ROBOT_PAUSE_MS);

        final DigitsField addBookISBNInput = (DigitsField) parent.lookup("#addBookISBNInput");
        clickOn(addBookISBNInput).write("1513852", WRITE_ROBOT_PAUSE_MILLIS);
        clickOn("#addBookISBNButton");
        FxTestUtil.assertToast(Toast.ToastState.INCORRECT, "Looks like an invalid ISBN", parent);

    }

    @Test
    public void noResultISBNSearchTest() {

        clickOn("#isbnButton").sleep(ROBOT_PAUSE_MS);

        final DigitsField addBookISBNInput = (DigitsField) parent.lookup("#addBookISBNInput");
        clickOn(addBookISBNInput).write("1234567890", WRITE_ROBOT_PAUSE_MILLIS);
        clickOn("#addBookISBNButton");
        FxTestUtil.assertToast(Toast.ToastState.INFO, "No results for that ISBN :(", parent);

    }

    /**
     * Testing if the year published field does not register letters.
     */
    @Test
    public void yearPublishedInputTest() {
        // testing if the year published field does not register letters
        final TextField addBookYearPublishedInput = (TextField) parent.lookup("#bookYearPublishedInput");
        addBookYearPublishedInput.setText("Hello123World");
        assertEquals("123", addBookYearPublishedInput.getText(),
                "This is a numbers only field, letters are not allowed");

    }

    /**
     * Make sure date picker text field displays same date as in date picker.
     */
    @Test
    public void datePickerDisplayInputTest() {

        final LocalDate timeStamp = LocalDate.of(2020, 9, 30);
        final DatePicker addBookDatePicker = (DatePicker) parent.lookup("#bookDatePicker");
        addBookDatePicker.setValue(timeStamp);
        final TextField addBookDatePickerDisplay = (TextField) parent.lookup("#bookDatePickerDisplay");
        assertEquals(
                addBookDatePicker.getConverter().toString(timeStamp),
                addBookDatePickerDisplay.getText(),
                "Book date acquired displayed in text field should be 30.09.2020"
        );

    }

    /**
     * Make sure all possible values are represented in dropdown.
     */
    @Test
    public void bookReadingStateDropdownTest() {

        final ComboBox<BookReadingState> addBookReadingStateCombo = (ComboBox<BookReadingState>) parent.lookup("#bookReadingStateCombo");
        assertEquals(List.of(BookReadingState.values()), List.of(addBookReadingStateCombo.getItems().toArray()));

    }

    /**
     * Make sure user can change their mind and go back to their library without adding a book.
     */
    @Test
    public void backToLibraryTest() {

        clickOn("#cancelButton");

        final AnchorPane libraryRoot = (AnchorPane) stage.getScene().getRoot().lookup("#libraryRoot");
        assertNotNull(libraryRoot);
        assertTrue(libraryRoot.isVisible());

    }

}
