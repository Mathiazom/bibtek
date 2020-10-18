package bibtek.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public final class App extends Application {

  @Override
  public void start(final Stage primaryStage) throws Exception {

    primaryStage.setTitle("Bibtek");
    try {
      primaryStage.getIcons().add(new Image("/bibtek/ui/icon.png"));
    } catch (Exception e) {
      // Don't add this icon.
    }

    final Scene libraryScene = new Scene(FXMLLoader.load(getClass().getResource("/bibtek/ui/LoginPage.fxml")));
    primaryStage.setScene(libraryScene);

    primaryStage.show();

  }

  /**
   * App main method.
   *
   * @param args command line arguments
   */
  public static void main(final String[] args) {
    launch(args);
  }

}
