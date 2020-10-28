package bibtek.ui;

import bibtek.core.Book;
import bibtek.core.BookEntry;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class BookItemView extends VBox {

    private static final String BOOK_IMAGE_PLACEHOLDER_LOCATION = "/bibtek/ui/book-cover-placeholder-orange.jpg";

    @FXML
    private Label bookEntryTitle;

    @FXML
    private Label bookEntryAuthor;

    @FXML
    private Label bookEntryYearPublished;

    @FXML
    private ImageView bookEntryImage;

    public BookItemView(final BookEntry bookEntry) {

        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "/bibtek/ui/BookItemView.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        final Book book = bookEntry.getBook();

        bookEntryTitle.setText(book.getTitle());
        bookEntryAuthor.setText(book.getAuthor());

        final int yearPublished = book.getYearPublished();
        if (yearPublished != Book.YEAR_PUBLISHED_MISSING) {
            bookEntryYearPublished.setText(String.valueOf(yearPublished));
        }

        Image bookImage;
        try {
            bookImage = new Image(bookEntry.getBook().getImgPath());
        } catch (IllegalArgumentException | NullPointerException e) {
            bookImage = new Image(BOOK_IMAGE_PLACEHOLDER_LOCATION);
        }
        bookEntryImage.setImage(bookImage);

    }


}
