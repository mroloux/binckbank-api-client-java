package binck.api;

import binck.api.collections.IteratorIterable;
import binck.api.collections.Page;
import binck.api.collections.PageIterator;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.function.Function;

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

    public Iterable<Transaction> listAllTransactions(String token, String accountNumber) {
        Function<String, Page<Transaction>> fetcher = range -> this.listTransactions(token, accountNumber, range);
        PageIterator<Transaction> iterator = new PageIterator<>(fetcher);
        return new IteratorIterable<>(iterator);
    }

    public Page<Transaction> listTransactions(String token, String accountNumber, String range) {
        Request request = new Request.Builder()
                .url("https://" + environment.apiDomain + "/api/v1/accounts/" + accountNumber + "/transactions?" + range)
                .header("Authorization", "Bearer " + token)
                .get()
                .build();
        try {
            try (Response response = httpClient.newCall(request).execute()) {
                assertOk(response);
                String responseBody = response.body().string();
                JsonObject responseJson = JsonParser.parseString(responseBody)
                        .getAsJsonObject();
                JsonArray transactionsJson = responseJson
                        .getAsJsonObject("transactionsCollection")
                        .getAsJsonArray("transactions");
                JsonObject paging = responseJson.getAsJsonObject("paging");
                String nextRange = paging.has("next") ? paging.getAsJsonPrimitive("next").getAsString() : null;
                Type listType = new TypeToken<List<Transaction>>() {
                }.getType();
                List<Transaction> transactions = gson().fromJson(transactionsJson, listType);
                return new Page<>(transactions, nextRange);
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
