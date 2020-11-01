package bibtek.ui;

import java.io.IOException;

import bibtek.core.Book;
import bibtek.core.BookEntry;
import bibtek.core.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public final class ViewBookController {

    private static final String BOOK_IMAGE_PLACEHOLDER_LOCATION = "/bibtek/ui/book-cover-placeholder-orange.jpg";

    @FXML
    private Label bookEntryTitle;

    @FXML
    private Label bookEntryAuthor;

    @FXML
    private Label bookEntryYearPublished;

    @FXML
    private ImageView bookEntryImage;

    private User user;

    private BookEntry bookEntry;

    @FXML
    public void update(final BookEntry b, final User u) {

        this.user = u;
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
            bookImage = new Image(BOOK_IMAGE_PLACEHOLDER_LOCATION);
        }
        bookEntryImage.setImage(bookImage);

    }

    @FXML
    private void handleEditBook() {

        final EditBookController controller;

        final Stage stage = (Stage) bookEntryTitle.getScene().getWindow();

        final Parent root;
        try {
            final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/bibtek/ui/EditBook.fxml"));
            root = fxmlLoader.load();
            final Scene scene = new Scene(root);
            stage.setScene(scene);
            // stage.show();
            controller = fxmlLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        controller.update(bookEntry, user);

    }

    @FXML
    private void handleShowLibrary() {

        final LibraryController controller;

        final Stage stage = (Stage) bookEntryTitle.getScene().getWindow();

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

}
