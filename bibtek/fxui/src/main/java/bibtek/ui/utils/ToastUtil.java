package bibtek.ui.utils;

import bibtek.ui.Toast;
import javafx.animation.KeyValue;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Utility class to display simple {@link Toast}.
 */
public final class ToastUtil {

    private static final int TOAST_LAYOUT_Y = 20;

    private static final int DEFAULT_MSG_TIME = 2500;
    private static final int DEFAULT_FADE_IN_TIME = 200;
    private static final int DEFAULT_FADE_OUT_TIME = 400;

    // Prevent initialization of utility class
    private ToastUtil() {
    }

    /**
     * @see #makeToast(Stage, Toast.ToastState, String, String).
     * {@inheritDoc}
     */
    public static void makeToast(final Stage ownerStage, final Toast.ToastState state, final String message) {

        makeToast(ownerStage, state, message, null, DEFAULT_MSG_TIME, DEFAULT_FADE_IN_TIME, DEFAULT_FADE_OUT_TIME);

    }

    /**
     * @see #makeToast(Stage, Toast.ToastState, String, String, int, int, int) .
     * {@inheritDoc}
     */
    public static void makeToast(final Stage ownerStage, final Toast.ToastState state, final String toastMsg, final String extraMsg) {

        makeToast(ownerStage, state, toastMsg, extraMsg, DEFAULT_MSG_TIME, DEFAULT_FADE_IN_TIME, DEFAULT_FADE_OUT_TIME);

    }

    /**
     * Utility method to display simple toast message.
     *
     * @param ownerStage        where toast should be hosted
     * @param state             to specify toast color and icon
     * @param toastMsg          to display inside toast
     * @param extraMsg          to display in parenthesis after main message (unless null)
     * @param displayTime       total time toast should be visible
     * @param transitionInTime  total time used to transition in toast
     * @param transitionOutTime total time used to transition out toast
     */
    public static void makeToast(final Stage ownerStage,
                                 final Toast.ToastState state,
                                 final String toastMsg,
                                 final String extraMsg,
                                 final int displayTime,
                                 final int transitionInTime,
                                 final int transitionOutTime) {

        String msg = toastMsg;
        if (extraMsg != null) {
            msg += " (" + extraMsg + ")";
        }

        final Toast toast = new Toast(msg, state);

        final Pane parentPane = (Pane) ownerStage.getScene().getRoot();

        clearToasts(parentPane);

        // Display Toast
        parentPane.getChildren().add(toast);

        // Adjust Toast width to match its parent
        toast.prefWidthProperty().bind(parentPane.widthProperty());

        // Animate Toast to slide in and fade out
        FxUtil.animateNodeTransition(
                new KeyValue(toast.layoutYProperty(), TOAST_LAYOUT_Y),
                new KeyValue(toast.opacityProperty(), 0),
                displayTime,
                transitionInTime,
                transitionOutTime,
                (aeb) -> ((AnchorPane) ownerStage.getScene().getRoot()).getChildren().remove(toast)
        );

    }

    /**
     * Remove any Toast messages from the given pane.
     *
     * @param parentPane to be cleared of Toasts
     */
    private static void clearToasts(final Pane parentPane) {

        final ObservableList<Node> children = parentPane.getChildren();
        for (Node node : children) {
            if (node instanceof Toast) {
                children.remove(node);
                break;
            }
        }

    }

}
