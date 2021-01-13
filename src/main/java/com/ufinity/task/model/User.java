package com.ufinity.task.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.time.LocalDateTime;

@Entity
@Table(name = "user", uniqueConstraints = {@UniqueConstraint(columnNames = {"username"})})
@Data
@NoArgsConstructor
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "username", length = 100, nullable = false, unique = true)
  private String username;

  @Column(name = "password", length = 60, nullable = false)
  private String password;

  @Column(name = "email", length = 50)
  private String email;

  @Column(name = "createdBy", nullable = false)
  private Long createdBy;

  @Column(name = "createdDate", nullable = false)
  private LocalDateTime createdDate;

  @Column(name = "updatedBy", nullable = false)
  private Long updatedBy;

  @Column(name = "updatedDate", nullable = false)
  private LocalDateTime updatedDate;

  public User(String username, String email) {
    this.username = username;
    this.email = email;
  }

  @Override
  public String toString() {
    return String.format("User[id=%d, username=%s, password=%s, email=%s]", id, username, password, email);
  }


}
