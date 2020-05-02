package binck.api;

import static binck.api.AuthorizationCodeGenerator.retrieveAuthorizationCode;
import static binck.api.Environment.SANDBOX;

public class ApiTest {

    private static String authorizationCode;
    private static String token;
    protected static Environment environment = SANDBOX;

    protected static BinckClient binckClient = new BinckClient(environment);

    protected static String anAuthorizationCode() {
        if (authorizationCode == null) {
            TestConfig config = TestConfig.retrieve();
            authorizationCode = retrieveAuthorizationCode(environment, config.realm, config.clientId, config.scope, config.redirectUri, config.username, config.password);
        }
        return authorizationCode;
    }

    protected static String aToken() {
        if (token == null) {
            TestConfig config = TestConfig.retrieve();
            token = binckClient.retrieveToken(anAuthorizationCode(), config.clientId, config.clientSecret, config.redirectUri, config.realm);
        }
        return token;
    }
}
