package bibtek.ui;

import bibtek.core.BookEntry;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class BookItemView extends VBox {

    @FXML
    private Label bookEntryTitle;

    @FXML
    private Label bookEntryAuthor;

    @FXML
    private Label bookEntryYearPublished;

    @FXML
    private ImageView bookEntryImage;

    public BookItemView(final BookEntry bookEntry) {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "/bibtek/ui/BookItemView.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        bookEntryTitle.setText(bookEntry.getBook().getTitle());
        bookEntryAuthor.setText(bookEntry.getBook().getAuthor());
        bookEntryYearPublished.setText(String.valueOf(bookEntry.getBook().getYearPublished()));
        bookEntryImage.setImage(new Image(bookEntry.getBook().getImgPath()));

    }


}
