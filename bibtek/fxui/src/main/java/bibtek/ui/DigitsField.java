package bibtek.ui;

import javafx.scene.control.TextField;

/**
 * Field type that ensures it only contains digits.
 */
public class DigitsField extends TextField {

    /**
     * Init with text property listener to continuously sanitize input.
     */
    public DigitsField() {

        textProperty().addListener((observable, oldValue, newValue) -> setText(newValue.replaceAll("[^\\d]", "")));

    }

    /**
     * Retrieve field input as integer.
     *
     * @return integer input
     */
    public int getInputAsInt() {
        return Integer.parseInt(getText());
    }

}
