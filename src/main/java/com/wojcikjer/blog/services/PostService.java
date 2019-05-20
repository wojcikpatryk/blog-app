package com.wojcikjer.blog.services;

import com.wojcikjer.blog.entities.Post;
import com.wojcikjer.blog.entities.User;
import com.wojcikjer.blog.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public List<Post> findAllPosts() {
        return postRepository.findAll();
    }

    public Post findPostById(long id) {
        return postRepository.findById(id);
    }

    public List<Post> findAllPostsByUser(User user) {
        return postRepository.findByUser(user);
    }

    public List<Post> findAllPostsByFollowingUsers(List<User> followingUsers) {
        List<Post> posts = new ArrayList<>();

        for (User user : followingUsers)
            posts.addAll(user.getPosts());

        return posts;
    }

    public Post save(Post post) {
        return postRepository.save(post);
    }

    public void remove(Post post) {
        postRepository.delete(post);
    }

    public void removePostById(long id) {
        postRepository.deleteById(id);
    }
}
