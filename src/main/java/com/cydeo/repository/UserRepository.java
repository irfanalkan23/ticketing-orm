package com.cydeo.repository;

import com.cydeo.entity.Role;
import com.cydeo.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByUserName(String username);   //derived query

    @Transactional
    void deleteByUserName(String username);     //derived query

    List<User> findAllByRoleDescriptionIgnoreCase(String description);  //derived query
}
