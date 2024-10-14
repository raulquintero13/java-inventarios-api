package com.inventarios.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inventarios.entity.Role;


@Repository
public interface IRoleRepository extends JpaRepository<Role, Long>{

    Optional<Role> findByName(String name);

}
