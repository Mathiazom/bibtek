package bibtek.ui;

import bibtek.core.BookEntry;
import bibtek.core.User;
import bibtek.ui.utils.ToastUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

public final class LibraryController extends SceneChangerController {

    @FXML
    ListView<BookItemView> libraryList;

    @FXML
    Button addBookButton;

    @FXML
    Button openSettingsButton;

    @FXML
    private void handleAddBook() {
        final Stage stage = (Stage) addBookButton.getScene().getWindow();
        try {
            this.changeSceneAndUpdateUser(stage, "/bibtek/ui/fxml/AddBook.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void viewBook(final BookEntry bookEntry) {

        final Stage stage = (Stage) addBookButton.getScene().getWindow();
        try {
            final ViewBookController editBookController = (ViewBookController) changeScene(stage,
                    "/bibtek/ui/fxml/ViewBook.fxml");
            editBookController.update(bookEntry, getUser());
        } catch (IOException e) {
            ToastUtil.makeText(stage, Toast.ToastState.ERROR, "There was an error when showing view book page");
            e.printStackTrace();
        }

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
                // Convert list of book entries to list of BookItemViews
                // with attached click listener
                bookEntrySet.stream().map(bookEntry -> {
                    final BookItemView bookItemView = new BookItemView(bookEntry);
                    bookItemView.setOnMouseClicked((event) -> viewBook(bookEntry));
                    return bookItemView;
                }).collect(Collectors.toList()));
    }

    /**
     * Opens the settings page by pressing the openSettingsButton.
     */
    public void openSettings() {
        final Stage stage = (Stage) openSettingsButton.getScene().getWindow();
        try {
            this.changeSceneAndUpdateUser(stage, "/bibtek/ui/Settings.fxml");
        } catch (IOException e) {
            ToastUtil.makeText(stage, Toast.ToastState.ERROR, "There was an error when showing Settings");
            e.printStackTrace();

        }
    }

}

