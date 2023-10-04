package com.cydeo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class SecurityConfig {

    //loads user object to UI. loadUserByUsername(String username) in UserDetailsService.
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder){

        //we are overriding Spring's user with our users
        //create a list of Spring "User"s
        List<UserDetails> userList = new ArrayList<>();

        userList.add(
                //role syntax: ROLE_
                new User("mike", encoder.encode("password"), Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"))));
        userList.add(
                new User("ozzy", encoder.encode("password"), Arrays.asList(new SimpleGrantedAuthority("ROLE_MANAGER"))));

                //save in memory. normally saved in database.
                return new InMemoryUserDetailsManager(userList);

    }
}
