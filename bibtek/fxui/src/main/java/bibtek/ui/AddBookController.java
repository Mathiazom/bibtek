package bibtek.ui;

import java.io.IOException;
import java.util.List;

import bibtek.core.Book;
import bibtek.core.BookEntry;
import bibtek.core.User;
import bibtek.json.BooksAPIHandler;
import bibtek.json.StorageHandler;
import bibtek.json.UserMapHandler;
import bibtek.ui.Toast.ToastState;
import bibtek.ui.utils.ToastUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public final class AddBookController extends BaseBookController {

    @FXML
    VBox searchBookContainer;

    @FXML
    VBox suggestionContainer;

    @FXML
    TextField searchBookField;

    @FXML
    Label errorLabelSearch;

    @FXML
    TextField addBookTitleField;

    @FXML
    private void handleAddBook() {

        final BookEntry bookEntry;
        try {
            bookEntry = new BookEntry(
                    new Book(bookTitleInput.getText(), bookAuthorInput.getText(),
                            Integer.parseInt(bookYearPublishedInput.getText()), bookImagePathInput.getText()),
                    bookDatePicker.getValue(), bookReadingStateCombo.getValue());
        } catch (Exception e) {
            Stage stage = (Stage) searchBookContainer.getScene().getWindow();
            ToastUtil.makeToast(stage, ToastState.INCORRECT, "You must fill out all fields except \'cover image\'");
            return;
        }

        final User user = getUser();

        user.getLibrary().addBookEntry(bookEntry);

        final StorageHandler storageHandler = new StorageHandler();
        final UserMapHandler.Status updateStatus = storageHandler.notifyUserChanged(user);
        if (!updateStatus.isOk()) {
            user.getLibrary().removeBookEntry(bookEntry);
            final Stage stage = (Stage) searchBookContainer.getScene().getWindow();
            ToastUtil.makeToast(stage, Toast.ToastState.ERROR, "There was an error updating your library",
                    updateStatus.toString());
            return;
        }

        handleShowLibrary();

    }

    @FXML
    private void toggleShowSearchInput() {

        searchBookContainer.setManaged(!searchBookContainer.isManaged());

    }

    @FXML
    private void handleSearchBook() {

        final String input = searchBookField.getText();

        final List<Book> bookSuggestions = new BooksAPIHandler().searchForBooks(input);

        suggestionContainer.getChildren().clear();

        if (bookSuggestions == null) {
            final Stage stage = (Stage) searchBookContainer.getScene().getWindow();
            ToastUtil.makeToast(stage, Toast.ToastState.INFO, "No results for that search :(");
            return;
        }

        bookSuggestions.forEach(book -> {
            Label suggestion = new Label(book.getTitle() + " by " + book.getAuthor());
            suggestion.setOnMouseClicked((agent) -> {
                loadBookInput(book);
                toggleShowSearchInput();
            });
            suggestionContainer.getChildren().add(suggestion);
        });

        // if (!ISBNUtils.isValidISBN(isbn)) {
        // errorLabelSearch.setText("Looks like an invalid ISBN :(");
        // errorLabelSearch.setManaged(true);
        // return;
        // }

    }

    @FXML
    private void handleShowLibrary() {
        final Stage stage = (Stage) bookAuthorInput.getScene().getWindow();
        try {
            this.changeSceneAndUpdateUser(stage, "/bibtek/ui/fxml/Library.fxml");
        } catch (IOException e) {
            ToastUtil.makeToast(stage, Toast.ToastState.ERROR, "There was an error when showing your library");
            e.printStackTrace();
        }

    }

}
