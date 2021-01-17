package com.ufinity.task.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserModel {

  private String username;
  private String password;
  private String email;

  public UserModel(String username, String password, String email) {
    this.username = username;
    this.password = password;
    this.email = email;
  }

}
