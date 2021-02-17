package com.ufinity.task.service;

import com.ufinity.task.model.User;
import com.ufinity.task.model.UserQueryModel;
import com.ufinity.task.repo.UserQueryDao;
import com.ufinity.task.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserQueryService implements IUserQueryService {
  private UserQueryDao userQueryDao;

  @Autowired
  public UserQueryService(UserQueryDao userQueryDao) {
    this.userQueryDao = userQueryDao;
  }

  @Override
  public List<UserQueryModel> getUsers(int recordsPerPage){
    List<User> userListFromDatabase =  userQueryDao.getUsers(recordsPerPage);
    return UserUtils.convertToQueryList(userListFromDatabase);
  }

  @Override
  public List<UserQueryModel> getNextUsers(int recordsPerPage, long cursor) {
    List<User> userListFromDatabase = userQueryDao.getNextUsers(cursor, recordsPerPage);
    return  UserUtils.convertToQueryList(userListFromDatabase);
  }
}
