package bibtek.json;

import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import bibtek.core.User;
import bibtek.core.UserMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import com.google.gson.reflect.TypeToken;

/**
 * Class responsible for reading and writing user to locally stored json files.
 */
public final class StorageHandler implements UserMapHandler {
    /**
     * The path where the library.json file should be stored by default.
     */
    private static final String DEFAULT_STORAGE_PATH = "target/user.json";

    /**
     * The file path where the json data will be stored.
     */
    private Path storagePath;


    private URI endPointBaseUri;

    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
            .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
            .setPrettyPrinting().create();

    private UserMap userMap;


    /**
     * @param path the file path where the json data will be stored
     */
    public StorageHandler(final String path) throws IOException {

        setStoragePath(path);

        try {
            endPointBaseUri = new URI("http://localhost:8080/bibtek/users/");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

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

    private UserMap getUserMap() {

        if (userMap == null) {

            final HttpRequest request = HttpRequest.newBuilder(endPointBaseUri)
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            try {
                final HttpResponse<String> response = HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());
                final String responseString = response.body();
                this.userMap = gson.fromJson(responseString, new TypeToken<UserMap>() {
                }.getType());
            } catch (InterruptedException | IOException e) {
                throw new RuntimeException(e);
            }

        }

        return userMap;

    }

    /**
     * Fetches the user from the information saved locally.
     *
     * @return the the user saved locally
     * @throws IOException if the StorageHandler fails to read the JSON file
     */
    public User getLocalUser() throws IOException {
        final Reader reader = Files.newBufferedReader(storagePath);
        final User user = gson.fromJson(reader, new TypeToken<User>() {
        }.getType());
        reader.close();
        return user;

    }

    @Override
    public boolean hasUser(final String username) {
        return getUserMap().hasUser(username);
    }

    @Override
    public Collection<String> getUsernames() {
        final Collection<String> usernames = new ArrayList<>();
        getUserMap().forEach(user -> usernames.add(user.getUserName()));
        return usernames;
    }


    private URI uriForUser(final String username) {

        return endPointBaseUri.resolve(URLEncoder.encode(username, StandardCharsets.UTF_8));

    }

    @Override
    public User getUser(final String username) {
        User user = getUserMap().getUser(username);

        if (user == null) {

            final Client client = Client.create();

            final WebResource webResource = client
                    .resource(uriForUser(username));

            ClientResponse response = webResource.accept("application/json")
                    .get(ClientResponse.class);

            final String responseString = response.getEntity(String.class);

            user = gson.fromJson(responseString, new TypeToken<User>() {
            }.getType());
            getUserMap().putUser(user);

        }

        return user;
    }

    @Override
    public void putUser(final User user) {

        final Client client = Client.create();

        final WebResource webResource = client
                .resource(uriForUser(user.getUserName()));

        final String input = gson.toJson(user);

        final ClientResponse response = webResource.type("application/json")
                .put(ClientResponse.class, input);

        final String responseString = response.getEntity(String.class);

        System.out.println(responseString);

        final Boolean added = gson.fromJson(responseString, Boolean.class);
        if (added != null) {
            getUserMap().putUser(user);
        }

    }

    @Override
    public void removeUser(final String username) {

        final Client client = Client.create();

        final WebResource webResource = client
                .resource(uriForUser(username));

        ClientResponse response = webResource.accept("application/json")
                .delete(ClientResponse.class);

        final String responseString = response.getEntity(String.class);

        final Boolean removed = gson.fromJson(responseString, Boolean.class);
        if (removed != null) {
            getUserMap().removeUser(getUserMap().getUser(username));
        }

    }

    @Override
    public void notifyUserChanged(final User user) {
        putUser(user);
    }
}
