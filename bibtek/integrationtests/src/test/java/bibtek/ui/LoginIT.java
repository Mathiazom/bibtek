package bibtek.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.net.URI;
import java.net.URISyntaxException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

public class LoginIT extends ApplicationTest {

  private LoginController controller;

  @Override
  public final void start(final Stage stage) throws Exception {

    final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/bibtek/ui/fxml/Login.fxml"));
    final Parent parent = fxmlLoader.load();
    this.controller = fxmlLoader.getController();
    stage.setScene(new Scene(parent));
    stage.show();

  }

  /**
   * Temporary comment.
   *
   * @throws URISyntaxException
   */
  @BeforeEach
  public void setupItems() throws URISyntaxException {
    // same as in test-todolist.json (should perhaps read it instead)
    String port = System.getProperty("bibtek.port");
    assertNotNull(port, "No bibtek.port system property set");
    URI baseUri = new URI("http://localhost:" + port + "/bibtek/users/");
  }

  /**
   * Test that the test works.
   */
  @Test
  public void testTest() {
    assertNotNull(this.controller);
  }
}
