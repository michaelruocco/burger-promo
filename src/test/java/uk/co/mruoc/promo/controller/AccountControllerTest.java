package uk.co.mruoc.promo.controller;

import org.junit.jupiter.api.Test;
import uk.co.mruoc.promo.entity.account.Account;
import uk.co.mruoc.promo.usecase.account.AccountService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class AccountControllerTest {

    private final AccountService service = mock(AccountService.class);

    private final AccountController controller = new AccountController(service);

    @Test
    void shouldGetAccountIfExists() {
        String accountId = "123456";
        Account expectedAccount = mock(Account.class);
        given(service.forceFind(accountId)).willReturn(expectedAccount);

        Account account = controller.getAccount(accountId);

        assertThat(account).isEqualTo(expectedAccount);
    }

}
