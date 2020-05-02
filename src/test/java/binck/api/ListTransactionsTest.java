package binck.api;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ListTransactionsTest extends ApiTest {

    @Test
    public void listTransactions() {
        String token = aToken();
        List<Account> accounts = binckClient.listAccounts(token);

        List<Transaction> transactions = binckClient.listTransactions(token, accounts.get(0).number);

        assertThat(transactions).isNotEmpty();
    }
}
