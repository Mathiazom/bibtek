package bibtek.restapi;

import bibtek.core.User;
import bibtek.core.UserMap;
import bibtek.json.LocalStorageHandler;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * Handler of server requests concerning a single User.
 */
public final class UserResource {

    private final UserMap userMap;
    private final User user;

    private final LocalStorageHandler localStorageHandler;

    /**
     * @param userMap holding all users
     * @param user    associated with request
     *
     * @throws WebApplicationException INTERNAL_SERVER_ERROR if local persistence fails.
     */
    public UserResource(final UserMap userMap, final User user) {

        this.userMap = userMap;
        this.user = user;

        try {
            this.localStorageHandler = new LocalStorageHandler("../target/remoteUsers");
        } catch (IOException e) {
            e.printStackTrace();
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * Checks if current user associated with request path exists.
     *
     * @return true if user exists, false otherwise
     */
    private boolean userExists() {
        return user != null;
    }

    /**
     * Handles request to access User object associated with username in path.
     *
     * @return user object, or null if not available
     *
     * @throws WebApplicationException NOT_FOUND if user does not exist
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public User getUser() {
        if (!userExists()) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return this.user;
    }

    /**
     * Handles request to update user, or add if not already in storage.
     *
     * @param user to be updated/added
     * @throws WebApplicationException INTERNAL_SERVER_ERROR if local persistence fails.
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void putUser(final User user) {
        if (localStorageHandler.putUser(user) != LocalStorageHandler.Status.OK) {
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
        userMap.putUser(user);
    }

    /**
     * Handles request to remove user from storage.
     *
     * @throws WebApplicationException NOT_FOUND if user does not exist
     *                                 INTERNAL_SERVER_ERROR if local persistence fails.
     */
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public void removeUser() {
        if (!userExists()) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        if (localStorageHandler.removeUser(user.getUserName()) != LocalStorageHandler.Status.OK) {
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
        userMap.removeUser(user);
    }

}
