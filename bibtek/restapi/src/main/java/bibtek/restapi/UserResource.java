package bibtek.restapi;


import bibtek.core.User;
import bibtek.core.UserMap;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

public final class UserResource {

    private final UserMap userMap;
    private final String username;
    private final User user;


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
