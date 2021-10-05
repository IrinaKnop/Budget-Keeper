package org.knop.budgetKeeper.service;

import org.knop.budgetKeeper.models.Account;
import org.knop.budgetKeeper.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountServiceImpl implements AccountService{

    @Autowired
    private AccountRepository accountRepository;

    @Override
    @Transactional
    public Account createAccount(String accountName) {
        Account newAccount = new Account(-1, accountName);
        return accountRepository.save(newAccount);
    }
}
