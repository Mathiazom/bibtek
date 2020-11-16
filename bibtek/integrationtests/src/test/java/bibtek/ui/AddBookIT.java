package bibtek.ui;

import bibtek.core.*;
import bibtek.json.RemoteStorageHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AddBookIT extends ApplicationTest {

    private AddBookController controller;

    private Parent parent;

    private RemoteStorageHandler remoteStorageHandler;

    @Override
    public final void start(final Stage stage) throws Exception {

        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/bibtek/ui/AddBook.fxml"));
        this.parent = fxmlLoader.load();
        this.controller = fxmlLoader.getController();
        stage.setScene(new Scene(parent));
        stage.show();

        remoteStorageHandler = getRemoteStorageHandler();
        final User user = remoteStorageHandler.getUser("dante");
        controller.update(user);

    }

    @Test
    public void addBookTest(){

        final TextField addBookTitleField = (TextField) parent.lookup("#addBookTitleField");
        clickOn(addBookTitleField).write("Finnegans Wake", 1);

        final TextField addBookAuthorField = (TextField) parent.lookup("#addBookAuthorField");
        clickOn(addBookAuthorField).write("James Joyce", 1);

        final DigitsField addBookYearPublishedField = (DigitsField) parent.lookup("#addBookYearPublishedField");
        clickOn(addBookYearPublishedField).write("1939", 1);

        final TextField addBookImagePathField = (TextField) parent.lookup("#addBookImagePathField");
        addBookImagePathField.setText("http://books.google.com/books/content?id=FNMS7qOqRwEC&printsec=frontcover&img=1&zoom=1&source=gbs_api");

        final LocalDate startDate = LocalDate.of(2020, 9, 21);
        final DatePicker addBookDatePicker = (DatePicker) parent.lookup("#addBookDatePicker");
        addBookDatePicker.setValue(startDate);
        final TextField addBookDatePickerField = (TextField) parent.lookup("#addBookDatePickerField");
        clickOn(addBookDatePickerField)
                .type(KeyCode.DOWN) // 21.08.2020 -> 28.08.2020
                .type(KeyCode.RIGHT) // 28.08.2020 -> 29.08.2020
                .type(KeyCode.RIGHT) // 29.08.2020 -> 30.08.2020
                .type(KeyCode.ENTER) // Pick date
                .type(KeyCode.ESCAPE); // Hide datepicker

//        final ComboBox<BookReadingState> addBookReadingStatusCombo = (ComboBox<BookReadingState>) parent.lookup("#addBookReadingStatusCombo");
//        clickOn(addBookReadingStatusCombo)
//                .press(KeyCode.DOWN)
//                .press(KeyCode.ENTER); // Select second element

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

        final Set<BookEntry> bookEntries = remoteStorageHandler.getUser("dante").getLibrary().getBookEntries();

        boolean added = false;

        for (final BookEntry bookEntry : bookEntries){
            if(bookEntry.equals(expected)){
                added = true;
                break;
            }
        }

        assertTrue(added,"Book was not added");

    }

    private RemoteStorageHandler getRemoteStorageHandler() throws URISyntaxException {

        final String port = System.getProperty("bibtek.port");

        final URI remoteUri = new URI("http://localhost:" + port + "/bibtek/users/");

        return new RemoteStorageHandler(remoteUri);

    }

}


