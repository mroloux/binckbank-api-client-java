package binck.api;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ListAccountsTest extends ApiTest {

    @Test
    public void listAccounts() {
        List<Account> accounts = binckClient.listAccounts(aToken());

        assertThat(accounts).isNotEmpty();
    }
}
