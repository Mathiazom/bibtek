package bibtek.json;

import bibtek.core.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import javax.ws.rs.core.MediaType;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import static bibtek.json.UserMapHandler.Status.ERROR;
import static bibtek.json.UserMapHandler.Status.NOT_FOUND;
import static bibtek.json.UserMapHandler.Status.OK;

public final class RemoteStorageHandler implements UserMapHandler<UserMapHandler.Status> {

    /**
     * Base path to reach remote server.
     */
    private static final String REMOTE_BASE_PATH = "http://localhost:8080/bibtek/users/";
    private URI remoteBaseURI;

    /**
     * JSON serializer/deserializer.
     */
    private final Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
            .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer()).setPrettyPrinting().create();

    /**
     * Convert http status code to {@link UserMapHandler.Status}.
     *
     * @param statusCode to convert
     * @return status
     */
    static Status statusFromHttp(final int statusCode) {

        switch (statusCode) {
            case HttpURLConnection.HTTP_NO_CONTENT:
            case HttpURLConnection.HTTP_OK:
                return OK;
            case HttpURLConnection.HTTP_NOT_FOUND:
                return NOT_FOUND;
            default:
                return ERROR;
        }

    }

    /**
     * Parse remote server base path.
     *
     * @throws URISyntaxException if path is invalid
     */
    RemoteStorageHandler() throws URISyntaxException {
        remoteBaseURI = new URI(REMOTE_BASE_PATH);
    }

    /**
     * Construct path for requests related to user with given username.
     *
     * @param username of user
     * @return remote server URI
     */
    private URI uriForUser(final String username) {
        return remoteBaseURI.resolve(URLEncoder.encode(username, StandardCharsets.UTF_8));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User getUser(final String username) {

        final Client client = Client.create();
        final WebResource webResource = client.resource(uriForUser(username));
        final ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);

        if (response.getClientResponseStatus() == ClientResponse.Status.OK) {

            final String responseString = response.getEntity(String.class);
            return gson.fromJson(responseString, new TypeToken<User>() {
            }.getType());

        }

        return null;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Status putUser(final User user) {

        final Client client = Client.create();
        final WebResource webResource = client.resource(uriForUser(user.getUserName()));
        final String input = gson.toJson(user);

        final ClientResponse response = webResource.type(MediaType.APPLICATION_JSON).put(ClientResponse.class, input);

        System.out.println("Putted user with response " + response.getStatus() + " (" + statusFromHttp(response.getStatus()) + ")");

        return statusFromHttp(response.getStatus());

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Status removeUser(final String username) {

        final Client client = Client.create();
        final WebResource webResource = client.resource(uriForUser(username));
        final ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON).delete(ClientResponse.class);

        return statusFromHttp(response.getStatus());

    }

    /**
     * @see #putUser(User).
     */
    @Override
    public Status notifyUserChanged(final User user) {
        return putUser(user);
    }

}
