package com.wojcikjer.blog.controllers;

import com.wojcikjer.blog.configuration.securityUser.UserContext;
import com.wojcikjer.blog.entities.User;
import com.wojcikjer.blog.services.PostService;
import com.wojcikjer.blog.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @GetMapping("/user")
    public ModelAndView getUserPage(@RequestParam String username) {

        boolean isFollowing = false;

        User follower = userService.findByUsername(UserContext.getCurrentUserUsername());
        User following = userService.findByUsername(username);

        if (follower.isFollowing(following))
            isFollowing = true;

        Collections.sort(following.getPosts(), Collections.reverseOrder());

        ModelAndView model = new ModelAndView("user");
        model.addObject("currentUsername", UserContext.getCurrentUserUsername());
        model.addObject("user", following);
        model.addObject("isFollowing", isFollowing);
        model.addObject("posts", following.getPosts());

        return model;
    }

    @PostMapping("/follow")
    public String followUser(@ModelAttribute("user") User user) {

        User follower = userService.findByUsername(UserContext.getCurrentUserUsername());
        User following = userService.findByUsername(user.getUsername());

        if (!follower.isFollowing(following)) {

//            follower.getFollowing().add(following);
//            userService.save(follower, false);

            userService.follow(follower, following);

        } else {

            follower.getFollowing().remove(following);
            userService.save(follower, false);

        }

        return "redirect:/user?username=" + following.getUsername();
    }

    @GetMapping("/removePostFromUserPage")
    public String removePost(@RequestParam long id) {

        postService.removePostById(id);

        return "redirect:/user?username=" + UserContext.getCurrentUserUsername();
    }

}
