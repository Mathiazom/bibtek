package bibtek.ui;

import java.io.IOException;

import bibtek.core.Book;
import bibtek.core.BookEntry;
import bibtek.core.BookReadingState;
import bibtek.core.User;
import bibtek.json.StorageHandler;
import bibtek.ui.utils.FxUtil;
import bibtek.ui.utils.ToastUtil;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public final class ViewBookController extends SceneChangerController {

    private static final String BOOK_IMAGE_PLACEHOLDER_LOCATION = "/bibtek/ui/images/book-cover-placeholder-orange.jpg";

    @FXML
    private Label bookEntryTitle;

    @FXML
    private Label bookEntryAuthor;

    @FXML
    private Label bookEntryYearPublished;

    @FXML
    private ImageView bookEntryImage;

    @FXML
    ComboBox<BookReadingState> bookReadingStateCombo;

    private BookEntry bookEntry;

    /**
     * Pass the current User and BookEntry to be viewed.
     *
     * @param b    to be viewed
     * @param user owner of book
     */
    @FXML
    public void update(final BookEntry b, final User user) {

        super.update(user);

        this.bookEntry = b;

        final Book book = bookEntry.getBook();

        bookEntryTitle.setText(book.getTitle());
        bookEntryAuthor.setText(book.getAuthor());

        final int yearPublished = book.getYearPublished();
        if (yearPublished != Book.YEAR_PUBLISHED_MISSING) {
            bookEntryYearPublished.setText(String.valueOf(yearPublished));
        }

        Image bookImage;
        try {
            bookImage = new Image(book.getImgPath());
        } catch (IllegalArgumentException | NullPointerException e) {
            bookImage = new Image(getClass().getResource(BOOK_IMAGE_PLACEHOLDER_LOCATION).toString());
        }
        bookEntryImage.setImage(bookImage);

        FxUtil.setUpReadingStateDropDown(bookReadingStateCombo);

        bookReadingStateCombo.getSelectionModel().select(bookEntry.getReadingState());

        bookReadingStateCombo.getSelectionModel().selectedItemProperty().addListener((o, a, newState) -> {

            bookEntry.setReadingState(newState);

            final StorageHandler storageHandler = new StorageHandler();
            try {
                storageHandler.putUser(getUser());
            } catch (IOException e) {
                final Stage stage1 = (Stage) addBookReadingStatusCombo.getScene().getWindow();
                ToastUtil.makeText(stage1, Toast.ToastState.ERROR,
                        "There was an error updating the reading status, try again later.");
                e.printStackTrace();
                return;
            }

            final Stage stage = (Stage) bookReadingStateCombo.getScene().getWindow();
            ToastUtil.makeToast(stage, Toast.ToastState.SUCCESS, "Reading state changed to '" + newState + "'");

        });

    }

    @FXML
    private void handleEditBook() {

        final Stage stage = (Stage) bookEntryTitle.getScene().getWindow();
        try {
            final EditBookController editBookController = (EditBookController) changeScene(stage,
                    "/bibtek/ui/fxml/EditBook.fxml");
            editBookController.update(bookEntry, getUser());
        } catch (IOException e) {
            ToastUtil.makeToast(stage, Toast.ToastState.ERROR, "There was an error when showing edit book page");
            e.printStackTrace();
        }

    }

    @FXML
    private void handleShowLibrary() {

        final Stage stage = (Stage) bookEntryTitle.getScene().getWindow();
        try {
            this.changeSceneAndUpdateUser(stage, "/bibtek/ui/fxml/Library.fxml");
        } catch (IOException e) {
            ToastUtil.makeToast(stage, Toast.ToastState.ERROR, "There was an error when showing your library");
            e.printStackTrace();
        }

    }

}
