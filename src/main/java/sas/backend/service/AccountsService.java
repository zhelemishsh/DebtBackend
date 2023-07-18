package sas.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sas.backend.dto.AccountCreationDto;
import sas.backend.dto.AccountDto;
import sas.backend.exception.AccountsServiceException;
import sas.backend.model.Account;
import sas.backend.repository.AccountRepository;

@Service
public class AccountsService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AccountsService(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AccountDto getAccount(String username) throws AccountsServiceException {
        Account account = getAccountByUsername(username);
        return accountEntityToDto(account);
    }

    public void createAccount(AccountCreationDto accountDto) throws AccountsServiceException {
        if (accountRepository.findByUsername(accountDto.getUsername()) != null) {
            throw new AccountsServiceException(accountDto.getUsername() + " is already taken");
        }
        accountRepository.save(new Account(
                0,
                accountDto.getUsername(),
                passwordEncoder.encode(accountDto.getPassword()),
                accountDto.getEmail()));
    }

    public void updateAccount(String currentUsername, AccountCreationDto accountDto) throws AccountsServiceException {
        Account account = getAccountByUsername(currentUsername);
        if (accountRepository.findByUsername(accountDto.getUsername()) != null) {
            throw new AccountsServiceException(accountDto.getUsername() + " is already taken");
        }
        accountRepository.save(new Account(
                account.getId(),
                accountDto.getUsername(),
                passwordEncoder.encode(accountDto.getPassword()),
                accountDto.getEmail()));
    }

    public void deleteAccount(String username) throws AccountsServiceException {
        Account account = getAccountByUsername(username);
        accountRepository.delete(account);
    }


    private Account getAccountByUsername(String username) throws AccountsServiceException {
        Account account = accountRepository.findByUsername(username);
        if (account == null) {
            throw new AccountsServiceException("Account by " + username + " does not exist");
        }
        return account;
    }

    public AccountDto accountEntityToDto(Account account) {
        return new AccountDto(account.getId(), account.getUsername());
    }
}
