package com.oauth.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService simpleUserDetailsService(){
        return new UserDetailsServiceImpl();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(simpleUserDetailsService());
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.userDetailsService(userDetailsService());
        http.csrf().disable();
        http.formLogin()
                //自定义登陆页,默认/login
                .loginPage("/login/page")
                //form表单提交的url
                .loginProcessingUrl("/login/form/account")
                //默认登陆成功返回的url
                .defaultSuccessUrl("/index")
                .and()
                .logout()
                .logoutUrl("/login/out")
                .logoutSuccessUrl("/login/page")
                .and()
                .authorizeRequests()//以下是定义哪些接口需要保护 哪些不用
                .antMatchers("/login/**","/static/**","/oauth/**").permitAll()
                .antMatchers("/user/**").hasAnyRole("USER","ADMIN")
                .anyRequest().authenticated(); //任何接口需要登录后才可以访问

    }

}
