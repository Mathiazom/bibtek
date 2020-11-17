package bibtek.ui.utils;

import bibtek.core.BookReadingState;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.skin.DatePickerSkin;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;
import javafx.util.StringConverter;

public final class FxUtil {

    private FxUtil() {
    }

    /**
     * Helper method to initialize dropdown of BookReadingStates.
     *
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
     *
     * @param datePicker actually picking dates
     * @param dateField  displaying picked date
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

        datePickerSkin.getPopupContent().addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            // Press ESC to hide datepicker
            if (event.getCode() == KeyCode.ESCAPE) {
                datePickerSkin.hide();
                event.consume();
            }
        });

        final TextField datePickerOutput = ((TextField) datePickerSkin.getDisplayNode());
        dateField.textProperty().bind(datePickerOutput.textProperty());

    }

    /**
     *
     * Create a transition of start- and end values of node properties.
     *
     * @param startValue property state at the start of transition
     * @param endValue property state at the end of transition
     * @param displayTime total time node values are stable between start and end animations
     * @param transitionInTime total time of start->stable animation
     * @param transitionOutTime total time of stable->end animation
     * @param onFinished called when transition is finished
     *
     */
    public static void animateNodeTransition(
            final KeyValue startValue,
            final KeyValue endValue,
            final int displayTime,
            final int transitionInTime,
            final int transitionOutTime,
            final EventHandler<ActionEvent> onFinished
    ) {

        final Timeline fadeInTimeline = new Timeline();
        final KeyFrame fadeInKey = new KeyFrame(Duration.millis(transitionInTime), startValue);
        fadeInTimeline.getKeyFrames().add(fadeInKey);
        fadeInTimeline.setOnFinished((ae) ->
                new Thread(() -> {
                    try {
                        Thread.sleep(displayTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    final Timeline fadeOutTimeline = new Timeline();
                    final KeyFrame fadeOutKey = new KeyFrame(Duration.millis(transitionOutTime), endValue);
                    fadeOutTimeline.getKeyFrames().add(fadeOutKey);
                    fadeOutTimeline.setOnFinished(onFinished);
                    fadeOutTimeline.play();
                }).start());
        fadeInTimeline.play();

    }


}
