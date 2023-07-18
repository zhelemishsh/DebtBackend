package sas.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sas.backend.model.Account;
import sas.backend.repository.AccountRepository;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class AuthenticationService implements UserDetailsService {
    private final AccountRepository accountRepository;

    @Autowired
    public AuthenticationService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username);
        if(account == null) {
            throw new UsernameNotFoundException("Account " + username + " not found");
        }

        return new User(account.getUsername(), account.getPassword(), createAuthorities(account));
    }

    private Collection<GrantedAuthority> createAuthorities(Account account) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_DEFAULT"));
        return authorities;
    }
}
