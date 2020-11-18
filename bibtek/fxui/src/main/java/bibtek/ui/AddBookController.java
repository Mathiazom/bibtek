package bibtek.ui;

import java.io.IOException;

import bibtek.core.Book;
import bibtek.core.BookEntry;
import bibtek.core.User;
import bibtek.json.BooksAPIHandler;
import bibtek.json.ISBNUtils;
import bibtek.json.StorageHandler;
import bibtek.ui.utils.ToastUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public final class AddBookController extends BaseBookController {

    @FXML
    VBox addBookISBNContainer;

    @FXML
    DigitsField addBookISBNInput;

    @FXML
    Label errorLabelISBN;

    @FXML
    private void handleAddBook() {

        final BookEntry bookEntry = new BookEntry(
                new Book(bookTitleInput.getText(), bookAuthorInput.getText(),
                        Integer.parseInt(bookYearPublishedInput.getText()), bookImagePathInput.getText()),
                bookDatePicker.getValue(), bookReadingStateCombo.getValue()

        );

        final User user = getUser();

        user.getLibrary().addBookEntry(bookEntry);

        final StorageHandler storageHandler = new StorageHandler();
        try {
            storageHandler.notifyUserChanged(user);
        } catch (IOException e) {
            final Stage stage = (Stage) addBookAuthorField.getScene().getWindow();
            ToastUtil.makeText(stage, Toast.ToastState.ERROR,
                    "There was an error updating your library, try again later.");
            return;

        }

        handleShowLibrary();

    }

    @FXML
    private void toggleShowISBNInput() {

        addBookISBNContainer.setManaged(!addBookISBNContainer.isManaged());

    }

    @FXML
    private void handleLoadBookFromISBNInput() {

        final String isbn = addBookISBNInput.getText();

        if (!ISBNUtils.isValidISBN(isbn)) {
            final Stage stage = (Stage) addBookISBNContainer.getScene().getWindow();
            ToastUtil.makeToast(stage, Toast.ToastState.INCORRECT, "Looks like an invalid ISBN");
            return;
        }

        final Book bookFromISBN = new BooksAPIHandler().fetchBook(isbn);
        if (bookFromISBN == null) {
            final Stage stage = (Stage) addBookISBNContainer.getScene().getWindow();
            ToastUtil.makeToast(stage, Toast.ToastState.INFO, "No results for that ISBN :(");
            return;
        }
        loadBookInput(bookFromISBN);

    }

    @FXML
    private void handleShowLibrary() {
        final Stage stage = (Stage) bookAuthorInput.getScene().getWindow();
        try {
            this.changeSceneAndUpdateUser(stage, "/bibtek/ui/fxml/Library.fxml");
        } catch (IOException e) {
            ToastUtil.makeToast(stage, Toast.ToastState.ERROR, "There was an error when showing your library");
            e.printStackTrace();
            return;
        }

    }

}
