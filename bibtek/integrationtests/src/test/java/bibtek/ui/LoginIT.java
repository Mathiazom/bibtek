package bibtek.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;

public class LoginIT extends ApplicationTest {

    private LoginPageController controller;

    private Stage stage;

    @Override
    public final void start(final Stage stage) throws Exception {

        this.stage = stage;

        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/bibtek/ui/Login.fxml"));
        final Parent parent = fxmlLoader.load();
        this.controller = fxmlLoader.getController();
        stage.setScene(new Scene(parent));
        stage.show();

    }

//  /**
//   * Retrieve test server port and get corresponding remote handler
//   *
//   * @throws URISyntaxException
//   */
//  @BeforeEach
//  public void setupRemotePort() {
//
//    final String port = System.getProperty("bibtek.port");
//    assertNotNull(port, "No bibtek.port system property set");
//
//    final URI remoteUri;
//    try {
//      remoteUri = new URI("http://localhost:" + port + "/bibtek/users/");
//    } catch (URISyntaxException e) {
//      e.printStackTrace();
//      fail("Could not get URI for port " + port);
//      return;
//    }
//
//    try {
//      remoteStorageHandler = new RemoteStorageHandler(remoteUri);
//    } catch (URISyntaxException e) {
//      e.printStackTrace();
//      fail("Could not get remote handler with URI" + remoteUri);
//    }
//
//  }

    @Test
    public void loginUnknownUserTest() {

        // Attempt to login with username not associated with any User
        clickOn("#userNameInput").write("reinhardt");
        clickOn("#logInButton");

        final Parent parent = stage.getScene().getRoot();

        // Should not login and display library
        assertNull(parent.lookup("#libraryRoot"));

    }

    @Test
    public void loginExistingUserTest() {

        clickOn("#userNameInput").write("dante");
        clickOn("#logInButton");

        final Parent parent = stage.getScene().getRoot();

        assertNotNull(parent.lookup("#libraryRoot"));

        final ListView<BookItemView> libraryList = (ListView<BookItemView>) parent.lookup("#libraryList");
        assertNotNull(libraryList, "Library list is not displayed");

        final BookItemView firstBook = libraryList.getItems().get(0);
        assertNotNull(firstBook, "First book is not displayed");
        clickOn(firstBook);

    }

}
