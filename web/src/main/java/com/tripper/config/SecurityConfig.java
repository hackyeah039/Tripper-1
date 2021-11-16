package com.tripper.config;

import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity

public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override //무시 사이트 목록
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers(
                        "/console/**"
                        ,"/favicon.ico"
                        ,"/error"
                        ,"/swagger-ui/**"
                );
    }
}
