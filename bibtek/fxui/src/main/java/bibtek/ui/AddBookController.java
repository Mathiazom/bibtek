package bibtek.ui;

import bibtek.core.Book;
import bibtek.core.BookEntry;
import bibtek.core.BookReadingState;
import bibtek.json.BooksAPIHandler;
import bibtek.json.ISBNUtils;
import bibtek.json.StorageHandler;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.skin.DatePickerSkin;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;

public final class AddBookController extends SceneChangerController {

    @FXML
    VBox addBookISBNContainer;

    @FXML
    DigitsField addBookISBNField;

    @FXML
    Label errorLabelISBN;

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

    @FXML
    private void initialize() {

        setUpReadingStateDropDown();

        setUpCustomDatePicker();

        addBookISBNField.textProperty()
                .addListener((observable, oldValue, newValue) -> errorLabelISBN.setManaged(false));

    }

    private void setUpReadingStateDropDown() {

        addBookReadingStatusCombo.setConverter(new StringConverter<>() {
            @Override
            public String toString(final BookReadingState readingState) {
                return readingState.toString();
            }

            @Override
            public BookReadingState fromString(final String s) {
                return BookReadingState.fromString(s);
            }
        });
        addBookReadingStatusCombo.setItems(FXCollections.observableArrayList(BookReadingState.values()));
        addBookReadingStatusCombo.getSelectionModel().selectFirst();

    }

    private void setUpCustomDatePicker() {

        final DatePickerSkin datePickerSkin = new DatePickerSkin(addBookDatePicker);

        addBookDatePickerField.focusedProperty().addListener((observableValue, a, focused) -> {
            if (focused) {
                showDatePicker();
                datePickerSkin.getDisplayNode().requestFocus();
            }
        });

        final TextField addBookDatePickerOutput = ((TextField) datePickerSkin.getDisplayNode());
        addBookDatePickerField.textProperty().bind(addBookDatePickerOutput.textProperty());

    }

    private void showDatePicker() {

        final DatePickerSkin datePickerSkin = new DatePickerSkin(addBookDatePicker);

        datePickerSkin.show();
        final Node datePickerNode = datePickerSkin.getPopupContent();
        final Bounds dateFieldBounds = addBookDatePickerField.getLayoutBounds();
        datePickerNode.relocate(dateFieldBounds.getMinX(), dateFieldBounds.getMaxY());

    }

    @FXML
    private void handleAddBook() {

        final BookEntry bookEntry = new BookEntry(
                new Book(addBookTitleField.getText(), addBookAuthorField.getText(),
                        Integer.parseInt(addBookYearPublishedField.getText()), addBookImagePathField.getText()),
                addBookDatePicker.getValue(), addBookReadingStatusCombo.getValue()

        );

        user.getLibrary().addBookEntry(bookEntry);

        final StorageHandler storageHandler = new StorageHandler();
        storageHandler.notifyUserChanged(user);

        handleShowLibrary();

    }

    @FXML
    private void toggleShowISBNInput() {

        addBookISBNContainer.setManaged(!addBookISBNContainer.isManaged());

    }

    @FXML
    private void handleLoadBookFromISBNInput() {

        final String isbn = addBookISBNField.getText();

        if (!ISBNUtils.isValidISBN(isbn)) {
            errorLabelISBN.setText("Looks like an invalid ISBN :(");
            errorLabelISBN.setManaged(true);
            return;
        }

        try {
            final Book bookFromISBN = new BooksAPIHandler().fetchBook(isbn);
            if (bookFromISBN == null) {
                errorLabelISBN.setText("No results for that ISBN :(");
                errorLabelISBN.setManaged(true);
                return;
            }
            loadBookInput(bookFromISBN);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void loadBookInput(final Book book) {

        addBookTitleField.setText(book.getTitle());

        addBookAuthorField.setText(book.getAuthor());

        if (book.getYearPublished() != Book.YEAR_PUBLISHED_MISSING) {
            addBookYearPublishedField.setText(String.valueOf(book.getYearPublished()));
        }

        addBookImagePathField.setText(book.getImgPath());

    }

    @FXML
    private void handleShowLibrary() {
        Stage stage = (Stage) addBookAuthorField.getScene().getWindow();
        try {
            this.changeSceneAndUpdateUser(stage, "/bibtek/ui/Library.fxml");
        } catch (IOException e) {
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText("There was an error when showing your library");
            e.printStackTrace();
        }

    }

}
