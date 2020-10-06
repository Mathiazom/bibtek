package bibtek.json;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.util.Set;

import bibtek.core.BookEntry;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class StorageHandlerTest {
    /**
     * Set the storage handler.
     */
    private StorageHandler sh;

    /**
     * The storgaHandler should be emtied before each test.
     */
    @BeforeEach
    public void clearStorageHanndler() {
        try {
            sh = new StorageHandler("target/testLibrary.json");
            sh.storeBookEntries(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test the fetchBookEntries method.
     */
    @Test
    public void fetchBookEntriesTest() {
        // Testing if it thows IOException if path does not exist
        try {
            sh.setStoragePath("nonExistent/nonExistentLibrary.json");
            Set<BookEntry> nonExistent = sh.fetchBookEntries();
            System.out.println("This should be empty: " + nonExistent.toString());
            fail("Should throw IOException when trying to get data from a non-excistent file");
        } catch (IOException e) {
            // Succeeds
        }
    }

}
