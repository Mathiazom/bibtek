package bibtek.ui;

import java.io.IOException;

import bibtek.core.Book;
import bibtek.core.BookEntry;
import bibtek.core.User;
import bibtek.json.BooksAPIHandler;
import bibtek.json.ISBNUtils;
import bibtek.json.StorageHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public final class AddBookController extends BaseBookController {

    @FXML
    VBox addBookISBNContainer;

    @FXML
    DigitsField addBookISBNField;

    @FXML
    Label errorLabelISBN;

    @FXML
    @Override
    protected void initialize() {

        super.initialize();

        addBookISBNField.textProperty()
                .addListener((observable, oldValue, newValue) -> errorLabelISBN.setManaged(false));

    }

    @FXML
    private void handleAddBook() {

        final BookEntry bookEntry = new BookEntry(
                new Book(addBookTitleField.getText(), addBookAuthorField.getText(),
                        Integer.parseInt(addBookYearPublishedField.getText()), addBookImagePathField.getText()),
                addBookDatePicker.getValue(), addBookReadingStatusCombo.getValue()

        );

        final User user = getUser();

        user.getLibrary().addBookEntry(bookEntry);

        final StorageHandler storageHandler = new StorageHandler();
        storageHandler.notifyUserChanged(user);

        handleShowLibrary();

    }

    @FXML
    private void toggleShowISBNInput() {

        addBookISBNContainer.setManaged(!addBookISBNContainer.isManaged());

    }

    @FXML
    private void handleLoadBookFromISBNInput() {

        final String isbn = addBookISBNField.getText();

        if (!ISBNUtils.isValidISBN(isbn)) {
            errorLabelISBN.setText("Looks like an invalid ISBN :(");
            errorLabelISBN.setManaged(true);
            return;
        }

        final Book bookFromISBN = new BooksAPIHandler().fetchBook(isbn);
        if (bookFromISBN == null) {
            errorLabelISBN.setText("No results for that ISBN :(");
            errorLabelISBN.setManaged(true);
            return;
        }
        loadBookInput(bookFromISBN);

    }

    @FXML
    private void handleShowLibrary() {
        try {
            final Stage stage = (Stage) addBookAuthorField.getScene().getWindow();
            this.changeSceneAndUpdateUser(stage, "/bibtek/ui/Library.fxml");
        } catch (IOException e) {
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText("There was an error when showing your library");
            e.printStackTrace();
        }

    }

}
