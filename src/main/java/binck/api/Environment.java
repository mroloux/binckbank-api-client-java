package binck.api;

public class Environment {

    public final String loginDomain;
    public final String apiDomain;

    public final static Environment PROD = new Environment("login.binck.com", "api.binck.com");
    public final static Environment SANDBOX = new Environment("login.sandbox.binck.com", "api.sandbox.binck.com");

    public Environment(String loginDomain, String apiDomain) {
        this.loginDomain = loginDomain;
        this.apiDomain = apiDomain;
    }
}
