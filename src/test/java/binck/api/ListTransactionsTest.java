package binck.api;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ListTransactionsTest extends ApiTest {

    @Test
    public void listTransactions() {
        String token = aToken();
        List<Account> accounts = binckClient.listAccounts(token);

        Iterable<Transaction> allTransactions = binckClient.listAllTransactions(token, accounts.get(0).number);

        assertThat(allTransactions).hasSize(107);
        Transaction mostRecentTransaction = allTransactions.iterator().next();
        assertThat(mostRecentTransaction.number).isEqualTo(107);
        assertThat(mostRecentTransaction.transactionDate).isNotNull();
    }
}
