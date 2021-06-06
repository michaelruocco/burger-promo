package uk.co.mruoc.promo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.co.mruoc.promo.entity.account.Account;
import uk.co.mruoc.promo.usecase.account.AccountService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/accounts")
@Slf4j
public class AccountController {

    private final AccountService service;

    @GetMapping("/{accountId}")
    public Account getAccount(@PathVariable("accountId") String accountId) {
        return service.forceFind(accountId);
    }

}
