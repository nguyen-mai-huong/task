package com.ufinity.task.model;

import com.ufinity.task.utils.RoleType;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "role", uniqueConstraints = { @UniqueConstraint(columnNames = {"roleType"})})
@Data
public class Role {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "roleType", length = 50, nullable = false, unique = true)
  @Enumerated(EnumType.STRING)
  private RoleType roleType;

  public Role(RoleType roleType) {
    this.roleType = roleType;
  }
}
