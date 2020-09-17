package bibtek.ui;

import bibtek.core.BookEntry;
import bibtek.core.Library;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

public class BookListController {

    @FXML
    ListView<String> bookListView;

    @FXML
    Button addBookButton;


    @FXML
    private void initialize() {

        final Library library = new Library();

        final Set<BookEntry> bookEntrySet = library.getBookEntries();

        // Display book entries in list view
        bookListView.getItems().setAll(
                // Convert list of book entries to list of strings
                bookEntrySet.stream().map(BookEntry::toPrintString).collect(Collectors.toList())
        );

    }

    @FXML
    private void handleAddBook() {

        final Stage stage = (Stage) addBookButton.getScene().getWindow();

        final Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/bibtek/ui/AddBook.fxml"));
            final Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}

