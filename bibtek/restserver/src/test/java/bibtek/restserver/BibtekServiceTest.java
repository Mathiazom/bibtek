package bibtek.restserver;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import com.google.gson.reflect.TypeToken;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.grizzly.GrizzlyTestContainerFactory;
import org.glassfish.jersey.test.spi.TestContainerException;
import org.glassfish.jersey.test.spi.TestContainerFactory;

import bibtek.core.User;
import bibtek.core.UserMap;
import bibtek.restapi.UserMapService;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the server responses.
 */
public class BibtekServiceTest extends JerseyTest {

    /**
     * Configures the test.
     */
    @Override
    protected ResourceConfig configure() {
        final BibtekConfig config = new BibtekConfig();
        return config;
    }

    /**
     * Gets...
     */
    @Override
    protected TestContainerFactory getTestContainerFactory() throws TestContainerException {
        return new GrizzlyTestContainerFactory();
    }

    private GsonProvider gsonProvider;

    /**
     * Sets up the test.
     */
    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();
        gsonProvider = new GsonProvider();
        // gsonProvider = new TodoModuleObjectMapperProvider().getContext(getClass());
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
        InputStream is = response.readEntity(InputStream.class);
        UserMap actualUserMap;
        try {
            actualUserMap = (UserMap) gsonProvider.readFrom(Object.class, new TypeToken<UserMap>() {
            }.getType(), getClass().getAnnotations(), MediaType.APPLICATION_JSON_TYPE, response.getStringHeaders(), is);
        } catch (IOException e) {
            e.printStackTrace();
            fail("IOException thrown");
            return;
        }
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
        InputStream is = response.readEntity(InputStream.class);
        User actualUser;
        try {
            actualUser = (User) gsonProvider.readFrom(Object.class, new TypeToken<User>() {
            }.getType(), getClass().getAnnotations(), MediaType.APPLICATION_JSON_TYPE, response.getStringHeaders(), is);
        } catch (IOException e) {
            e.printStackTrace();
            fail("IOException thrown");
            return;
        }
        assertEquals(dante, actualUser, "The response from the GET getUser was not as expected");
    }

    /**
     * Testing the PUT request method in UserResource.
     */
    @Test
    public void putUserPUTTest() {
        Entity<User> userEntity = Entity.entity(ServerUtil.VERGIL_USER, MediaType.APPLICATION_JSON);
        Response response = target(UserMapService.USER_MAP_SERVICE_PATH).path("vergil")
                .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8").put(userEntity);
        assertEquals(HttpURLConnection.HTTP_OK, response.getStatus());

    }

}
