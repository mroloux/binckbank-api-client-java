package binck.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import static binck.api.util.BinckGson.gson;

public class BinckClient {

    private final OkHttpClient httpClient = new OkHttpClient();

    private Environment environment;

    public BinckClient(Environment environment) {
        this.environment = environment;
    }

    public String retrieveToken(String code, String clientId, String clientSecret, String redirectUri, String realm) {
        FormBody body = new FormBody.Builder()
                .add("grant_type", "authorization_code")
                .add("client_id", clientId)
                .add("client_secret", clientSecret)
                .add("redirect_uri", redirectUri)
                .add("code", code)
                .build();
        Request request = new Request.Builder()
                .url("https://" + environment.loginDomain + "/am/oauth2/realms/" + realm + "/access_token")
                .post(body)
                .build();
        try {
            try (Response response = httpClient.newCall(request).execute()) {
                assertOk(response);
                String responseBody = response.body().string();
                return JsonParser.parseString(responseBody)
                        .getAsJsonObject()
                        .getAsJsonPrimitive("access_token")
                        .getAsString();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Account> listAccounts(String token) {
        Request request = new Request.Builder()
                .url("https://" + environment.apiDomain + "/api/v1/accounts")
                .header("Authorization", "Bearer " + token)
                .get()
                .build();
        try {
            try (Response response = httpClient.newCall(request).execute()) {
                assertOk(response);
                String responseBody = response.body().string();
                JsonArray accountsJson = JsonParser.parseString(responseBody)
                        .getAsJsonObject()
                        .getAsJsonObject("accountsCollection")
                        .getAsJsonArray("accounts");
                Type listType = new TypeToken<List<Account>>() {
                }.getType();
                return gson().fromJson(accountsJson, listType);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Transaction> listTransactions(String token, String accountNumber) {
        Request request = new Request.Builder()
                .url("https://" + environment.apiDomain + "/api/v1/accounts/" + accountNumber + "/transactions")
                .header("Authorization", "Bearer " + token)
                .get()
                .build();
        try {
            try (Response response = httpClient.newCall(request).execute()) {
                assertOk(response);
                String responseBody = response.body().string();
                JsonArray transactionsJson = JsonParser.parseString(responseBody)
                        .getAsJsonObject()
                        .getAsJsonObject("transactionsCollection")
                        .getAsJsonArray("transactions");
                Type listType = new TypeToken<List<Transaction>>() {
                }.getType();
                return gson().fromJson(transactionsJson, listType);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void assertOk(Response response) throws IOException {
        if (!response.isSuccessful()) {
            throw new RuntimeException(response.code() + ": " + response.body().string());
        }
    }
}
