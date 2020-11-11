package bibtek.ui;

import bibtek.core.Book;
import bibtek.core.BookReadingState;
import bibtek.core.User;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.skin.DatePickerSkin;
import javafx.util.StringConverter;

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

        FxUtils.setUpReadingStateDropDown(addBookReadingStatusCombo);

        FxUtils.setUpCustomDatePicker(addBookDatePicker, addBookDatePickerField);

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
