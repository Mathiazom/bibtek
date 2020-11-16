package bibtek.restserver;

import bibtek.core.User;
import bibtek.core.UserMap;
import bibtek.restapi.UserMapService;

import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

public final class BibtekConfig extends ResourceConfig {

        /**
         * Setup server configs.
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
         * Setup server with default data source.
         */
        public BibtekConfig() {
                this(createDefaultUserMap());
        }

        /**
         *
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
