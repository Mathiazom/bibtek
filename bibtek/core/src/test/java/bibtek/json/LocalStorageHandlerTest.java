package bibtek.json;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import bibtek.core.Library;
import bibtek.core.TestConstants;
import bibtek.core.User;
import bibtek.core.UserMap;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LocalStorageHandlerTest {

    private LocalStorageHandler localStorageHandler;
    private File tempDirectory;
    private User user;
    private User user2;

    /**
     * Creates a target directory if it is not created.
     *
     * @throws IOException if creation fails
     */
    @BeforeAll
    public static void makeTestTargetDir() throws IOException {
        final File testDir = new File(Paths.get("target").toAbsolutePath().toFile(), "test");
        if (!testDir.exists()) {
            if (!testDir.mkdirs()) {
                throw new IOException("Unable to create directory");
            }
        }
    }

    /**
     * Initialize StorageHandler with a temporary file.
     *
     * @throws IOException if directory could not be created
     */
    @BeforeEach
    public void initializeLocalStorageHandler() throws IOException {

        try {
            tempDirectory = Files.createTempDirectory(Paths.get("target/test"), "testDirectory").toFile();
            localStorageHandler = new LocalStorageHandler(tempDirectory.getAbsolutePath());
            tempDirectory.deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException(e);
        }

        final Library lib = new Library();
        lib.addBookEntry(TestConstants.BOOK_ENTRY1);
        lib.addBookEntry(TestConstants.BOOK_ENTRY2);
        user = new User("Name", TestConstants.AGE20, lib);
        user2 = new User("AnotherName", TestConstants.AGE21, lib);

    }

    /**
     * Test the putUser method.
     */
    @Test
    public void putUserTest() {
        // Test if the putUser stores the user as expected
        assertEquals(localStorageHandler.putUser(user), LocalStorageHandler.OK, "Failed to put user");
        try (BufferedReader reader = new BufferedReader(
                new FileReader(new File(this.tempDirectory, user.getUserName() + ".json")))) {
            final Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
                    .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer()).setPrettyPrinting().create();
            User actualUser = gson.fromJson(reader, new TypeToken<User>() {
            }.getType());
            assertEquals(user, actualUser, "The LocalStorageHandlers putUser method did not store user correctly despite STATUS_OK");
        } catch (FileNotFoundException e) {
            fail("Could not find file despite STATUS_OK");

        } catch (IOException e) {
            e.printStackTrace();
            fail("IOxception thrown despite STATUS_OK");
        }

    }

    /**
     * Test if setStorageDirectory throws exception when given a file path istead of
     * a directory.
     */
    @Test
    public void setStorageDirectoryTest() {
        File tempFile;
        try {
            tempFile = File.createTempFile("testFile", ".json");
        } catch (IOException e1) {
            fail("Error creating file");
            return;
        }
        try {
            localStorageHandler.setStorageDirectory(tempFile.getAbsolutePath());
            fail("Did not throw excption when given a filepath instad of a directory");
        } catch (IOException e) {
            // Succeeds
        }
    }

    /**
     * Test the getUserMap method.
     */
    @Test
    public void getUserMapTest() {

        localStorageHandler.putUser(user);
        localStorageHandler.putUser(user2);
        final UserMap actual = localStorageHandler.getUserMap();

        final UserMap expected = new UserMap();
        expected.putUser(user);
        expected.putUser(user2);

        assertEquals(expected, actual, "getUserMap() did not return expected UserMap");

    }

    /**
     * Test that LocalStorageHandler returns empty user map if no puts have been performed.
     */
    @Test
    public void getEmptyUserMapTest(){

        final UserMap actual = localStorageHandler.getUserMap();
        final UserMap expected = new UserMap();
        assertEquals(expected, actual, "getUserMap() should have returned an empty user map");

    }

    /**
     * Test the putUserMap method.
     */
    @Test
    public void putUserMapTest() {

        final UserMap userMap = new UserMap();
        userMap.putUser(user);
        userMap.putUser(user2);

        localStorageHandler.putUserMap(userMap);
        UserMap actualUserMap = localStorageHandler.getUserMap();
        assertEquals(userMap, actualUserMap, "The user map was not stored correctly with putUserMap()");



    }

    /**
     * Test that if you put an empty UserMap, the file becomes empty
     */
    @Test
    public void putEmptyUserMapTest(){

        final UserMap empty = new UserMap();
        localStorageHandler.putUserMap(empty);
        UserMap actual = localStorageHandler.getUserMap();
        assertEquals(empty, actual,
                "File should be empty after putting an empty user map there");

    }

    /**
     * Test the getUser(username) method.
     */
    @Test
    public void getUserTest() {

        localStorageHandler.putUser(user);
        localStorageHandler.putUser(user2);
        final User actual = localStorageHandler.getUser(user2.getUserName());
        assertEquals(user2, actual, "getUser did not return expected user");

    }

    /**
     * Test if it returns a null User if the username is not there.
     */
    @Test
    public void getNonExistentUserTest(){

        localStorageHandler.putUser(user2);

        final User actual = localStorageHandler.getUser(user.getUserName());
        assertNull(actual, "getUser(username) should return null when it does not have that username");

    }

    /**
     * Test if the removeUser() actually removes the correct user
     */
    @Test
    public void removeUserTest() {

        localStorageHandler.putUser(user);
        localStorageHandler.putUser(user2);
        localStorageHandler.removeUser(user.getUserName());

        final UserMap expected = new UserMap();
        expected.putUser(user2);

        final UserMap actual = localStorageHandler.getUserMap();
        assertEquals(expected, actual, "removeUser did not remove the user");

        assertEquals(LocalStorageHandler.NOT_FOUND, localStorageHandler.removeUser("Non Existent User"), "Should return NOT_FOUND on non existent username");

        assertEquals(expected, actual,
                "User map changed after removeUser() on a non existent username");
    }

    /**
     * Delete directory recursively.
     *
     * @param directory to be deleted
     * @return true if it got deleted false otherwise.
     */
    private boolean deleteDirectory(final File directory) {
        final File[] allContents = directory.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directory.delete();
    }

    /**
     * Delete temp file after each test.
     */
    @AfterEach
    public void deleteTempFile() {
        assertTrue(deleteDirectory(tempDirectory), "Failed to delete temp dir after test");
    }

}
