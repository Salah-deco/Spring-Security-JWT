package org.sid.authjwt.security.web;

import org.sid.authjwt.security.entities.AppUser;
import org.sid.authjwt.security.services.AccountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AccountRestController {
    private AccountService accountService;

    public AccountRestController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping(path =  "/users")
    public List<AppUser> appUsers() {
        return accountService.listUsers();
    }
}
