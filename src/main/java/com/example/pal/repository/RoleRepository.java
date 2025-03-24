package com.example.pal.repository;

import com.example.pal.model.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(String name);

  @Query("SELECT r FROM Role r JOIN r.users u WHERE u.id = :userId")
  Optional<Role[]> findByUserId(Long userId);
}
