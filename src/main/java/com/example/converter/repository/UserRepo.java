package com.example.converter.repository;

import com.example.converter.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User,Integer> {
    User findByUsername(String username);
    boolean existsUserByUsername(String username);
}
