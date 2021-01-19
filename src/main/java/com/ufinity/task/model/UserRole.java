package com.ufinity.task.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "userrole", uniqueConstraints = {@UniqueConstraint(columnNames = {"userId", "roleId"})})
@Data
public class UserRole {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "userId", nullable = false)
  private Long userId;

  @Column(name = "roleId", nullable = false)
  private Long roleId;

  public UserRole(Long userId, Long roleId) {
    this.userId = userId;
    this.roleId =roleId;
  }
}
