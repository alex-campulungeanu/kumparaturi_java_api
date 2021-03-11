package com.alex.kumparaturi.utils;

import com.alex.kumparaturi.exception.ResourceNotFoundException;
import com.alex.kumparaturi.security.CustomUserDetailsService;
import com.alex.kumparaturi.security.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class SecurityUtils {

    public static Optional<UserPrincipal> getCurrentUserLoginOptional() {
        SecurityContext securityContext = SecurityContextHolder.getContext();

        return Optional.ofNullable(securityContext.getAuthentication())
                .map(authentication -> {
                    if (authentication.getPrincipal() instanceof CustomUserDetailsService) {
                        return (UserPrincipal) authentication.getPrincipal();
                    }
                    return null;
                });

    }

    public static UserPrincipal getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
            return (UserPrincipal) authentication.getPrincipal();
        } else {
            throw new IllegalStateException("User not found!");
        }


    }
}
