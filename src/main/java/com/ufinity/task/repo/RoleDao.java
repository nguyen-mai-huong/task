package com.ufinity.task.repo;

import com.ufinity.task.model.Role;
import com.ufinity.task.utils.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoleDao extends JpaRepository<Role, Long> {
  @Query(value = "SELECT r.roleType " +
          "FROM Role r, UserRole ur " +
          "WHERE ur.userId = :userId AND ur.roleId = r.id ", nativeQuery = true)
  List<Role> findUserRoles(@Param("userId") long userId);

  Role findByRoleType(RoleType roleType);
}
