package uk.co.mruoc.promo.entity.account;


import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AccountFactory {

    public Collection<Account> buildAccounts(int numberToBuild) {
        return IntStream.rangeClosed(1, numberToBuild)
                .mapToObj(Integer::toString)
                .map(this::buildAccount)
                .collect(Collectors.toList());
    }

    public Account buildAccount(String id) {
        return Account.builder()
                .id(id)
                .build();
    }

}
