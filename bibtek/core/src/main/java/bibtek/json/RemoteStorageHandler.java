package bibtek.json;

import bibtek.core.User;
import bibtek.core.UserMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

public final class RemoteStorageHandler implements UserMapHandler {

    private URI endPointBaseUri;

    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
            .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
            .setPrettyPrinting().create();

    private UserMap userMap;


    RemoteStorageHandler() throws URISyntaxException {

        endPointBaseUri = new URI("http://localhost:8080/bibtek/users/");

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

        final ClientResponse response = webResource.type("application/json")
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

        final ClientResponse response = webResource.accept("application/json")
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
