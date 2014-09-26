package com.magnoliales.handlebars.security;

import info.magnolia.context.MgnlContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class MagnoliaAuthenticationManager implements AuthenticationManager {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return new MagnoliaAuthentication(MgnlContext.getUser());
    }
}
