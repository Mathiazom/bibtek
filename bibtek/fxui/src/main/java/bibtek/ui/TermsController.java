package bibtek.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class TermsController extends SceneChangerController {

    @FXML
    Button close;

    /**
     * Closes the window.
     */
    public void close() {
        Stage stage = (Stage) close.getScene().getWindow();
        stage.close();

    }

}
