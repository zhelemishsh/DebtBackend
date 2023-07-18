package sas.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sas.backend.dto.AccountCreationDto;
import sas.backend.dto.AccountDto;
import sas.backend.exception.AccountsServiceException;
import sas.backend.service.AccountsService;

@RestController
@RequestMapping(value = "/accounts")
public class AccountsController {
    private final AccountsService accountsService;

    @Autowired
    public AccountsController(AccountsService accountsService) {
        this.accountsService = accountsService;
    }

    @GetMapping("/get")
    public AccountDto getAccount() throws AccountsServiceException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return accountsService.getAccount(username);
    }

    @PostMapping("/create")
    public void createAccount(@RequestBody AccountCreationDto accountDto) throws AccountsServiceException {
        accountsService.createAccount(accountDto);
    }

    @PutMapping("/update")
    public void updateAccount(@RequestBody AccountCreationDto accountDto) throws AccountsServiceException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        accountsService.updateAccount(username, accountDto);
    }

    @DeleteMapping("/delete")
    public void deleteAccount() throws AccountsServiceException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        accountsService.deleteAccount(username);
    }
}
