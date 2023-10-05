package com.cydeo.config;

import com.cydeo.service.SecurityService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {

    private final SecurityService securityService;
    private final AuthSuccessHandler authSuccessHandler;

    public SecurityConfig(SecurityService securityService, AuthSuccessHandler authSuccessHandler) {
        this.securityService = securityService;
        this.authSuccessHandler = authSuccessHandler;
    }


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

        http
                .authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers("/user/**").hasRole("Admin")   //only ADMIN has access to all the UserController methods
                        .requestMatchers("/user/**").hasAuthority("Admin")   //only ADMIN has access to all the UserController methods
                        .requestMatchers("/project/**").hasAuthority("Manager")  //hasRole() includes "ROLE_" prefix
                        .requestMatchers("/task/employee/**").hasAuthority("Employee")
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
//                .formLogin(Customizer.withDefaults());
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
//                        .defaultSuccessUrl("/welcome")
                        .successHandler(authSuccessHandler)
                        .failureUrl("/login?error=true")
                        .permitAll())
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/login"))
                .rememberMe(rememberMe -> rememberMe
                        .tokenValiditySeconds(120)
                        .key("cydeo")
                        .userDetailsService(securityService));

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









