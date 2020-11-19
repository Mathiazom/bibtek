package bibtek.restapi;


import bibtek.core.User;
import bibtek.core.UserMap;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path(UserMapService.USER_MAP_SERVICE_PATH)
public final class UserMapService {

    /**
     * Root service path.
     */
    public static final String USER_MAP_SERVICE_PATH = "users";

    @Inject
    private UserMap userMap;

    /**
     * Gives access to the map of all users currently in storage.
     *
     * @return userMap
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public UserMap getUserMap() {
        return userMap;
    }

    /**
     * Redirect request on specific user to UserResource.
     *
     * @param username for the associated User
     * @return UserResource for further request handling
     */
    @Path("/{username}")
    public UserResource getUser(@PathParam("username") final String username) {

        final User user = getUserMap().getUser(username);

        return new UserResource(userMap, user);

    }


}
