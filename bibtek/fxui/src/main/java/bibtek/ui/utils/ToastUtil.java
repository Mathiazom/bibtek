package bibtek.ui.utils;

import bibtek.ui.Toast;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Utility class to display simple toast messages.
 */
public final class ToastUtil {

    private static final int DEFAULT_MSG_TIME = 2500;
    private static final int DEFAULT_FADE_IN_TIME = 200;
    private static final int DEFAULT_FADE_OUT_TIME = 400;

    // Prevent initialization of utility class
    private ToastUtil() {
    }

    /**
     * Utility method to display simple toast message.
     * @param ownerStage where toast should be hosted
     * @param state to specify toast color and icon
     * @param toastMsg to display inside toast
     */
    public static void makeText(final Stage ownerStage, final Toast.ToastState state, final String toastMsg) {

        makeText(ownerStage, state, toastMsg, DEFAULT_MSG_TIME, DEFAULT_FADE_IN_TIME, DEFAULT_FADE_OUT_TIME);

    }

    /**
     * Utility method to display simple toast message.
     * @param ownerStage where toast should be hosted
     * @param state to specify toast color and icon
     * @param toastMsg to display inside toast
     * @param toastMsgTime total time toast should be visible
     * @param fadeInTime total time used to transition in toast
     * @param fadeOutTime total time used to transition out toast
     */
    public static void makeText(final Stage ownerStage, final Toast.ToastState state, final String toastMsg, final int toastMsgTime, final int fadeInTime, final int fadeOutTime) {

        final Toast toast = new Toast(toastMsg, state);

        final Pane parentPane = (Pane) ownerStage.getScene().getRoot();

        final ObservableList<Node> children = parentPane.getChildren();

        for (Node node : children) {
            if (node instanceof Toast) {
                children.remove(node);
                break;
            }
        }

        parentPane.getChildren().add(toast);

        toast.prefWidthProperty().bind(parentPane.widthProperty());

        final Timeline fadeInTimeline = new Timeline();
        final KeyFrame fadeInKey = new KeyFrame(Duration.millis(fadeInTime), new KeyValue(toast.layoutYProperty(), 20));
        fadeInTimeline.getKeyFrames().add(fadeInKey);
        fadeInTimeline.setOnFinished((ae) ->
                new Thread(() -> {
                    try {
                        Thread.sleep(toastMsgTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    final Timeline fadeOutTimeline = new Timeline();
                    final KeyFrame fadeOutKey = new KeyFrame(Duration.millis(fadeOutTime), new KeyValue(toast.opacityProperty(), 0));
                    fadeOutTimeline.getKeyFrames().add(fadeOutKey);
                    fadeOutTimeline.setOnFinished((aeb) -> ((AnchorPane) ownerStage.getScene().getRoot()).getChildren().remove(toast));
                    fadeOutTimeline.play();
                }).start());
        fadeInTimeline.play();

    }
}
