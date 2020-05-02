package binck.api;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TestConfig {

    public String clientId;
    public String clientSecret;
    public String realm;
    public String scope;
    public String redirectUri;
    public String username;
    public String password;

    private static TestConfig testConfig;

    public static TestConfig retrieve() {
        if (testConfig == null) {
            testConfig = load();
        }
        return testConfig;
    }

    private static TestConfig load() {
        InputStream testConfigOnClasspath = TestConfig.class.getResourceAsStream("/test-config.json");
        if (testConfigOnClasspath == null) {
            return loadFromEnvironment();
        }
        return load(testConfigOnClasspath);
    }

    private static TestConfig loadFromEnvironment() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private static TestConfig load(InputStream stream) {
        try {
            try (JsonReader reader = new JsonReader(new InputStreamReader(stream))) {
                return new Gson().fromJson(reader, TestConfig.class);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
