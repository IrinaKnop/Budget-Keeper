package org.knop.budgetKeeper.service;

import org.knop.budgetKeeper.models.Account;

public interface AccountService {
    Account createAccount(String accountName);
}
