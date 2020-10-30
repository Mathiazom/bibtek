package bibtek.ui;

import bibtek.core.Book;
import bibtek.core.BookEntry;
import bibtek.core.BookReadingState;
import bibtek.core.User;
import bibtek.json.BooksAPIHandler;
import bibtek.json.ISBNUtils;
import bibtek.json.StorageHandler;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.skin.DatePickerSkin;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;

public final class AddBookController {

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

    private User user;

    @FXML
    private void initialize() {

        setUpReadingStateDropDown();

        setUpCustomDatePicker();

        addBookISBNField.textProperty().addListener((observable, oldValue, newValue) -> errorLabelISBN.setManaged(false));

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
                        Integer.parseInt(addBookYearPublishedField.getText()),
                        addBookImagePathField.getText()),
                addBookDatePicker.getValue(), addBookReadingStatusCombo.getValue()

        );

        user.getLibrary().addBookEntry(bookEntry);
        try {
            StorageHandler storageHandler = new StorageHandler();
            storageHandler.updateUser(user);
        } catch (IOException e) {
            errorLabel.setText("Was not able to update the user library");
            errorLabel.setTextFill(Color.RED);
        }

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

        addBookYearPublishedField.setText(String.valueOf(book.getYearPublished()));

        addBookImagePathField.setText(book.getImgPath());

    }

    @FXML
    private void handleShowLibrary() {

        final LibraryController controller;

        final Stage stage = (Stage) addBookTitleField.getScene().getWindow();

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

    /**
     * Updating the user of this controller.
     *
     * @param u
     */
    public void update(final User u) {
        this.user = u;
    }

    /**
     * Get method for user.
     *
     * @return the user.
     */
    public User getUser() {
        return this.user;
    }

}
