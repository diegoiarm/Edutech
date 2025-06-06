package com.edutech.identity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.edutech.identity.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

}
