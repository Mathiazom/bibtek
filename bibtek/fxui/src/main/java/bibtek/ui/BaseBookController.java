package bibtek.ui;

import bibtek.core.Book;
import bibtek.core.BookReadingState;
import bibtek.ui.utils.FxUtil;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public abstract class BaseBookController extends SceneChangerController {

    @FXML
    TextField bookTitleInput;

    @FXML
    TextField bookAuthorInput;

    @FXML
    DigitsField bookYearPublishedInput;

    @FXML
    TextField bookImagePathInput;

    @FXML
    DatePicker bookDatePicker;

    @FXML
    TextField bookDatePickerDisplay;

    @FXML
    ComboBox<BookReadingState> bookReadingStateCombo;

    /**
     *
     * Load reading state input and custom date picker.
     *
     */
    @FXML
    protected void initialize() {

        FxUtil.setUpReadingStateDropDown(bookReadingStateCombo);

        FxUtil.setUpCustomDatePicker(bookDatePicker, bookDatePickerDisplay);

    }

    /**
     * Fill input fields with book data.
     *
     * @param book
     */
    protected void loadBookInput(final Book book) {

        bookTitleInput.setText(book.getTitle());

        bookAuthorInput.setText(book.getAuthor());

        if (book.getYearPublished() != Book.YEAR_PUBLISHED_MISSING) {
            bookYearPublishedInput.setText(String.valueOf(book.getYearPublished()));
        }

        bookImagePathInput.setText(book.getImgPath());

    }

}
