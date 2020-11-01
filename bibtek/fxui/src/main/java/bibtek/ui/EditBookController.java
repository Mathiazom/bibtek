package bibtek.ui;

import java.io.IOException;
import java.util.Optional;

import bibtek.core.Book;
import bibtek.core.BookEntry;
import bibtek.core.User;
import bibtek.json.StorageHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public final class EditBookController extends BaseBookController {

    private BookEntry bookEntry;

    @Override
    protected void initialize() {

        super.initialize();

    }

    @FXML
    public void handleConfirmEditBook() {

        final Book newBook = new Book(addBookTitleField.getText(), addBookAuthorField.getText(),
                Integer.parseInt(addBookYearPublishedField.getText()), addBookImagePathField.getText());

        bookEntry.setBook(newBook);
        bookEntry.setDateAcquired(addBookDatePicker.getValue());
        bookEntry.setReadingState(addBookReadingStatusCombo.getValue());

        try {
            StorageHandler storageHandler = new StorageHandler();
            storageHandler.updateUser(getUser());
        } catch (IOException e) {
            errorLabel.setText("Was not able to update the user library");
            errorLabel.setTextFill(Color.RED);
        }

        handleShowBookView();

    }

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
        if (result.get() != deleteButtonType) {
            return;
        }

        User user = getUser();
        user.getLibrary().removeBookEntry(bookEntry);
        try {
            StorageHandler storageHandler = new StorageHandler();
            storageHandler.updateUser(getUser());
        } catch (IOException e) {
            errorLabel.setText("Was not able to update the user library");
            errorLabel.setTextFill(Color.RED);
        }

        handleShowLibrary();

    }

    @FXML
    public void handleShowBookView() {

        ViewBookController controller;

        final Stage stage = (Stage) addBookTitleField.getScene().getWindow();
        final Parent root;
        try {
            final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/bibtek/ui/ViewBook.fxml"));
            root = fxmlLoader.load();
            final Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            controller = fxmlLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        controller.update(bookEntry, getUser());

    }

    public void update(final BookEntry b, final User user) {

        super.update(user);

        this.bookEntry = b;

        loadBookInput(bookEntry.getBook());

    }

    private void handleShowLibrary() {

        final LibraryController controller;

        final Stage stage = (Stage) addBookTitleField.getScene().getWindow();

        final Parent root;
        try {
            final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/bibtek/ui/Library.fxml"));
            root = fxmlLoader.load();
            final Scene scene = new Scene(root);
            stage.setScene(scene);
            // stage.show();
            controller = fxmlLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        controller.update(getUser());

    }
}
