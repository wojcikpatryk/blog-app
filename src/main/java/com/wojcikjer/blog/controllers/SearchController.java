package com.wojcikjer.blog.controllers;

import com.wojcikjer.blog.configuration.securityUser.UserContext;
import com.wojcikjer.blog.entities.User;
import com.wojcikjer.blog.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class SearchController {

    @Autowired
    private UserService userService;

    @GetMapping("/search")
    public ModelAndView searchForUsername(@RequestParam(required = false, defaultValue = "") String username) {

        ModelAndView model = new ModelAndView("search");
        model.addObject("currentUsername", UserContext.getCurrentUserUsername());

        List<User> users = new ArrayList<>();

        if (username.trim().replace(" ", "").equals("")) {
            model.addObject("users", users);
            model.addObject("username", "empty username");

            return model;
        }

        users = userService.findAllUsers().stream()
                .filter(userSearch -> userSearch.getUsername().startsWith(username.trim()))
                .collect(Collectors.toList());

        model.addObject("users", users);
        model.addObject("username", username);

        return model;
    }
}
