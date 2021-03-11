package com.alex.kumparaturi.repository;

import com.alex.kumparaturi.model.Role;
import com.alex.kumparaturi.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName roleName);
}
