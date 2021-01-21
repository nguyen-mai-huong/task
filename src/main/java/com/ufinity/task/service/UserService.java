package com.ufinity.task.service;

import com.ufinity.task.model.Role;
import com.ufinity.task.model.User;
import com.ufinity.task.model.UserRole;
import com.ufinity.task.repo.RoleDao;
import com.ufinity.task.repo.UserDao;
import com.ufinity.task.repo.UserRoleDao;
import com.ufinity.task.utils.RoleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.ufinity.task.utils.Constants.ERROR;
import static com.ufinity.task.utils.Constants.OK;

@Service
public class UserService implements IUserService{

  private UserDao userDao;
  private RoleDao roleDao;
  private UserRoleDao userRoleDao;
  private BCryptPasswordEncoder passwordEncoder;

  @Autowired
  public UserService(UserDao userDao, RoleDao roleDao, UserRoleDao userRoleDao, BCryptPasswordEncoder passwordEncoder) {
    this.userDao = userDao;
    this.roleDao = roleDao;
    this.userRoleDao = userRoleDao;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public List<User> listUsers() {
    return userDao.findAll();
  }

  @Override
  public User findByUsername(String username) {
    return userDao.findByUsername(username);
  }

  @Override
  @Transactional
  public String signup(String username, String password, String email) {
    //TODO: validate username and password

    Long createdBy = 1L;
    LocalDateTime now = LocalDateTime.now();

    User user = new User(username, passwordEncoder.encode(password));
    user.setEmail(email);
    user.setCreatedBy(createdBy);
    user.setCreatedDate(now);
    user.setUpdatedBy(createdBy);
    user.setUpdatedDate(now);

    try {
      User savedUser = userDao.save(user);

      UserRole userRole = new UserRole(savedUser.getId(), roleDao.findByRoleType(RoleType.USER).getId());
      userRoleDao.save(userRole);

    } catch (Exception e) {
      // Failed to create user
      e.printStackTrace();
      return ERROR;
    }

    return OK;
  }

  @Override
  public List<String> getUserRoles(String username) {
    List<String> roles = new ArrayList<>();
    User user = userDao.findByUsername(username);

    if (user != null) {
      roles = roleDao.findUserRoles(user.getId());
    }
    return roles;
  }
}
