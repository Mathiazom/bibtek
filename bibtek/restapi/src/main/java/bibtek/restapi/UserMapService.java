package bibtek.restapi;


import bibtek.core.User;
import bibtek.core.UserMap;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path(UserMapService.USER_MAP_SERVICE_PATH)
public final class UserMapService {

    /**
     * Root service path.
     */
    public static final String USER_MAP_SERVICE_PATH = "users";

    @Inject
    private UserMap userMap;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public UserMap getUserMap() {
        return userMap;
    }

    @Path("/{username}")
    public UserResource getUser(@PathParam("username") final String username) {

        final User user = getUserMap().getUser(username);

        return new UserResource(userMap, username, user);

    }


}
