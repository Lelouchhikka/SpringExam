package org.itstep.exam.config;

import org.itstep.exam.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    private UserService service;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/css/**", "/js/**").permitAll()
                        .antMatchers("/", "/home").permitAll() // (3)
                .anyRequest().authenticated(); // (4)

        http.formLogin().loginPage("/login").permitAll()
                .loginProcessingUrl("/auth").permitAll()
                .usernameParameter("email")
                .passwordParameter("password")
                .successForwardUrl("/")
                .defaultSuccessUrl("/")
                .failureUrl("/login?error");

        http.logout().logoutUrl("/logout")
                .logoutSuccessUrl("/login").permitAll();

      http.csrf().disable();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(service);
    }

    @Bean
    public BCryptPasswordEncoder bcryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void setService(UserService service) {
        this.service = service;
    }

    // создаем пользоватлелей, admin и user
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        auth.inMemoryAuthentication()
                .withUser("user").password("password").roles("USER")
                .and()
                .withUser("admin").password("{noop}password").roles("ADMIN");
    }

}
