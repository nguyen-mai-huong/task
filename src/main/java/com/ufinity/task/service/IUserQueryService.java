package com.ufinity.task.service;

import com.ufinity.task.model.UserQueryModel;

import java.util.List;

public interface IUserQueryService {

  public List<UserQueryModel> getUsers(int recordsPerPage);

  public List<UserQueryModel> getNextUsers(int recordsPerPage, long cursor);

  public List<UserQueryModel> getPreviousUsers(int recordsPerPage, long cursor);

  public Long countUsers();
}
