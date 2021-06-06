package uk.co.mruoc.promo.entity.account;


import java.util.stream.IntStream;
import java.util.stream.Stream;

public class AccountFactory {

    public Stream<Account> buildAccounts(int numberToBuild) {
        return IntStream.rangeClosed(1, numberToBuild)
                .mapToObj(Integer::toString)
                //.map(s -> StringUtils.leftPad(s, 8, '0'))
                .map(this::buildAccount);
    }

    public Account buildAccount(String id) {
        return Account.builder()
                .id(id)
                .build();
    }

}
