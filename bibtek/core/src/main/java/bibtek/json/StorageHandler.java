package bibtek.json;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import bibtek.core.User;

import com.google.gson.reflect.TypeToken;

/**
 * Class responsible for reading and writing user to locally stored json files.
 */
public final class StorageHandler {
    /**
     * The path where the library.json file should be stored by default.
     */
    private static final String DEFAULT_STORAGE_PATH = "target/user.json";

    /**
     * The file path where the json data will be stored.
     */
    private Path storagePath;

    /**
     * @param path the file path where the json data will be stored
     */
    public StorageHandler(final String path) throws IOException {

        setStoragePath(path);

    }

    /**
     * Default constructor of storageHandler, creating a storageHandler with the
     * default storage path.
     *
     * @throws IOException
     */
    public StorageHandler() throws IOException {
        this(StorageHandler.DEFAULT_STORAGE_PATH);
    }

    /**
     * Change the location at which the library entries should be stored. Attempts
     * to create a new file at this location.
     *
     * @param path new storage location
     */
    public void setStoragePath(final String path) throws IOException {

        this.storagePath = Paths.get(path);

        if (!Files.exists(storagePath)) {

            try {
                Files.createFile(storagePath);
            } catch (IOException e) {
                throw new IOException("Exception when creating file");
            }

        }

    }

    /**
     * Stores the given user in the backend.
     *
     * @param user the user you want to store
     * @throws IOException if the StorageHandler fails to store the user
     */
    public void storeUserInRemote(final User user) throws IOException {
        // Temporary code
    }

    /**
     * Stores the given user in the local user.json file.
     *
     * @param user
     * @throws IOException
     */
    public void storeUserLocally(final User user) throws IOException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateDeserializer());
        final Gson gson = gsonBuilder.setPrettyPrinting().create();
        final Writer writer = Files.newBufferedWriter(storagePath);
        gson.toJson(user, writer);
        writer.close();
    }

    /**
     * Updates the user attributes locally and in the server.
     *
     * @param user the user you want to update to
     * @throws IOException
     */
    public void updateUser(final User user) throws IOException {
        String localName;
        try {
            localName = getLocalUser().getUserName();
        } catch (Exception e) {
            localName = "";
        }
        if (localName.equals("") || user.getUserName().equals(localName)) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
            gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateDeserializer());
            final Gson gson = gsonBuilder.setPrettyPrinting().create();
            final Writer writer = Files.newBufferedWriter(storagePath);
            gson.toJson(user, writer);
            writer.close();

            // Also code to update this user in the back end
        } else {
            throw new IOException("Cannot update user with that username");
        }

    }

    /**
     * Fetches the user from the information saved locally.
     *
     * @return the the user saved locally
     * @throws IOException if the StorageHandler fails to read the JSON file
     */
    public User getLocalUser() throws IOException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateDeserializer());
        final Gson gson = gsonBuilder.setPrettyPrinting().create();
        final Reader reader = Files.newBufferedReader(storagePath);
        final User user = gson.fromJson(reader, new TypeToken<User>() {
        }.getType());
        reader.close();
        return user;

    }

    /**
     * Fetches user by username from the backend.
     *
     * @param userName
     * @return the user with that username from the backend
     * @throws IOExeption if it does not find a user with that username
     */
    public User fetchUserFromRemote(final String userName) throws IOException {
        // This is temporary code
        if (!userName.equals("sigmund")) {
            throw new IOException("Could not find user with given username");
        }
        final int fourteen = 14;
        return new User("sigmund", fourteen);
    }

    /**
     * @return all the user names from the backend.
     */
    public List<String> fetchAllUserNamesFromRemote() {
        // This is temporary code
        return List.of("sigmund");
    }

}
