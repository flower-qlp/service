package com.oauth.service.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 权限管理的核心配置
 *
 * @Author happy
 * @EnableGlobalMethodSecurity(prePostEnabled = true) 在controller启用注解机制安全确认
 * 基于方法的认证机制
 **/
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * UserDetailsService 接口的实现
     **/
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService simpleUserDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(simpleUserDetailsService())
                .passwordEncoder(passwordEncoder());
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 配置放行资源
     **/
    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }

    /**
     * 配置过滤限制
     **/
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.userDetailsService(userDetailsService());
        http.formLogin()
                //自定义登陆页,默认/login
                .loginPage("/login/page")
                //form表单提交的url
                .loginProcessingUrl("/login/form/account")
                .passwordParameter("passWord")
                .usernameParameter("userName")
                //默认登陆成功返回的url
                .defaultSuccessUrl("/oauth/my_approval_page")
                .and()
                .logout()
                .logoutUrl("/login/out")
                .logoutSuccessUrl("/oauth/my_approval_page")
                .and()
                .authorizeRequests()//以下是定义哪些接口需要保护 哪些不用
                .antMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                .antMatchers("/login/**", "/static/**", "/oauth/**").permitAll();
                //.anyRequest()//任何接口需要登录后才可以访问
                //.authenticated();

    }

}
