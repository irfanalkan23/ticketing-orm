package com.cydeo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class SecurityConfig {

    //loads user object to UI. loadUserByUsername(String username) in UserDetailsService.
    //this one is manual way, we will do user bean creation from database
//    @Bean
//    public UserDetailsService userDetailsService(PasswordEncoder encoder){
//
//        //we are overriding Spring's user with our users
//        //create a list of Spring "User"s
//        List<UserDetails> userList = new ArrayList<>();
//
//        userList.add(
//                //role syntax: ROLE_
//                new User("mike", encoder.encode("password"), Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"))));
//        userList.add(
//                new User("ozzy", encoder.encode("password"), Arrays.asList(new SimpleGrantedAuthority("ROLE_MANAGER"))));
//
//                //save in memory. normally saved in database.
//                return new InMemoryUserDetailsManager(userList);
//    }

    //setup who will be authorized to access which pages
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

//        return http
//                .authorizeRequests()    //all the pages will be required to authorized,
//                //but I don't want the login page and some other pages to require authorization
//                //can be based on the directory or controller end point
//                .requestMatchers(
//                        "/",
//                        "/login",
//                        "/fragments/**",    //everything inside fragments directory
//                        "/assets/**",
//                        "images/**"
//                ).permitAll()   //permit access to everybody
//                .anyRequest().authenticated()
//                .and()
//                .httpBasic()
//                .and().build();

        http
                .authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers("/user/**").hasRole("Admin")   //only ADMIN has access to all the UserController methods
                        .requestMatchers("/user/**").hasAuthority("Admin")   //only ADMIN has access to all the UserController methods
//                        .requestMatchers("/project/**").hasRole("Manager")  //hasRole() includes "ROLE_" prefix
                        .requestMatchers("/project/**").hasAuthority("Manager")  //hasRole() includes "ROLE_" prefix
//                        .requestMatchers("/task/employee/**").hasRole("Employee")
                        .requestMatchers("/task/employee/**").hasAuthority("Employee")
//                        .requestMatchers("/task/**").hasRole("Manager")
                        .requestMatchers("/task/**").hasAuthority("Manager")
//                        .requestMatchers("/task/**").hasAnyRole("EMPLOYEE", "ADMIN")
//                        .requestMatchers("/task/**").hasAuthority("ROLE_EMPLOYEE")    //hasAuthority() is NOT putting prefix

                        .requestMatchers(
                                "/",
                                "/login",
                                "/fragments/**",
                                "/assets/**",
                                "images/**")
                        .permitAll() // Allow access to these endpoints and directories without authentication
                        .anyRequest().authenticated()
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .defaultSuccessUrl("/welcome")
//                .successHandler(authSuccessHandler)
                        .failureUrl("/login?error=true")
                        .permitAll());
//                .formLogin(Customizer.withDefaults());

        return http.build();



        /* Ozzy's code:
                return http
                .authorizeRequests()
//                .antMatchers("/user/**").hasRole("Admin")
                .antMatchers("/user/**").hasAuthority("Admin")
                .antMatchers("/project/**").hasAuthority("Manager")
                .antMatchers("/task/employee/**").hasAuthority("Employee")
                .antMatchers("/task/**").hasAuthority("Manager")
//                .antMatchers("/task/**").hasAnyRole("EMPLOYEE","ADMIN")
//                .antMatchers("task/**").hasAuthority("ROLE_EMPLOYEE")

                .antMatchers(
                       "/",
                       "/login",
                       "fragments/**",
                       "/assets/**",
                       "/images/**"
                ).permitAll()
                .anyRequest().authenticated()
                .and()
//                .httpBasic()
                .formLogin()
                    .loginPage("/login")
//                    .defaultSuccessUrl("/welcome")
                    .successHandler(authSuccessHandler)
                    .failureUrl("/login?error=true")
                    .permitAll()
                .and()
                .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/login")
                .and()
                .rememberMe()
                    .tokenValiditySeconds(120)
                    .key("cydeo")
                    .userDetailsService(securityService)
                .and().build();
         */
    }


}









