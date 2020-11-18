package bibtek.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;

/**
 * Simple popup containing a short message, icon and fitting background color.
 * Appears on top of all nodes.
 */
public class Toast extends AnchorPane {


    public enum ToastState {

        /**
         * General information.
         */
        INFO("mdi-information-outline", "#78909c"),

        /**
         * Successful event has occurred.
         */
        SUCCESS("mdi-check", "#66bb6a"),

        /**
         * Something is incorrect (e.g. user input)
         */
        INCORRECT("mdi-close", "#ef5350"),

        /**
         * Error has occurred.
         */
        ERROR("mdi-alert-outline", "#ef5350");

        private final String iconLiteral;
        private final String boxColor;

        ToastState(final String iconLiteral, final String boxColor) {
            this.iconLiteral = iconLiteral;
            this.boxColor = boxColor;
        }

    }

    @FXML
    HBox toastBox;

    @FXML
    Label toastText;

    @FXML
    FontIcon toastIcon;

    private ToastState state;

    /**
     * Display simple message.
     *
     * @param message to display
     * @param state   deciding icon and color
     */
    public Toast(final String message, final ToastState state) {

        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/bibtek/ui/fxml/Toast.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.state = state;

        toastText.setText(message);
        toastIcon.setIconLiteral(state.iconLiteral);
        toastBox.setStyle("-fx-background-color: " + state.boxColor);

    }

    /**
     * Get current {@link ToastState}.
     *
     * @return state
     */
    public ToastState getState() {
        return state;
    }

}
