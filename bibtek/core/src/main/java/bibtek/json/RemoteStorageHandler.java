package bibtek.json;

import bibtek.core.User;
import bibtek.core.UserMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import javax.ws.rs.core.MediaType;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

public final class RemoteStorageHandler implements UserMapHandler {

    private URI endPointBaseUri;

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
            .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
            .setPrettyPrinting().create();

    private UserMap userMap;


    RemoteStorageHandler() throws URISyntaxException {

        endPointBaseUri = new URI("http://localhost:8080/bibtek/users/");

    }


    @Override
    public UserMap getUserMap() {

        if (userMap == null) {

            final Client client = Client.create();

            final WebResource webResource = client
                    .resource(endPointBaseUri);

            final ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON)
                    .get(ClientResponse.class);

            final String responseString = response.getEntity(String.class);

            this.userMap = gson.fromJson(responseString, new TypeToken<UserMap>() {
            }.getType());

        }

        return userMap;

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

            final ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON)
                    .get(ClientResponse.class);

            if (response.getClientResponseStatus() == ClientResponse.Status.OK) {

                final String responseString = response.getEntity(String.class);

                user = gson.fromJson(responseString, new TypeToken<User>() {
                }.getType());

                getUserMap().putUser(user);

            }

        }

        return user;
    }

    @Override
    public void putUser(final User user) {

        final Client client = Client.create();

        final WebResource webResource = client
                .resource(uriForUser(user.getUserName()));

        final String input = gson.toJson(user);

        final ClientResponse response = webResource.type(MediaType.APPLICATION_JSON)
                .put(ClientResponse.class, input);

        final String responseString = response.getEntity(String.class);

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

        final ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON)
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
