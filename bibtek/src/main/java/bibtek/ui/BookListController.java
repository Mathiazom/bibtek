package bibtek.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;

public class BookListController {

    @FXML
    ListView<String> bookListView;

    @FXML
    Button addBookButton;


    @FXML
    private void initialize(){

        bookListView.getItems().setAll(
                "1984, George Orwell, COMPLETED",
                "Neuromancer, William Gibson, COMPLETED",
                "Superintelligence, Nick Bostrom, NOT_STARTED"
        );

    }

    @FXML
    private void handleAddBook(){

        final Stage stage = (Stage) addBookButton.getScene().getWindow();

        final Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/bibtek/ui/AddBook.fxml"));
            final Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}

