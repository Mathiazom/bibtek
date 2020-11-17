package bibtek.ui;

import bibtek.ui.utils.ToastUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class SettingsController extends SceneChangerController {


    @FXML
    Button exitSettingsButton;

    @FXML
    Button logOffButton;

    /**
     * Exits settings by pressing corresponding button.
     */
    public void exitSettings() {
        final Stage stage = (Stage) exitSettingsButton.getScene().getWindow();
        try {
            this.changeScene(stage, "/bibtek/ui/fxml/Library.fxml");
        } catch (IOException e) {
            ToastUtil.makeText(stage, Toast.ToastState.ERROR, "There was an error showing your library");
            e.printStackTrace();

        }
    }

    /**
     * Logs the user off and displays the login page.
     */
    public void logOff() {
        final Stage stage = (Stage) logOffButton.getScene().getWindow();
        try {
            this.changeSceneAndUpdateUser(stage, "/bibtek/ui/fxml/LoginPage.fxml");
        } catch (IOException e) {
            ToastUtil.makeText(stage, Toast.ToastState.ERROR, "There was an error logging off");
            e.printStackTrace();

        }
    }
}
