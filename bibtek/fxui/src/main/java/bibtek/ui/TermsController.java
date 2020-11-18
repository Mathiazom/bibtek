package bibtek.ui;

import bibtek.ui.utils.ToastUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class TermsController extends SceneChangerController {

    @FXML
    Button close;

    /**
     * Closes the window.
     */
    public void close() {
        final Stage stage = (Stage) close.getScene().getWindow();
        try {
            this.changeScene(stage, "/bibtek/ui/CreateUser.fxml");
        } catch (IOException e) {
            ToastUtil.makeToast(stage, Toast.ToastState.ERROR, "There was an error showing the create user page");
            e.printStackTrace();
        }
    }

}
