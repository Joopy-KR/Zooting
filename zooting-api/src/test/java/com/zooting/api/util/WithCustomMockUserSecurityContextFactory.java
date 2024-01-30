package com.zooting.api.util;

import com.zooting.api.domain.member.entity.Privilege;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithCustomMockUserSecurityContextFactory implements WithSecurityContextFactory<WithCustomMockUser> {
    @Override
    public SecurityContext createSecurityContext(WithCustomMockUser annotation) {
        String email = annotation.email();
        Privilege role = annotation.role();

        Authentication auth = new UsernamePasswordAuthenticationToken(email, "", List)
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication();

        return null;
    }
}
