package bibtek.ui.utils;

import bibtek.core.BookReadingState;
import javafx.collections.FXCollections;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.skin.DatePickerSkin;
import javafx.util.StringConverter;

public final class FxUtil {

    private FxUtil() {
    }

    /**
     * Helper method to initialize dropdown of BookReadingStates.
     * @param dropDown to be initialized
     */
    public static void setUpReadingStateDropDown(final ComboBox<BookReadingState> dropDown) {

        dropDown.setConverter(new StringConverter<>() {
            @Override
            public String toString(final BookReadingState readingState) {
                return readingState.toString();
            }

            @Override
            public BookReadingState fromString(final String s) {
                return BookReadingState.fromString(s);
            }
        });
        dropDown.setItems(FXCollections.observableArrayList(BookReadingState.values()));
        dropDown.getSelectionModel().selectFirst();

    }

    /**
     * Helper method to setup date picker with custom date field (for added CSS support).
     * @param datePicker actually picking dates
     * @param dateField displaying picked date
     */
    public static void setUpCustomDatePicker(final DatePicker datePicker, final TextField dateField) {

        final DatePickerSkin datePickerSkin = new DatePickerSkin(datePicker);

        dateField.focusedProperty().addListener((observableValue, a, focused) -> {
            if (focused) {
                datePickerSkin.show();
                final Node datePickerNode = datePickerSkin.getPopupContent();
                final Bounds dateFieldBounds = dateField.getLayoutBounds();
                datePickerNode.relocate(dateFieldBounds.getMinX(), dateFieldBounds.getMaxY());

                datePickerSkin.getDisplayNode().requestFocus();
            }
        });

        final TextField addBookDatePickerOutput = ((TextField) datePickerSkin.getDisplayNode());
        dateField.textProperty().bind(addBookDatePickerOutput.textProperty());

    }


}
