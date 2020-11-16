package bibtek.restapi;


import bibtek.core.User;
import bibtek.core.UserMap;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Handler of requests concerning a single User.
 */
public final class UserResource {

    private final UserMap userMap;
    private final User user;

    /**
     * @param userMap  holding all users
     * @param user     associated with request
     */
    public UserResource(final UserMap userMap, final User user) {

        this.userMap = userMap;
        this.user = user;

    }

    /**
     * Checks if current user associated with request path is valid.
     *
     * @return true if user is valid, false otherwise
     */
    private boolean hasValidUser() {

        return user != null;

    }

    /**
     * Handle request to access User object associated with username in path.
     *
     * @return user object, or null if not available
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public User getUser() {
        if (!hasValidUser()) {
            return null;
        }
        return this.user;
    }

    /**
     * Handle request to update user, or add if not already in storage.
     *
     * @param user to be updated/added
     * @return true if user was added/update, false otherwise
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public boolean putUser(final User user) {
        this.userMap.putUser(user);
        return true;
    }

    /**
     * Handle request to remove user from storage.
     *
     * @return true if user was removed, false otherwise
     */
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public boolean removeUser() {
        if (!hasValidUser()) {
            return false;
        }
        userMap.removeUser(user);
        return true;
    }


}
