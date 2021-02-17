package com.ufinity.task.utils;

import com.ufinity.task.model.User;
import com.ufinity.task.model.UserQueryModel;

import java.util.ArrayList;
import java.util.List;

public class UserUtils {
  public static List<UserQueryModel> convertToQueryList(List<User> userList) {
    if (userList != null) {
      List<UserQueryModel> userQueryModelList = new ArrayList<>();
      for (User user: userList) {
        UserQueryModel userQueryModel = new UserQueryModel(user);
        userQueryModelList.add(userQueryModel);
      }
      return userQueryModelList;
    }
    return null;
  }
}
