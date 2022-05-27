package org.sid.authjwt.security;

import org.sid.authjwt.security.entities.AppUser;
import org.sid.authjwt.security.filters.JwtAuthenticationFilter;
import org.sid.authjwt.security.filters.JwtAuthorizationFilter;
import org.sid.authjwt.security.services.AccountService;
import org.sid.authjwt.security.services.UserDetailsServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.ArrayList;
import java.util.Collection;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private UserDetailsServiceImp userDetailsServiceImp;

    public SecurityConfig(UserDetailsServiceImp userDetailsServiceImp) {
        this.userDetailsServiceImp = userDetailsServiceImp;
    }

    // specifiques qui sont les utilisateurs qui ont le droit d'acceder
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // utiliser c'est methode pour la recherche utilisateur a partir de d'une couche service
        auth.userDetailsService(userDetailsServiceImp);
    }

    // specifier les droits d'acces
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable(); // desactiver le CSRF token
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.headers().frameOptions().disable(); // desactive la protection contre les frames
        // H2 utilise les frames
        // http.formLogin(); // contient un utilisateur qui tentent d'acceder a une ressource dont
        // il n'a pas le droit, affiche le formulaire d'authentication
        http.authorizeRequests().antMatchers("/h2-console/**", "/refreshToken/**", "/login/**").permitAll();
//        http.authorizeRequests().antMatchers("/login").permitAll();
        // premier solution
        http.authorizeRequests().antMatchers(HttpMethod.POST,"/user/**").hasAnyAuthority("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.GET,"/users/**").hasAnyAuthority("USER");
        http.authorizeRequests().anyRequest().authenticated();

        http.addFilter(new JwtAuthenticationFilter(authenticationManagerBean()));
        http.addFilterBefore(new JwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    // override methode pour utilise dans JwtAuthenticationFilter
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
