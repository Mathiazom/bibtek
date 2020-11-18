package bibtek.ui;

import java.io.IOException;
import java.util.Optional;

import bibtek.core.Book;
import bibtek.core.BookEntry;
import bibtek.core.User;
import bibtek.json.StorageHandler;
import bibtek.ui.utils.ToastUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public final class EditBookController extends BaseBookController {

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

        final Book newBook = new Book(bookTitleInput.getText(), bookAuthorInput.getText(),
                Integer.parseInt(bookYearPublishedInput.getText()), bookImagePathInput.getText());

        bookEntry.setBook(newBook);
        bookEntry.setDateAcquired(bookDatePicker.getValue());
        bookEntry.setReadingState(bookReadingStateCombo.getValue());

        final StorageHandler storageHandler = new StorageHandler();
        try {
            storageHandler.putUser(getUser());
        } catch (IOException e) {
            final Stage stage = (Stage) bookAuthorInput.getScene().getWindow();
            ToastUtil.makeToast(stage, Toast.ToastState.ERROR,
                    "There was an error updating your library, try again later.");
            e.printStackTrace();
            return;
        }

        handleShowBookView();

    }

    /**
     * Handle request to delete book by first asking for confirmation.
     */
    @FXML
    public void handleDeleteBook() {

        final Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Confirm book deletion");
        alert.setHeaderText("Are you sure you want to delete this book. This action cannot be undone");

        final Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        alertStage.getIcons().add(new Image("/bibtek/ui/images/bibtek_round.png"));

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
        try {
            storageHandler.putUser(getUser());
        } catch (IOException e) {
            final Stage stage = (Stage) bookDatePicker.getScene().getWindow();
            ToastUtil.makeToast(stage, Toast.ToastState.ERROR, "There was an error deleting the book, try again later.");
            e.printStackTrace();
            return;
        }

        handleShowLibrary();

    }

    /**
     * Show page for single book.
     */
    public void handleShowBookView() {

        final Stage stage = (Stage) bookDatePicker.getScene().getWindow();
        try {
            final ViewBookController editBookController = (ViewBookController) changeScene(stage,
                    "/bibtek/ui/fxml/ViewBook.fxml");
            editBookController.update(bookEntry, getUser());
        } catch (IOException e) {
            ToastUtil.makeToast(stage, Toast.ToastState.ERROR, "There was an error when showing book page");
            e.printStackTrace();
            return;
        }

    }

    /**
     * Pass the current User and BookEntry to be edited.
     *
     * @param b    to be edited
     * @param user owner of book
     */
    public void update(final BookEntry b, final User user) {

        super.update(user);

        this.bookEntry = b;

        loadBookEntryInput();

    }

    private void loadBookEntryInput() {

        loadBookInput(bookEntry.getBook());

        bookDatePicker.setValue(bookEntry.getDateAcquired());

        bookReadingStateCombo.getSelectionModel().select(bookEntry.getReadingState());

    }

    private void handleShowLibrary() {

        final Stage stage = (Stage) bookDatePicker.getScene().getWindow();
        try {
            this.changeSceneAndUpdateUser(stage, "/bibtek/ui/fxml/Library.fxml");
        } catch (IOException e) {
            ToastUtil.makeToast(stage, Toast.ToastState.ERROR, "There was an error when showing your library");
            e.printStackTrace();
            return;
        }

    }
}
