package com.wojcikjer.blog.services;

import com.wojcikjer.blog.entities.User;
import com.wojcikjer.blog.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User findByUsername(String username) {
        User user = userRepository.findByUsername(username);
//        user.getRoles().stream().forEach(role -> role.setUsers(null));
        return user;
    }

    public User findUserByUsernameAndEmail(String username, String email) {
        return userRepository.findByUsernameOrEmail(username, email);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findUserById(long id) {
        return userRepository.findById(id);
    }

    public User save(User user) {

        user = trimPassword(user);

        String oldPassword = user.getPassword();
        String encodedPassword = encoder.encode(oldPassword);

        if (!oldPassword.equals(encodedPassword))
            user.setPassword(encodedPassword);

        return userRepository.save(user);
    }

    public User save(User user, boolean doHash) {
        user = trimPassword(user);

        if (!doHash)
            return userRepository.save(user);

        return this.save(user);
    }

    public void follow(User follower, User following) {
        follower.getFollowing().add(following);
        this.save(follower, false);
    }

    private User trimPassword(User user) {
        user.setPassword(user.getPassword().trim());
        return user;
    }

    public void deleteUserById(long id) {
        userRepository.deleteById(id);
    }
}
