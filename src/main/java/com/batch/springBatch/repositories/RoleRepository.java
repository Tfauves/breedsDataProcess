package com.batch.springBatch.repositories;

import com.batch.springBatch.auth.ERole;
import com.batch.springBatch.auth.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}