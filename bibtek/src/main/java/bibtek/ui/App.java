package bibtek.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class App extends Application {

  @Override
  public void start(final Stage primaryStage) throws Exception {

    primaryStage.setTitle("Bibtek");
    primaryStage.getIcons().add(new Image("/bibtek/ui/icon.png"));

    final Scene bookListScene = new Scene(FXMLLoader.load(getClass().getResource("/bibtek/ui/BookList.fxml")));
    primaryStage.setScene(bookListScene);

    primaryStage.show();

  }

  public static void main(final String[] args) {
    launch(args);
  }

}
