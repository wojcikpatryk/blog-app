package com.wojcikjer.blog.configuration.securityUser;

import org.springframework.security.core.context.SecurityContextHolder;

public class UserContext {

    public static String getCurrentUserUsername() {

        String username = null;

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof MyUserPrincipal)
            username = ((MyUserPrincipal) principal).getUsername();
        else
            username = principal.toString();

        return username;
    }
}
