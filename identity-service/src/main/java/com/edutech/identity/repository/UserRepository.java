package com.edutech.identity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.edutech.identity.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

}
