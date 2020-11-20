package bibtek.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.http.impl.conn.Wire;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginTest extends WireMockApplicationTest {

    private Parent parent;
    private Stage stage;

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

        stubFor(get(urlEqualTo("/bibtek/users/dante"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(TestConstants.userDanteJson())
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
