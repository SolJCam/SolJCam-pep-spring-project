package com.example.service;

import org.springframework.stereotype.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.repository.AccountRepository;
import com.example.entity.Account;

@Service
public class AccountService {
    @Autowired
    AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public Account registerAccount(String username, String password){
        // check if the username is blank or the password is at least 4 characters long
        if(username == "" || password.length() < 4){
            return null;
        }
        // find account if it already exists
        Account existingAccount = accountRepository.accountExists(username);
        // return account object
        if (existingAccount == null) {
            Account newAccount = new Account(username, password);
            return accountRepository.save(newAccount);
        }else{
            Account duplicateAccount = new Account("","");
            return duplicateAccount;
        }
    }

    public Account login(String username, String password){
        Account account = accountRepository.accountLogin(username, password);
        return account;
    }
}
