package bibtek.restserver;

import bibtek.core.User;
import bibtek.core.UserMap;
import bibtek.json.LocalStorageHandler;
import bibtek.restapi.UserMapService;

import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;

public final class BibtekConfig extends ResourceConfig {

    private static final String SERVER_PERSISTENCE_DIRECTORY = "target/remoteUsers";

    /**
     * Setup server configs.
     *
     * Specify Gson as JSON serializer
     * Connect to rest API and inject user map
     *
     * @param userMap map of Users to start server with
     */
    public BibtekConfig(final UserMap userMap) {
        register(UserMapService.class);
        register(GsonProvider.class);
        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(userMap);
            }
        });
    }

    /**
     * Retrieve user data persisted in file storage.
     *
     * @return userMap of persisted users
     */
    private static UserMap getPreviouslyLoadedData() {

        UserMap loadedUserMap;
        try {
            // Try to retrieve previously loaded data if it exists.
            final LocalStorageHandler localStorageHandler = new LocalStorageHandler(SERVER_PERSISTENCE_DIRECTORY);
            loadedUserMap = localStorageHandler.getUserMap();
            if (loadedUserMap == null || !loadedUserMap.iterator().hasNext()) {
                loadedUserMap = createDefaultUserMap();
            }
        } catch (IOException e) {
            // Otherwise use the default data
            loadedUserMap = createDefaultUserMap();
        }
        return loadedUserMap;

    }

    /**
     * Setup server with default data source.
     */
    public BibtekConfig() {
        this(BibtekConfig.getPreviouslyLoadedData());
    }

    /**
     * Populate user map with dummy user and book entries.
     *
     * @return UserMap with dummy user
     */
    private static UserMap createDefaultUserMap() {
        final UserMap userMap = new UserMap();
        final User dummyUser = ServerUtil.DANTE_USER;
        userMap.putUser(dummyUser);
        return userMap;
    }

}
