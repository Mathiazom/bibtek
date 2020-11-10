package bibtek.ui;

import bibtek.core.BookEntry;
import bibtek.core.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.util.Set;
import java.util.stream.Collectors;

public final class LibraryController extends SceneChangerController {

    @FXML
    ListView<BookItemView> libraryList;

    @FXML
    Button addBookButton;

    @FXML
    private void handleAddBook() {
        final Stage stage = (Stage) addBookButton.getScene().getWindow();
        this.changeSceneAndUpdateUser(stage, "/bibtek/ui/AddBook.fxml");

    }

    /**
     * Updates who the user is, and displays the updated library. Overrides the
     * standard SceneChangerControllers update(User) method.
     *
     * @param u the updated user
     */
    @Override
    public void update(final User u) {
        super.update(u);
        final Set<BookEntry> bookEntrySet = this.getUser().getLibrary().getBookEntries();

        // Display book entries in list view
        libraryList.getItems().setAll(
                // Convert list of book entries to list of strings
                bookEntrySet.stream().map(BookItemView::new).collect(Collectors.toList()));
    }

}
