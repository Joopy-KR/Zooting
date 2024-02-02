package com.zooting.api.util;

import com.zooting.api.domain.member.entity.Privilege;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)     // 런타임까지 어노테이션 유지
@WithSecurityContext(factory = WithCustomMockUserSecurityContextFactory.class)
public @interface WithCustomMockUser {
    String email();
    Privilege role();
}
