package bibtek.ui;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.testfx.framework.junit5.ApplicationTest;

public class WireMockApplicationTest extends ApplicationTest {

    private WireMockConfiguration config;
    private WireMockServer wireMockServer;

    @BeforeEach
    public void startWireMockServerAndSetup() {
        config = WireMockConfiguration.wireMockConfig().port(8080);
        wireMockServer = new WireMockServer(config.portNumber());
        wireMockServer.start();
        WireMock.configureFor("localhost", config.portNumber());
    }

    @AfterEach
    public void stopWireMockServer() {
        wireMockServer.stop();
    }

}
