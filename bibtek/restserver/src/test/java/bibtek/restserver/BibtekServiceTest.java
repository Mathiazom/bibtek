package bibtek.restserver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import java.net.HttpURLConnection;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.grizzly.GrizzlyTestContainerFactory;
import org.glassfish.jersey.test.spi.TestContainerException;
import org.glassfish.jersey.test.spi.TestContainerFactory;

import bibtek.core.User;
import bibtek.core.UserMap;
import bibtek.restapi.UserMapService;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.reflect.TypeToken;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the server responses.
 */
public class BibtekServiceTest extends JerseyTest {

    /**
     * Provider of the gson used in this class.
     */
    private GsonProvider gsonProvider;

    /*
     * public BibtekServiceTest() throws Exception { super(this.clientConfig(new
     * DefaultClientConfig(GsonProvider.class))
     *
     * service = new ServiceRequester(resource()); }
     */

    /**
     * Configures the test.
     */
    @Override
    protected ResourceConfig configure() {
        final BibtekConfig config = new BibtekConfig();
        return config;
    }

    /**
     * Gets Grizzly factory.
     */
    @Override
    protected TestContainerFactory getTestContainerFactory() throws TestContainerException {
        return new GrizzlyTestContainerFactory();
    }

    /**
     * Sets up the test.
     */
    @BeforeEach
    @Override
    public void setUp() throws Exception {
        gsonProvider = new GsonProvider();
        super.setUp();
    }

    /**
     * Tears down after each test.
     */
    @AfterEach
    public void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Tests the GET method in UserMapService that should return the whole user map.
     */
    @Test
    public void getUserMapGETTest() {
        Response response = target(UserMapService.USER_MAP_SERVICE_PATH)
                .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8").get();
        // Test if the response is ok.
        assertEquals(HttpURLConnection.HTTP_OK, response.getStatus());

        // Test if the response is like the expected response. he
        UserMap expectedUserMap = new UserMap();
        expectedUserMap.putUser(ServerUtil.DANTE_USER);
        if (!expectedUserMap.iterator().hasNext()) {
            fail("Returned empty UserMap");
        }
        String responseString = response.readEntity(String.class);
        UserMap actualUserMap = gsonProvider.getGson().fromJson(responseString, new TypeToken<UserMap>() {
        }.getType());

        assertEquals(expectedUserMap, actualUserMap, "The response from the GET getUserMap was not as expected");
    }

    /**
     * Testing the GET request method in UserResource.
     */
    @Test
    public void getUserGETTest() {
        Response response = target(UserMapService.USER_MAP_SERVICE_PATH).path("dante")
                .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8").get();

        // Test if the request if ok
        assertEquals(HttpURLConnection.HTTP_OK, response.getStatus());

        // Test if it returns expected value
        User dante = ServerUtil.DANTE_USER;
        String responseString = response.readEntity(String.class);
        User actualUser = gsonProvider.getGson().fromJson(responseString, new TypeToken<User>() {
        }.getType());
        assertEquals(dante, actualUser, "The response from the GET getUser was not as expected");
    }

    /**
     * Testing the PUT request method in UserResource.
     */
    @Test
    public void putUserPUTTest() {
        // Setup the entity to PUT
        Entity<String> userEntity = Entity
                .entity(gsonProvider.getGson().toJson(ServerUtil.DANTE_USER_EDITED, new TypeToken<User>() {
                }.getType()), MediaType.APPLICATION_JSON_TYPE);

        // Send the PUT request
        target(UserMapService.USER_MAP_SERVICE_PATH).path("dante")
                .request(MediaType.APPLICATION_JSON_TYPE + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8")
                .put(userEntity);
        // Make a GET request to see the results
        Response response = target(UserMapService.USER_MAP_SERVICE_PATH).path("dante")
                .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8").get();
        // Test if it worked
        assertEquals(HttpURLConnection.HTTP_OK, response.getStatus());

        // Test if the request actaully updates that user.
        User danteUpdated = ServerUtil.DANTE_USER_EDITED;
        String responseString = response.readEntity(String.class);
        User actualUser = gsonProvider.getGson().fromJson(responseString, new TypeToken<User>() {
        }.getType());
        assertEquals(danteUpdated, actualUser, "The response from the GET getUser was not as expected");

    }

    /**
     * Test if doing a PUT request on a new user actaully saves a new user.
     */
    @Test
    public void putUserNewPUTTest() {
        // Setup the entitiy to PUT
        Entity<String> userEntity = Entity
                .entity(gsonProvider.getGson().toJson(ServerUtil.VERGIL_USER, new TypeToken<User>() {
                }.getType()), MediaType.APPLICATION_JSON_TYPE);

        // Send the PUT request
        target(UserMapService.USER_MAP_SERVICE_PATH).path("dante")
                .request(MediaType.APPLICATION_JSON_TYPE + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8")
                .put(userEntity);
        // Get the newly PUT user
        Response response = target(UserMapService.USER_MAP_SERVICE_PATH).path("vergil")
                .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8").get();
        // Test if that user exists
        assertEquals(HttpURLConnection.HTTP_OK, response.getStatus());

        // Test if the response is as expected
        User vergil = ServerUtil.VERGIL_USER;
        String responseString = response.readEntity(String.class);
        User actualUser = gsonProvider.getGson().fromJson(responseString, new TypeToken<User>() {
        }.getType());
        assertEquals(vergil, actualUser, "The response from the GET getUser was not as expected");

    }

    /**
     * Test if the DELETE request method works.
     */
    @Test
    public void removeUserDELETETest() {
        // Send the DELETE request
        target(UserMapService.USER_MAP_SERVICE_PATH).path("dante")
                .request(MediaType.APPLICATION_JSON_TYPE + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8").delete();
        // Send a GET request for dante to test if it returns nothing
        Response response = target(UserMapService.USER_MAP_SERVICE_PATH).path("dante")
                .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8").get();
        // Test if that user exists (it should not)
        assertEquals(HttpURLConnection.HTTP_NO_CONTENT, response.getStatus());
    }

}
