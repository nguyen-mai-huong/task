package com.ufinity.task.service;

import com.ufinity.task.model.User;
import com.ufinity.task.repo.UserDao;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.ufinity.task.utils.Constants.OK;
import static com.ufinity.task.utils.Constants.USER_CREDENTIALS_NOT_MATCH;
import static com.ufinity.task.utils.Constants.USER_NOT_EXIST;

@Service
public class UserService implements IUserService{

  private UserDao userDao;

  public UserService(UserDao userDao) {
    this.userDao = userDao;
  }

  @Override
  public List<User> listUsers() {
    return userDao.findAll();
  }

  @Override
  public String login(String username, String password) {
    User user = userDao.findByUsername(username);

    if (user == null) {
      return USER_NOT_EXIST;
    }

    // TODO: Using encryption function e.g. bcrypt during user signup
    // Need to encrypt password before comparison
    if (user != null && !user.getPassword().equals(password)) {
      return USER_CREDENTIALS_NOT_MATCH;
    }

    return OK;
  }
}
