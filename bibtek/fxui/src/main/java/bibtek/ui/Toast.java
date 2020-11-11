package bibtek.ui;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public final class Toast {

    private Toast() {
    }

    public static void makeText(final Stage ownerStage, final String toastMsg) {

        final int toastMsgTime = 3500; //3.5 seconds
        final int fadeInTime = 500; //0.5 seconds
        final int fadeOutTime = 500; //0.5 seconds

        makeText(ownerStage, toastMsg, toastMsgTime, fadeInTime, fadeOutTime);

    }

    public static void makeText(final Stage ownerStage, final String toastMsg, final int toastMsgTime, final int fadeInTime, final int fadeOutTime) {

        final Stage toastStage = new Stage();
        toastStage.initOwner(ownerStage);
        toastStage.setResizable(false);
        toastStage.initStyle(StageStyle.TRANSPARENT);

        final Text text = new Text(toastMsg);
        text.setFont(Font.font("Verdana", 40));
        text.setFill(Color.RED);

        final StackPane root = new StackPane(text);
        root.setStyle("-fx-background-radius: 20; -fx-background-color: rgba(0, 0, 0, 0.2); -fx-padding: 50px;");
        root.setOpacity(0);

        final Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        toastStage.setScene(scene);
        toastStage.show();

        final Timeline fadeInTimeline = new Timeline();
        final KeyFrame fadeInKey1 = new KeyFrame(Duration.millis(fadeInTime), new KeyValue(toastStage.getScene().getRoot().opacityProperty(), 1));
        fadeInTimeline.getKeyFrames().add(fadeInKey1);
        fadeInTimeline.setOnFinished((ae) ->
        {
            new Thread(() -> {
                try {
                    Thread.sleep(toastMsgTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                final Timeline fadeOutTimeline = new Timeline();
                final KeyFrame fadeOutKey1 = new KeyFrame(Duration.millis(fadeOutTime), new KeyValue(toastStage.getScene().getRoot().opacityProperty(), 0));
                fadeOutTimeline.getKeyFrames().add(fadeOutKey1);
                fadeOutTimeline.setOnFinished((aeb) -> toastStage.close());
                fadeOutTimeline.play();
            }).start();
        });
        fadeInTimeline.play();
    }
}
