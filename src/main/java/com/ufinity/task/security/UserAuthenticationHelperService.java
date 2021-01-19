package com.ufinity.task.security;

import com.ufinity.task.model.User;
import com.ufinity.task.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class UserAuthenticationHelperService implements UserDetailsService {

  @Autowired
  private UserService userService;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userService.findByUsername(username);
    List<GrantedAuthority> grantedAuthorities = user == null ? Collections.emptyList() : getUserAuthorities(user);


    return org.springframework.security.core.userdetails.User.builder()
            .username(user.getUsername())
            .password(user.getPassword())
            .authorities(grantedAuthorities)
            .build();
  }

  private List<GrantedAuthority> getUserAuthorities(User user) {
    List<GrantedAuthority> grantedAuthorities= new ArrayList<>();

    List<String> userRoles = userService.getUserRoles(user.getUsername());
    for (String role : userRoles) {
      grantedAuthorities.add(new SimpleGrantedAuthority(role));
    }
    return grantedAuthorities;
  }
}
