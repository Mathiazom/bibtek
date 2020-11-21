package bibtek.ui;

import bibtek.core.Book;
import bibtek.core.BookEntry;
import bibtek.core.BookReadingState;
import bibtek.json.RemoteStorageHandler;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ScenarioIT extends ApplicationTest {

    // Stage for the current window (stays the same through scenario)
    private Stage stage;

    // Parent of current page (changes as user moves through scenario)
    private Parent parent;

    // Used to verify changes are saved to server
    private RemoteStorageHandler remoteStorageHandler;

    @Override
    public final void start(final Stage stage) throws Exception {

        this.stage = stage;

        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/bibtek/ui/fxml/Login.fxml"));
        this.parent = fxmlLoader.load();
        fxmlLoader.getController();
        stage.setScene(new Scene(parent));
        stage.show();

        remoteStorageHandler = getRemoteStorageHandler();

    }

    private RemoteStorageHandler getRemoteStorageHandler() throws URISyntaxException {

        return new RemoteStorageHandler();

    }

    /**
     * Testing a full scenario, moving through multiple pages.
     */
    @Test
    public void userScenarioTest() {

        createUserTest();

        loginUserTest();

        addBookTest();

        viewBookTest();

        editBookTest();

        logOffTest();

    }


    private void createUserTest(){

        clickOn("#createNewUserLabel");

        this.parent = stage.getScene().getRoot();

        assertNotNull(parent.lookup("#createUserRoot"),"Failed to navigate to create user page");

        final TextField userNameInput = (TextField) parent.lookup("#userNameInput");
        final TextField userNameConfirmInput = (TextField) parent.lookup("#userNameConfirmInput");

        clickOn(userNameInput).write("heinrich");
        clickOn(userNameConfirmInput).write("heinrich");

        final DigitsField ageInput = (DigitsField) parent.lookup("#ageInput");
        clickOn(ageInput).write("56");

        clickOn("#confirmCheckbox");

        clickOn("#createUserButton");

        this.parent = stage.getScene().getRoot();

        assertNotNull(remoteStorageHandler.getUser("heinrich"), "Failed to create user");

        assertNotNull(parent.lookup("#loginRoot"),"Failed to return to login page");

    }


    private void loginUserTest() {

        clickOn("#userNameInput").write("heinrich");
        clickOn("#logInButton");

        this.parent = stage.getScene().getRoot();

        assertNotNull(parent.lookup("#libraryRoot"));

        final ListView<BookItemView> libraryList = (ListView<BookItemView>) parent.lookup("#libraryList");
        assertNotNull(libraryList, "Library list is not displayed");

    }

    private void addBookTest(){

        clickOn("#addBookButton");

        this.parent = stage.getScene().getRoot();

        final TextField addBookTitleField = (TextField) parent.lookup("#bookTitleInput");
        clickOn(addBookTitleField).write("Finnegans Wake", 1);

        final TextField addBookAuthorField = (TextField) parent.lookup("#bookAuthorInput");
        clickOn(addBookAuthorField).write("James Joyce", 1);

        final DigitsField addBookYearPublishedField = (DigitsField) parent.lookup("#bookYearPublishedInput");
        clickOn(addBookYearPublishedField).write("1939", 1);

        final TextField addBookImagePathField = (TextField) parent.lookup("#bookImagePathInput");
        addBookImagePathField.setText("http://books.google.com/books/content?id=FNMS7qOqRwEC&printsec=frontcover&img=1&zoom=1&source=gbs_api");

        final LocalDate startDate = LocalDate.of(2020, 9, 21);
        final DatePicker addBookDatePicker = (DatePicker) parent.lookup("#bookDatePicker");
        addBookDatePicker.setValue(startDate);
        final TextField addBookDatePickerField = (TextField) parent.lookup("#bookDatePickerDisplay");
        clickOn(addBookDatePickerField)
                .type(KeyCode.DOWN) // 21.08.2020 -> 28.08.2020
                .type(KeyCode.RIGHT) // 28.08.2020 -> 29.08.2020
                .type(KeyCode.RIGHT) // 29.08.2020 -> 30.08.2020
                .type(KeyCode.ENTER) // Pick date
                .type(KeyCode.ESCAPE); // Hide datepicker

        clickOn("#confirmAddBookButton");

        final BookEntry expected = new BookEntry(
                new Book(
                        "Finnegans Wake",
                        "James Joyce",
                        1939,
                        "http://books.google.com/books/content?id=FNMS7qOqRwEC&printsec=frontcover&img=1&zoom=1&source=gbs_api"
                ),
                LocalDate.of(2020, 9, 30),
                BookReadingState.NOT_STARTED
        );

        final Set<BookEntry> bookEntries = remoteStorageHandler.getUser("heinrich").getLibrary().getBookEntries();

        boolean added = false;

        for (final BookEntry bookEntry : bookEntries){
            if(bookEntry.equals(expected)){
                added = true;
                break;
            }
        }

        assertTrue(added,"Book was not added");

        this.parent = stage.getScene().getRoot();

        assertNotNull(parent.lookup("#libraryRoot"), "Library not displayed after add book");

    }


    private void viewBookTest(){

        final ListView<BookItemView> libraryList = (ListView<BookItemView>) parent.lookup("#libraryList");
        assertNotNull(libraryList, "Library list is not displayed");

        final BookItemView firstBook = libraryList.getItems().get(0);
        assertNotNull(firstBook, "First book is not displayed");
        clickOn(firstBook);

        this.parent = stage.getScene().getRoot();

        assertNotNull(parent.lookup("#viewBookRoot"), "Failed to view book on list click");

        final ComboBox<BookReadingState> readingStateCombo = (ComboBox<BookReadingState>) parent.lookup("#bookReadingStateCombo");

        clickOn(readingStateCombo)
                .type(KeyCode.DOWN).sleep(300)
                .type(KeyCode.ENTER).sleep(300);

        final BookReadingState readingState = readingStateCombo.getValue();

        clickOn("#backButton").sleep(300);

        this.parent = stage.getScene().getRoot();
        final ListView<BookItemView> libraryList2 = (ListView<BookItemView>) parent.lookup("#libraryList");
        final BookItemView firstBook2 = libraryList2.getItems().get(0);
        clickOn(firstBook2).sleep(300);

        assertEquals(readingState, readingStateCombo.getValue());

    }

    private void editBookTest(){

        clickOn("#editBookButton");

        this.parent = stage.getScene().getRoot();

        assertNotNull(parent.lookup("#editBookRoot"), "Failed to load edit book page");

        final TextField bookTitleInput = (TextField) parent.lookup("#bookTitleInput");

        clickOn(bookTitleInput).write(" (Anniversary Edition)");

        clickOn("#confirmEditBookButton");

        this.parent = stage.getScene().getRoot();

        assertNotNull(parent.lookup("#viewBookRoot"), "Failed to view book on after edit");

        final Label bookEntryTitle = (Label) parent.lookup("#bookEntryTitle");
        assertEquals("Finnegans Wake (Anniversary Edition)", bookEntryTitle.getText(), "Book title was not edited");

        clickOn("#backButton");

        this.parent = stage.getScene().getRoot();
        assertNotNull(parent.lookup("#libraryRoot"), "Failed to return to library");

    }

    private void logOffTest(){

        clickOn("#openSettingsButton");

        this.parent = stage.getScene().getRoot();
        assertNotNull(parent.lookup("#settingsRoot"), "Failed to open settings");

        clickOn("#logOffButton");

        this.parent = stage.getScene().getRoot();
        assertNotNull(parent.lookup("#loginRoot"), "Failed to log off");

    }


}
