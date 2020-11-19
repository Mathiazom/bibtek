package bibtek.ui;

import bibtek.json.StorageHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginTest extends WireMockApplicationTest {

    private Parent parent;
    private Stage stage;

    /**
     * Prepares the system.
     */
    @BeforeAll
    public static void headless() {
        if (Boolean.parseBoolean(System.getProperty("gitlab-ci", "false"))) {
            System.setProperty("prism.verbose", "true");
            System.setProperty("java.awt.headless", "true");
            System.setProperty("testfx.robot", "glass");
            System.setProperty("testfx.headless", "true");
            System.setProperty("glass.platform", "Monocle");
            System.setProperty("monocle.platform", "Headless");
            System.setProperty("prism.order", "sw");
            System.setProperty("prism.text", "t2k");
            System.setProperty("testfx.setup.timeout", "2500");
        }
    }

    /**
     * Starts the app to test it.
     *
     * @param stage takes the stage of the app
     */
    @Override
    public void start(final Stage stage) throws Exception {
        this.stage = stage;
        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/bibtek/ui/fxml/Login.fxml"));
        this.parent = fxmlLoader.load();
        stage.setScene(new Scene(parent));
        stage.show();

    }

    @Test
    public void loginTest(){

        clickOn("#userNameInput").write("dante");

        stubFor(get(urlEqualTo("/bibtek/users/"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(TestConstants.userMapDanteJson())
                )
        );

        clickOn("#logInButton");

        final AnchorPane libraryRoot = (AnchorPane) stage.getScene().getRoot().lookup("#libraryRoot");
        assertNotNull(libraryRoot);
        assertTrue(libraryRoot.isVisible());

    }

    @Test
    public void loginIncorrectUsernameTest(){

        clickOn("#userNameInput").write("strauss");

        clickOn("#logInButton");

        FxTestUtil.assertToast(Toast.ToastState.INFO, "No user with given username", parent);

    }

    @Test
    public void showCreateUserTest(){

        clickOn("#createNewUserLabel");

        final AnchorPane createUserRoot = (AnchorPane) stage.getScene().getRoot().lookup("#createUserRoot");
        assertNotNull(createUserRoot);
        assertTrue(createUserRoot.isVisible());

    }

}
