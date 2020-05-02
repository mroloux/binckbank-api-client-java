package binck.api;

import org.junit.jupiter.api.Test;

import static binck.api.AuthorizationCodeGenerator.retrieveAuthorizationCode;
import static org.assertj.core.api.Assertions.assertThat;

public class RetrieveTokenTest extends ApiTest {

    @Test
    public void retrieveToken() {
        TestConfig config = TestConfig.retrieve();
        String authorizationCode = retrieveAuthorizationCode(environment, config.realm, config.clientId, config.scope, config.redirectUri, config.username, config.password);

        String token = binckClient.retrieveToken(authorizationCode, config.clientId, config.clientSecret, config.redirectUri, config.realm);

        assertThat(token).isNotBlank();
    }
}
