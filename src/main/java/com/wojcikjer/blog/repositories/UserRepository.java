package com.wojcikjer.blog.repositories;

import com.wojcikjer.blog.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    User findByUsernameOrEmail(String username, String email);

    User findByEmail(String email);

    User findById(long id);

}
