package binck.api;

import com.google.common.base.Function;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.openqa.selenium.By.id;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public class AuthorizationCodeGenerator {

    public static String retrieveAuthorizationCode(Environment environment, String realm, String clientId, String scope, String redirectUri, String username, String password) {
        WebDriver driver = chromeDriver();
        try {
            return retrieveAuthorizationCode(driver, environment, realm, clientId, scope, redirectUri, username, password);
        } finally {
            driver.quit();
        }
    }

    private static String retrieveAuthorizationCode(WebDriver driver, Environment environment, String realm, String clientId, String scope, String redirectUri, String username, String password) {
        String encodedRedirectUri = URLEncoder.encode(redirectUri, StandardCharsets.UTF_8);
        driver.get("https://" + environment.loginDomain + "/am/oauth2/realms/" + realm + "/authorize?client_id=" + clientId + "&scope=" + scope + "&state=1234zyx&response_type=code&redirect_uri=" + encodedRedirectUri);
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement element = wait.until(visibilityOfElementLocated(id("idToken2")));
        element.sendKeys(username);
        driver.findElement(id("idToken3")).sendKeys(password);
        driver.findElement(id("idToken4_0")).click();
        wait.until((Function<WebDriver, Boolean>) d -> d.getCurrentUrl().startsWith(redirectUri));
        try {
            URL url = new URL(driver.getCurrentUrl());
            return splitQuery(url).get("code");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private static WebDriver chromeDriver() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");
        return new ChromeDriver(chromeOptions);
    }

    public static Map<String, String> splitQuery(URL url) {
        Map<String, String> query_pairs = new LinkedHashMap<>();
        for (String pair : url.getQuery().split("&")) {
            int idx = pair.indexOf("=");
            query_pairs.put(URLDecoder.decode(pair.substring(0, idx), StandardCharsets.UTF_8), URLDecoder.decode(pair.substring(idx + 1), StandardCharsets.UTF_8));
        }
        return query_pairs;
    }

}
