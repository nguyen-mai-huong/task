package com.ufinity.task.service;

import com.ufinity.task.model.User;
import com.ufinity.task.repo.UserDao;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
