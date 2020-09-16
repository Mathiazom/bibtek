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
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;

public class AddBookController {
    private final static Library library = new Library();

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
    Button bookListButton;



    @FXML
    private void initialize(){
        addBookReadingStatusCombo.setItems(FXCollections.observableArrayList(BookReadingState.values()));
        addBookReadingStatusCombo.getSelectionModel().selectFirst();

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
                Date.from(Instant.from(addBookDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()))),
                addBookReadingStatusCombo.getValue()

        );

        library.addBookEntry(bookEntry);
    }


    @FXML
    private void handleShowBookList(){

        final Stage stage = (Stage) bookListButton.getScene().getWindow();

        final Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/bibtek/ui/BookList.fxml"));
            final Scene scene = new Scene(root);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



}
