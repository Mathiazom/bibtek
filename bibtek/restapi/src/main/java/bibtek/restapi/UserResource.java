package bibtek.restapi;


import bibtek.core.User;
import bibtek.core.UserMap;
import bibtek.json.LocalDateDeserializer;
import bibtek.json.LocalDateSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.time.LocalDate;

public final class UserResource {

    private final UserMap userMap;
    private final String username;
    private final User user;

    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
            .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
            .setPrettyPrinting().create();


    public UserResource(final UserMap userMap, final String username, final User user) {

        this.userMap = userMap;
        this.username = username;
        this.user = user;

    }

    private void validateUser() {

        if (user == null) {
            throw new IllegalArgumentException(("No user with username " + username));
        }

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public User getUser() {
        System.out.println("Getting: " + username);
        validateUser();
        return this.user;
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public boolean putUser(final User user) {
        System.out.println("Putting: " + user);
        return this.userMap.putUser(user) == null;
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public boolean removeUser() {
        System.out.println("Deleting: " + username);
        validateUser();
        userMap.removeUser(user);
        return true;
    }


}
