package bibtek.ui;

import java.io.IOException;
import java.util.Optional;

import bibtek.core.Book;
import bibtek.core.BookEntry;
import bibtek.core.BookReadingState;
import bibtek.core.User;
import bibtek.json.StorageHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;

public final class EditBookController extends BaseBookController {

    @FXML
    DatePicker addBookDatePicker;

    @FXML
    ComboBox<BookReadingState> addBookReadingStatusCombo;

    private BookEntry bookEntry;

    @Override
    protected void initialize() {

        super.initialize();

    }

    /**
     * Save changes made in editor.
     */
    @FXML
    public void handleConfirmEditBook() {

        final Book newBook = new Book(addBookTitleField.getText(), addBookAuthorField.getText(),
                Integer.parseInt(addBookYearPublishedField.getText()), addBookImagePathField.getText());

        bookEntry.setBook(newBook);
        bookEntry.setDateAcquired(addBookDatePicker.getValue());
        bookEntry.setReadingState(addBookReadingStatusCombo.getValue());

        final StorageHandler storageHandler = new StorageHandler();
        storageHandler.putUser(getUser());

        handleShowBookView();

    }

    /**
     * Handle request to delete book by first asking for confirmation.
     */
    @FXML
    public void handleDeleteBook() {

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Confirm book deletion");
        alert.setHeaderText("Are you sure you want to delete this book. This acton cannot be undone");

        final ButtonType deleteButtonType = new ButtonType("Delete", ButtonData.RIGHT);

        alert.getButtonTypes().setAll(ButtonType.CANCEL, deleteButtonType);

        final Button deleteButton = (Button) alert.getDialogPane().lookupButton(deleteButtonType);
        deleteButton.setDefaultButton(true);

        final Optional<ButtonType> result = alert.showAndWait();
        if (result.isEmpty() || result.get() != deleteButtonType) {
            return;
        }

        User user = getUser();
        user.getLibrary().removeBookEntry(bookEntry);
        StorageHandler storageHandler = new StorageHandler();
        storageHandler.putUser(getUser());

        handleShowLibrary();

    }

    /**
     * Show page for single book.
     */
    @FXML
    public void handleShowBookView() {

        final Stage stage = (Stage) addBookDatePicker.getScene().getWindow();
        try {
            final ViewBookController editBookController = (ViewBookController) changeScene(stage, "/bibtek/ui/ViewBook.fxml");
            editBookController.update(bookEntry, getUser());
        } catch (IOException e) {
            ToastUtil.makeText(stage, Toast.ToastState.ERROR, "There was an error when showing book page");
            e.printStackTrace();
        }

    }

    /**
     * Pass the current User and BookEntry to be edited.
     * @param b to be edited
     * @param user owner of book
     */
    public void update(final BookEntry b, final User user) {

        super.update(user);

        this.bookEntry = b;

        loadBookEntryInput();

    }

    private void loadBookEntryInput() {

        loadBookInput(bookEntry.getBook());

        addBookDatePicker.setValue(bookEntry.getDateAcquired());

        addBookReadingStatusCombo.getSelectionModel().select(bookEntry.getReadingState());

    }

    private void handleShowLibrary() {

        final Stage stage = (Stage) addBookDatePicker.getScene().getWindow();
        try {
            this.changeSceneAndUpdateUser(stage, "/bibtek/ui/Library.fxml");
        } catch (IOException e) {
            ToastUtil.makeText(stage, Toast.ToastState.ERROR, "There was an error when showing your library");
            e.printStackTrace();
        }

    }
}
