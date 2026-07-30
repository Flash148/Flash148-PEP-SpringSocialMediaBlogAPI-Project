package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.exception.DuplicateUsernameException;
import com.example.exception.InvalidLoginException;
import com.example.exception.InvalidRegistrationException;
import com.example.repository.AccountRepository;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    public void validateUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new InvalidRegistrationException("Username cannot be blank");
        }
    }

    public void validatePassword(String password) {
        if (password == null || password.length() < 4) {
            throw new InvalidRegistrationException("Password must be at least 4 characters long");
        }
    }

    public void checkDuplicateUsername(String username) {
        if (accountRepository.findByUsername(username).isPresent()) {
            throw new DuplicateUsernameException("Username already exists");
        }
    }

    public Account registerAccount(String username, String password) {
        validateUsername(username);
        validatePassword(password);
        checkDuplicateUsername(username);
        Account account = new Account(username, password);
        return accountRepository.save(account);
    }

    public Account login(String username, String password) {
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new InvalidLoginException("Invalid username or password"));
        if (!account.getPassword().equals(password)) {
            throw new InvalidLoginException("Invalid username or password");
        }
        return account;
    }
}
