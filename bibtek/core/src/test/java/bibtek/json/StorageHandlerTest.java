package bibtek.json;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;

import bibtek.core.User;

import org.junit.jupiter.api.Test;

public class StorageHandlerTest {
    /**
     * Set the storage handler.
     */
    private StorageHandler sh;

    /*
     * The storgaHandler should be emtied before each test.
     */
    /*
     * @BeforeEach public void clearStorageHanndler() { try { sh = new
     * StorageHandler("target/testLibrary.json"); sh.storeUser(null); } catch
     * (IOException e) { e.printStackTrace(); } }
     */

    /**
     * Test the fetchBookEntries method.
     */
    @Test
    public void fetchUserTest() {
        // Testing if it thows IOException if path does not exist
        try {
            sh = new StorageHandler("target/testLibrary.json");
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
        }
    }

}
