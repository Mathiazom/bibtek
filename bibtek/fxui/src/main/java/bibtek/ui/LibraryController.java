package bibtek.ui;

import bibtek.core.BookEntry;
import bibtek.core.User;
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

public final class LibraryController {

    @FXML
    ListView<BookItemView> libraryList;

    @FXML
    Button addBookButton;

    private User user;

    @FXML
    private void handleAddBook() {
        AddBookController controller;
        final Stage stage = (Stage) addBookButton.getScene().getWindow();
        final Parent root;
        try {
            final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/bibtek/ui/AddBook.fxml"));
            root = fxmlLoader.load();
            final Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            controller = fxmlLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        controller.update(user);

    }

    /**
     * Updates who the user is, and displays the updated library.
     *
     * @param u the updated user
     */
    public void update(final User u) {
        this.user = u;
        final Set<BookEntry> bookEntrySet = user.getLibrary().getBookEntries();

        // Display book entries in list view
        libraryList.getItems().setAll(
                // Convert list of book entries to list of strings
                bookEntrySet.stream().map(BookItemView::new).collect(Collectors.toList())
        );
    }

}
