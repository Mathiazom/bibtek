package bibtek.ui;

import bibtek.core.Book;
import bibtek.core.BookReadingState;
import bibtek.ui.utils.FxUtil;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public abstract class BaseBookController extends SceneChangerController {

    @FXML
    TextField addBookTitleField;

    @FXML
    TextField addBookAuthorField;

    @FXML
    DigitsField addBookYearPublishedField;

    @FXML
    TextField addBookImagePathField;

    @FXML
    DatePicker addBookDatePicker;

    @FXML
    TextField addBookDatePickerField;

    @FXML
    Label errorLabel;

    @FXML
    ComboBox<BookReadingState> addBookReadingStatusCombo;

    /**
     *
     * Load reading state input and custom date picker.
     *
     */
    @FXML
    protected void initialize() {

        FxUtil.setUpReadingStateDropDown(addBookReadingStatusCombo);

        FxUtil.setUpCustomDatePicker(addBookDatePicker, addBookDatePickerField);

    }

    /**
     * Fill input fields with book data.
     *
     * @param book
     */
    protected void loadBookInput(final Book book) {

        addBookTitleField.setText(book.getTitle());

        addBookAuthorField.setText(book.getAuthor());

        if (book.getYearPublished() != Book.YEAR_PUBLISHED_MISSING) {
            addBookYearPublishedField.setText(String.valueOf(book.getYearPublished()));
        }

        addBookImagePathField.setText(book.getImgPath());

    }

}
