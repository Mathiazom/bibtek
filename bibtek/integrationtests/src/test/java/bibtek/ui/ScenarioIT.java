package bibtek.ui;

import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ScenarioIT extends ApplicationTest {

    private SceneChangerController controller;
    private Stage stage;

    @Override
    public final void start(final Stage stage) throws Exception {

        this.stage = stage;

        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/bibtek/ui/LoginPage.fxml"));
        final Parent parent = fxmlLoader.load();
        this.controller = fxmlLoader.getController();
        stage.setScene(new Scene(parent));
        stage.show();

    }

    /**
     * Testing a full scenario, moving through multiple pages.
     */
    @Test
    public void userScenarioTest() {
        // Actually

    }

}
