package com.wojcikjer.blog.repositories;

import com.wojcikjer.blog.entities.Post;
import com.wojcikjer.blog.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Post findById(long id);

    List<Post> findByUser(User user);

}
