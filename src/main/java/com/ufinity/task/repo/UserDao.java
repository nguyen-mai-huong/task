package com.ufinity.task.repo;

import com.ufinity.task.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserDao extends JpaRepository<User, Long> {
  @Override
  List<User> findAll();

  User findByUsername(String username);

}
