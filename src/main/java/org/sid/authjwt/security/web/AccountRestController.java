package org.sid.authjwt.security.web;

import org.sid.authjwt.security.RequestBodyClasses.RoleUserForm;
import org.sid.authjwt.security.entities.AppRole;
import org.sid.authjwt.security.entities.AppUser;
import org.sid.authjwt.security.services.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping(path = "/user")
    public AppUser saveUser(@RequestBody AppUser appUser) {
        return accountService.addNewUser(appUser);
    }

    @PostMapping(path = "/role")
    public AppRole saveRole(@RequestBody AppRole appRole) {
        return accountService.addNewRole(appRole);
    }

    @PostMapping(path = "/addRoleToUser")
    public void addRoleToUser(@RequestBody RoleUserForm roleUserForm) {
        accountService.addRoleToUser(roleUserForm.getUsername(), roleUserForm.getRoleName());
    }

//    @PostMapping(path = "/addRoleToUser")
//    public ResponseEntity<?> addRoleToUser(@RequestBody RoleUserForm roleUserForm) {
//        try {
//            accountService.addRoleToUser(roleUserForm.getUsername(), roleUserForm.getRoleName());
//            return new ResponseEntity<>(HttpStatus.OK);
//        } catch (Exception e) {
//            System.err.println(e.getMessage());
//            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
//        }
//    }

    @GetMapping(path = "/user/{username}")
    public AppUser getUser(@PathVariable String username) {
        return accountService.loadUserByUsername(username);
    }
}