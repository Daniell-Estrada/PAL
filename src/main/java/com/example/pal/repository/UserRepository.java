package com.example.pal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.pal.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	User findByUsername(String username);

	User findByEmail(String email);

	boolean existsByEmail(String email);

	@Query("SELECT u FROM User u JOIN u.roles r WHERE LOWER(r.name) = LOWER(:roleName)")
	List<User> findUsersByRole(@Param("roleName") String roleName);
}
