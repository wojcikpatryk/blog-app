package com.wojcikjer.blog.controllers;

import com.wojcikjer.blog.entities.User;
import com.wojcikjer.blog.repositories.RoleRepository;
import com.wojcikjer.blog.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Arrays;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/registration")
    public ModelAndView showRegistrationForm() {
        ModelAndView model = new ModelAndView("registration");
        model.addObject("user", new User());

        return model;
    }

    @PostMapping("/registration")
    public ModelAndView register(@Valid @ModelAttribute("user") User user,
                                 BindingResult result) {

        ModelAndView model = new ModelAndView("redirect:/login");
        model.addObject("result", result);

        User userInDatabase =
                userService.findUserByUsernameAndEmail(user.getUsername(), user.getEmail());

        if (userInDatabase != null)
            result.addError(new ObjectError("userExists", "User already exists"));

        if (result.hasErrors()) {
            model.addObject("user", new User());
            model.setViewName("registration");

            return model;
        }

        user.setRoles(Arrays.asList(
                roleRepository.findByName("ROLE_USER")
        ));

        userService.save(user);

        model.addObject("registered", true);

        return model;
    }
}
