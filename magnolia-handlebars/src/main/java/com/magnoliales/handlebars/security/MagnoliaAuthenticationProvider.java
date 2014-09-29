package com.magnoliales.handlebars.security;

import info.magnolia.cms.security.User;
import info.magnolia.context.MgnlContext;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class MagnoliaAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userName = authentication.getName();
        User user = MgnlContext.getUser();
        if (user.getName().equals(userName)) {
            return new MagnoliaAuthentication(user);
        } else {
            throw new BadCredentialsException("User not found.");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
