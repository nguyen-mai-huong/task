package com.ufinity.task.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserQueryModel{

  private Long id;
  private String username;
  private String email;

  public UserQueryModel(Long id, String username, String email) {
    this.id = id;
    this.username = username;
    this.email = email;
  }

  public UserQueryModel(User user) {
    this.id = user.getId();
    this.username = user.getUsername();
    this.email = user.getEmail();
  }

}
