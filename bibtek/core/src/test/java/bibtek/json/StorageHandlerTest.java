package bibtek.json;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;

import bibtek.core.Library;
import bibtek.core.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class StorageHandlerTest {
    /**
     * Set the storage handler.
     */
    private LocalStorageHandler sh;

    /**
     * Initilize he storage hanlder with a temporary file.
     */
    @BeforeEach
    public void intitilizeStorageHandler() {

        try {
            File tempFile = File.createTempFile("testLibrary", ".json");
            sh = new StorageHandler(tempFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test the fetchBookEntries method.
     */
    //@Test
    public void fetchUserTest() {
        /*// Testing if it thows IOException if path does not exist
        try {
            sh = new DirectStorageHandler("target/testLibrary.json");
        } catch (IOException e1) {
            e1.printStackTrace();
            return;
        }
        try {

            sh.setStoragePath("nonExistent/nonExistentLibrary.json");
            User nonExistent = sh.getLocalUser();
            System.out.println("This should be empty: " + nonExistent.toString());
            fail("Should throw IOException when trying to get data from a non-excistent file");
        } catch (IOException e) {
            // Succeeds
        }*/
    }

    @Test
    public void storeUserLocallyTest() {
        Library lib = new Library();

    }

}
