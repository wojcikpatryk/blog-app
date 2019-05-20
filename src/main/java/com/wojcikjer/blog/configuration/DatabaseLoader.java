package com.wojcikjer.blog.configuration;

import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;
import com.wojcikjer.blog.entities.Post;
import com.wojcikjer.blog.entities.Role;
import com.wojcikjer.blog.entities.User;
import com.wojcikjer.blog.repositories.PostRepository;
import com.wojcikjer.blog.repositories.RoleRepository;
import com.wojcikjer.blog.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
public class DatabaseLoader implements
        ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PostRepository postRepository;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (alreadySetup)
            return;

        createRoleIfNotFound("ROLE_ADMIN");
        createRoleIfNotFound("ROLE_USER");

        createUserIfNotFound("root", "root", "wojcik.patrykk@gmail.com",
                Arrays.asList(
                        roleRepository.findByName("ROLE_USER"),
                        roleRepository.findByName("ROLE_ADMIN")
                ));

        createUserIfNotFound("jer", "jer", "jermarcin15@gmail.com",
                Arrays.asList(
                        roleRepository.findByName("ROLE_USER"),
                        roleRepository.findByName("ROLE_ADMIN")
                ));

        createUserIfNotFound("user", "user", "user@user.com",
                Arrays.asList(
                        roleRepository.findByName("ROLE_USER")
                ));

        createUserIfNotFound("user1", "user1", "user1@user.com",
                Arrays.asList(
                        roleRepository.findByName("ROLE_USER")
                ));

        createUserIfNotFound("user2", "user2", "user2@user.com",
                Arrays.asList(
                        roleRepository.findByName("ROLE_USER")
                ));

//        for (int i = 1; i <= 100; i++) createRandomUser();

        alreadySetup = true;
    }

    @Transactional
    Role createRoleIfNotFound(String name) {

        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role();
            role.setName(name);

            roleRepository.save(role);
        }

        return role;
    }

    @Transactional
    User createUserIfNotFound(String username, String password,
                              String email, List<Role> roles) {

        User user = userService.findByUsername(username);

        if (user == null) {
            user = new User();

            user.setUsername(username);
            user.setPassword(password);
            user.setEmail(email);
            user.setRoles(roles);

            user = userService.save(user);

            Lorem lorem = LoremIpsum.getInstance();
            createPost(user, lorem.getWords(1, 10));
        }

        return user;
    }

    @Transactional
    User createRandomUser() {

        Lorem lorem = LoremIpsum.getInstance();
        Random random = new Random();

        List<Role> roles = null;

        boolean isAdmin = random.nextBoolean();
        int amountOfPosts = random.nextInt(4) + 1;

        if (isAdmin == false)
            roles = Arrays.asList(
                    roleRepository.findByName("ROLE_USER"));
        else
            roles = Arrays.asList(
                    roleRepository.findByName("ROLE_USER"),
                    roleRepository.findByName("ROLE_ADMIN"));

        User user = new User();

        user.setUsername(lorem.getName().toLowerCase().replace(" ", ""));
        user.setPassword(lorem.getWords(8, 18).replace(" ", ""));
        user.setEmail(lorem.getEmail());
        user.setRoles(roles);

        userService.save(user);

        for (int i = 1; i <= amountOfPosts; i++)
            createPost(userService.findByUsername(user.getUsername()), lorem.getWords(1, 10));

        return user;
    }

    @Transactional
    Post createPost(User user, String content) {

        Post post = new Post();
        post.setUser(user);
        post.setContent(content);

        postRepository.save(post);

        return post;
    }
}
