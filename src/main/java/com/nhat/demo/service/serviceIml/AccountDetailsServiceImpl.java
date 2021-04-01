package com.nhat.demo.service.serviceIml;

import com.nhat.demo.entity.Account;
import com.nhat.demo.entity.Role;
import com.nhat.demo.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccountDetailsServiceImpl implements UserDetailsService {

  @Autowired
  private AccountRepository accountRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    com.nhat.demo.entity.Account account = accountRepository.findByUsername(username);

    if (account == null) {
      System.out.println("User not found! " + username);
      throw new UsernameNotFoundException("User " + username + " was not found in the database");
    }
    Role roleNames = account.getRole();
    List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
    GrantedAuthority authority = new SimpleGrantedAuthority(roleNames.getRoleName());
    grantList.add(authority);

    UserDetails userDetails =  new User(account.getUsername(),
            account.getPassword(), grantList);
    
    return userDetails;
  }

}
