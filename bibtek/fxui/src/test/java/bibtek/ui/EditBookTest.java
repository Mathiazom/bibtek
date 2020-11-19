package bibtek.ui;

import bibtek.core.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

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

    private User user;
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
        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/bibtek/ui/fxml/EditBook.fxml"));
        parent = fxmlLoader.load();
        this.controller = fxmlLoader.getController();
        this.user = TestConstants.userDante();
        this.bookEntry = user.getLibrary().getBookEntries().iterator().next();
        controller.update(bookEntry, user); // Dummy user
        stage.setScene(new Scene(parent));
        stage.show();

    }

//    @Test
//    public void deleteBookTest(){
//
//        clickOn("#deleteBookButton");
//
//        sleep(1000);
//
//        final Stage alertDialog = FxTestUtil.getTopModalStage(this);
//        assertNotNull(alertDialog);
//
//        final DialogPane dialogPane = (DialogPane) alertDialog.getScene().getRoot();
//        assertEquals("Confirm book deletion", dialogPane.getHeaderText());
//        assertEquals("Are you sure you want to delete this book. This action cannot be undone", dialogPane.getContentText());
//
//        final Button cancelButton = (Button) dialogPane.lookupButton(ButtonType.CANCEL);
//        clickOn(cancelButton);
//        assertTrue(user.getLibrary().getBookEntries().contains(bookEntry));
//
//        final Button deleteButton = (Button) dialogPane.lookupButton(new ButtonType("Delete", ButtonBar.ButtonData.RIGHT));
//        clickOn(deleteButton);
//        assertFalse(user.getLibrary().getBookEntries().contains(bookEntry));
//
//    }

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
                .withHeader("Accept", equalTo("application/json"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("true")
                )
        );

        clickOn("#confirmEditBookButton");

        assertEquals(BookReadingState.READING, bookEntry.getReadingState(),"BookReadingState should be READING after editing");

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

        final AnchorPane viewBookRoot = (AnchorPane) stage.getScene().getRoot().lookup("#viewBookRoot");
        assertNotNull(viewBookRoot);
        assertTrue(viewBookRoot.isVisible());

    }

}
