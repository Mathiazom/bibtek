package bibtek.ui;

import javafx.scene.control.TextField;

/**
 * Field type that ensures it only contains digits.
 */
public class DigitsField extends TextField {

    public DigitsField() {

        textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

    }

}
