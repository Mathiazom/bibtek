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
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;


public class AddBookController {

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


    private Library library;

    @FXML
    private void initialize(){

        library = new Library();

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
    private void handleAddBook(){

        final BookEntry bookEntry = new BookEntry(
                new Book(
                        addBookTitleField.getText(),
                        addBookAuthorField.getText(),
                        Integer.parseInt(addBookYearPublishedField.getText())
                ),
                addBookDatePicker.getValue(),
                addBookReadingStatusCombo.getValue()

        );

        library.addBookEntry(bookEntry);

        handleShowLibrary();

    }


    @FXML
    private void handleShowLibrary(){

        final Stage stage = (Stage) libraryButton.getScene().getWindow();

        final Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/bibtek/ui/Library.fxml"));
            final Scene scene = new Scene(root);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Library getLibrary(){
        return this.library;
    }



}
