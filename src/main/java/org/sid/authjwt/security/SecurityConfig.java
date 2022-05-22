package org.sid.authjwt.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    // specifiques qui sont les utilisateurs qui ont le droit d'acceder
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

    }

    // specifier les droits d'acces
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable(); // desactiver le CSRF token
        http.headers().frameOptions().disable(); // desactive la protection contre les frames
        // H2 utilise les frames
        http.authorizeRequests().anyRequest().permitAll(); // necessite pas une authentification
    }
}
