package bibtek.ui;

import bibtek.core.Book;
import bibtek.core.BookEntry;
import bibtek.core.BookReadingState;
import bibtek.core.User;
import bibtek.json.StorageHandler;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public final class AddBookController {

    @FXML
    Button addBookButton;

    @FXML
    TextField addBookTitleField;

    @FXML
    TextField addBookAuthorField;

    @FXML
    TextField addBookYearPublishedField;

    @FXML
    DatePicker addBookDatePicker;

    @FXML
    ComboBox<BookReadingState> addBookReadingStatusCombo;

    @FXML
    Button libraryButton;

    @FXML
    Label errorLabel;

    private User user;

    @FXML
    private void initialize() {

        addBookReadingStatusCombo.setItems(FXCollections.observableArrayList(BookReadingState.values()));
        addBookReadingStatusCombo.getSelectionModel().selectFirst();

        // Make sure year input is only digits
        addBookYearPublishedField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                addBookYearPublishedField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

    }

    @FXML
    private void handleAddBook() {

        final BookEntry bookEntry = new BookEntry(
                new Book(addBookTitleField.getText(), addBookAuthorField.getText(),
                        Integer.parseInt(addBookYearPublishedField.getText())),
                addBookDatePicker.getValue(), addBookReadingStatusCombo.getValue()

        );

        user.getLibrary().addBookEntry(bookEntry);
        try {
            StorageHandler storageHandler = new StorageHandler();
            storageHandler.updateUser(user);
        } catch (IOException e) {
            errorLabel.setText("Was not able to update the user library");
            errorLabel.setTextFill(Color.RED);
        }

        handleShowLibrary();

    }

    @FXML
    private void handleShowLibrary() {
        LibraryController controller;
        final Stage stage = (Stage) libraryButton.getScene().getWindow();
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
        controller.update(user);

    }

    /**
     * Updating the user of this controller.
     *
     * @param u
     */
    public void update(final User u) {
        this.user = u;
    }

    /**
     * Get method for user.
     *
     * @return the user.
     */
    public User getUser() {
        return this.user;
    }

}
