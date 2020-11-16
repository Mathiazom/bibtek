package bibtek.ui;

import bibtek.core.Book;
import bibtek.core.BookEntry;
import bibtek.core.BookReadingState;
import bibtek.core.User;
import bibtek.json.RemoteStorageHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ViewBookIT extends ApplicationTest {

    private ViewBookController controller;

    private Stage stage;

    private RemoteStorageHandler storageHandler;

    private BookEntry bookEntry;

    @Override
    public final void start(final Stage stage) throws Exception {

        this.stage = stage;

        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/bibtek/ui/ViewBook.fxml"));
        final Parent parent = fxmlLoader.load();
        this.controller = fxmlLoader.getController();
        stage.setScene(new Scene(parent));
        stage.show();

        this.storageHandler = getRemoteStorageHandler();

        final User user = storageHandler.getUser("dante");
        bookEntry = user.getLibrary().getBookEntries().iterator().next();
        controller.update(bookEntry, user);


    }

    @Test
    public void changeReadingStateTest(){

        Parent parent = stage.getScene().getRoot();

        final ComboBox<BookReadingState> readingStateCombo = (ComboBox<BookReadingState>) parent.lookup("#addBookReadingStatusCombo");

        clickOn(readingStateCombo)
                .type(KeyCode.DOWN).sleep(300)
                .type(KeyCode.ENTER).sleep(300);

        final BookReadingState readingState = readingStateCombo.getValue();

        clickOn("#backButton").sleep(300);

        parent = stage.getScene().getRoot();
        final ListView<BookItemView> libraryList = (ListView<BookItemView>) parent.lookup("#libraryList");
        final BookItemView firstBook = libraryList.getItems().get(0);
        clickOn(firstBook).sleep(300);

        assertEquals(readingState, readingStateCombo.getValue());

    }

    private RemoteStorageHandler getRemoteStorageHandler() throws URISyntaxException {

        final String port = System.getProperty("bibtek.port");

        final URI remoteUri = new URI("http://localhost:" + port + "/bibtek/users/");

        return new RemoteStorageHandler(remoteUri);

    }

}


