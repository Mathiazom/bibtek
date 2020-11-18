package bibtek.json;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;

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

public class LocalStorageHandlerTest {
    /**
     * Set the storage handler.
     */
    private LocalStorageHandler localStorageHandler;
    private File tempDirectory;
    private Library lib;
    private User user;
    private User user2;
    private UserMap userMap;

    /**
     * Creates dir if it is not created.
     *
     * @throws IOException
     */
    @BeforeAll
    public static void makeTestDir() throws IOException {
        File testDir = new File(Paths.get("target").toAbsolutePath().toFile(), "test");
        if (!testDir.exists()) {
            if (!testDir.mkdirs()) {
                throw new IOException("Unable to create files");
            }
        }
    }

    /**
     * Initilize he storage hanlder with a temporary file.
     *
     * @throws IOException
     */
    @BeforeEach
    public void intitilizeStorageHandler() throws IOException {

        try {
            tempDirectory = Files.createTempDirectory(Paths.get("target/test"), "testDirectory").toFile();
            localStorageHandler = new LocalStorageHandler(tempDirectory.getAbsolutePath());
            tempDirectory.deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException(e);
        }
        lib = new Library();
        lib.addBookEntry(TestConstants.BOOK_ENTRY1);
        lib.addBookEntry(TestConstants.BOOK_ENTRY2);
        user = new User("Name", TestConstants.AGE20, lib);
        user2 = new User("AnotherName", TestConstants.AGE21, lib);
        userMap = new UserMap();
    }

    /**
     * Test the putUser method.
     */
    @Test
    public void putUserTest() {
        // Test if the putUser stores the user as exptected
        try {
            localStorageHandler.putUser(user);
        } catch (IOException e1) {
            e1.printStackTrace();
            fail("IOExcption thrown");
            return;
        }
        try (BufferedReader reader = new BufferedReader(
                new FileReader(new File(this.tempDirectory, user.getUserName() + ".json")))) {
            final Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
                    .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer()).setPrettyPrinting().create();
            User actualUser = gson.fromJson(reader, new TypeToken<User>() {
            }.getType());
            assertEquals(user, actualUser, "The LocalStorageHandlers putUser method did not store user correctly");
        } catch (FileNotFoundException e) {
            fail("Could not find file");

        } catch (IOException e) {
            e.printStackTrace();
            fail("IOxception thrown");
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
    public void getUserMapTest() throws IOException {
        // Test if it returns an empty UserMap when the file is empty
        UserMap actual1;
        try {
            actual1 = localStorageHandler.getUserMap();
        } catch (IOException e) {
            e.printStackTrace();
            fail("IOException thrown");
            return;
        }
        UserMap expected1 = new UserMap();
        assertEquals(expected1, actual1, "getUserMap() did not return an empty userMap when the file is empty");

        // Test if it returns expected UserMap
        UserMap expected2 = new UserMap();
        expected2.putUser(user);
        expected2.putUser(user2);

        localStorageHandler.putUser(user);
        localStorageHandler.putUser(user2);
        UserMap actual2 = localStorageHandler.getUserMap();

        assertEquals(expected2, actual2, "getUserMap() did not return expected UserMap");

    }

    /**
     * Test the putUserMap method.
     */
    @Test
    public void putUserMapTest() throws IOException {
        // Test that it puts expected userMap
        userMap.putUser(user);
        userMap.putUser(user2);

        localStorageHandler.putUserMap(userMap);
        UserMap actualUserMap = localStorageHandler.getUserMap();
        assertEquals(userMap, actualUserMap, "The user map was not stored correcly with putUserMap()");

        // Test that if you put an empy UserMap, the file becomes empty
        UserMap empty = new UserMap();
        localStorageHandler.putUserMap(empty);
        UserMap actualUserMap2 = localStorageHandler.getUserMap();
        assertEquals(empty, actualUserMap2,
                "The user map should be emtied in the file after putting an empty user map there");

    }

    /**
     * Test if the hasUser() method works as expeted.
     *
     * @throws IOException
     */
    @Test
    public void hasUserTest() throws IOException {
        // Test if it returns false on an empty file
        boolean actual = localStorageHandler.hasUser("anything");
        assertFalse(actual, "Should return false when the hasUser() is called on an empty file");

        // Test if the hasUser() returns false on a nonEmpty file

        localStorageHandler.putUser(user);

        boolean actual2 = localStorageHandler.hasUser(user2.getUserName());
        assertFalse(actual2, "Should return false when hasUser() us called on a username not in the file");

        // Test if it returns true on the user that is in the file
        localStorageHandler.putUser(user2);
        boolean actual3 = localStorageHandler.hasUser(user.getUserName());
        assertTrue(actual3, "Should return true when hasUser() is called on a username in the file");

    }

    /**
     * Test if the getUsernames() method works as expected.
     */
    @Test
    public void getUsernamesTest() throws IOException {
        // Test if it returns an empty Collection when the method is called an an empty
        // file
        boolean actual1 = localStorageHandler.getUsernames().isEmpty();
        assertTrue(actual1, "Should return an empty Collection when the file is empty");

        // Test if it returns the expected collection when the file is not empty
        Collection<String> expected2 = new HashSet<String>();
        expected2.add(user.getUserName());
        expected2.add(user2.getUserName());
        localStorageHandler.putUser(user);
        localStorageHandler.putUser(user2);
        Collection<String> actual2 = localStorageHandler.getUsernames();
        assertEquals(expected2, actual2, "getUsernames() did not return expected collection of usernames");

    }

    /**
     * Test the getUser(username) method.
     */
    @Test
    public void getUserTest() throws IOException {
        // Test if it returns a null User if the username is not there
        User actual1 = localStorageHandler.getUser(user.getUserName());
        boolean shouldBeTrue = actual1 == null;
        assertTrue(shouldBeTrue, "getUser(username) should return null when it does not have that username");

        // Test if pusUser returns the expected user.
        localStorageHandler.putUser(user);
        localStorageHandler.putUser(user2);
        User actual2 = localStorageHandler.getUser(user2.getUserName());
        assertEquals(user2, actual2, "getUser did not return expected user");

    }

    /**
     * Test the removeUser(username) method.
     *
     * @throws IOException
     */
    @Test
    public void removeUserTest() throws IOException {
        // Test if the removeUser() actaully removes the correct user
        localStorageHandler.putUser(user);
        localStorageHandler.putUser(user2);
        localStorageHandler.removeUser(user.getUserName());
        userMap.putUser(user2);
        UserMap actual2 = localStorageHandler.getUserMap();
        assertEquals(userMap, actual2, "removeUser did not remove the user");

        // Test if it does not remove a user when the username does not exist in file
        try {
            localStorageHandler.removeUser("Non Existent User");
            fail("Should throw exception when removeUser() is called on a non existent username");
        } catch (Exception e) {
            // Succeeds
        }
        assertEquals(userMap, actual2,
                "Something wrong happened when removeUser() was called on a non exixtent username");
    }

    /**
     * Test notifyUserChanged() method.
     */
    @Test
    public void notifyUserChangedTest() {
        // This is just putUserTest.
    }

    /**
     * Delete recursively function.
     *
     * @param directoryToBeDeleted
     * @return true if it got deleted false otherwise.
     */
    private boolean deleteDirectory(final File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }

    /**
     * Delete temp file after reach test.
     *
     * @throws IOException if faile to delete directory.
     */
    @AfterEach
    public void deleteTempFile() throws IOException {
        deleteDirectory(tempDirectory);
    }

}
