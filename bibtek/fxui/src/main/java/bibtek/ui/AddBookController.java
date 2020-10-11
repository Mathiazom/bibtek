package bibtek.ui;

import bibtek.core.Book;
import bibtek.core.BookEntry;
import bibtek.core.BookReadingState;
import bibtek.core.Library;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;

public final class AddBookController {

    @FXML
    TextField addBookTitleField;

    @FXML
    TextField addBookAuthorField;

    @FXML
    TextField addBookYearPublishedField;

    @FXML
    TextField addBookImagePathField;

    @FXML
    DatePicker addBookDatePicker;

    @FXML
    ComboBox<BookReadingState> addBookReadingStatusCombo;

    private Library library;

    @FXML
    private void initialize() {

        library = new Library();

        addBookReadingStatusCombo.setItems(FXCollections.observableArrayList(BookReadingState.values()));
        addBookReadingStatusCombo.setConverter(new StringConverter<>() {
            @Override
            public String toString(final BookReadingState readingState) {
                return readingState.toString();
            }

            @Override
            public BookReadingState fromString(final String s) {
                return BookReadingState.fromString(s);
            }
        });
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
                        Integer.parseInt(addBookYearPublishedField.getText()),
                        addBookImagePathField.getText()),
                addBookDatePicker.getValue(), addBookReadingStatusCombo.getValue()

        );

        library.addBookEntry(bookEntry);

        handleShowLibrary();

    }

    @FXML
    private void handleShowLibrary() {

        final Stage stage = (Stage) addBookTitleField.getScene().getWindow();

        final Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/bibtek/ui/Library.fxml"));
            final Scene scene = new Scene(root);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * @return the current library
     */
    public Library getLibrary() {
        return this.library;
    }

}
