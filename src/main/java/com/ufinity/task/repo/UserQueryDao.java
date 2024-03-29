package com.ufinity.task.repo;

import com.ufinity.task.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * UserQueryDao is used to retrieve user info
 */
public interface UserQueryDao extends JpaRepository<User, Long> {

  @Query(value = "SELECT * FROM user u LIMIT :numOfRecords", nativeQuery = true)
  List<User> getUsers(@Param("numOfRecords") int numOfRecords);

  @Query(value = "SELECT * FROM user u LIMIT :numOfRecords OFFSET :offset", nativeQuery = true)
  List<User> getPaginatedUsers(@Param("numOfRecords") int numOfRecords, @Param("offset") long offset);

  @Query(value = "SELECT * FROM user u WHERE id > :cursor LIMIT :numOfRecords", nativeQuery = true)
  List<User> getNextUsers(@Param("cursor") long cursor, @Param("numOfRecords") int numOfRecords);

  @Query(value = "SELECT * FROM user u WHERE id < :cursor ORDER BY u.id DESC LIMIT :numOfRecords", nativeQuery = true)
  List<User> getPreviousUsers(@Param("cursor") long cursor, @Param("numOfRecords") int numOfRecords);

  @Query(value = "SELECT COUNT(*) FROM user u", nativeQuery = true)
  Long countUsers();
}
