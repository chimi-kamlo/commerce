package com.ecommerce.repositories;

import com.ecommerce.entities.Role;
import com.ecommerce.entities.TypeRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRpository extends JpaRepository<Role , Integer> {


    Optional<Role> findRoleByLibele(TypeRole role);
}
