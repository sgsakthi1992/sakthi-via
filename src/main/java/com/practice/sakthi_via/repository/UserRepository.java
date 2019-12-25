package com.practice.sakthi_via.repository;

import com.practice.sakthi_via.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {

   //@Query("SELECT u FROM USERS u WHERE u.email = :email")
   Optional<List> findByEmail(String email);
}
