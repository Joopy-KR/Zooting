package com.zooting.api.global.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaAuditing
//@EnableJpaRepositories(basePackages = {"com.zooting.api.domain"},
//excludeFilters = @ComponentScan.Filter(type = FilterType.ASPECTJ,
//        pattern = "com.zooting.api.domain.member.redis.dao.**")
//)
public class JpaConfig {
}
