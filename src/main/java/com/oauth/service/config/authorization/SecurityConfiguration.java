package com.oauth.service.config.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.annotation.Id;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.expression.OAuth2MethodSecurityExpressionHandler;

import static org.springframework.security.crypto.factory.PasswordEncoderFactories.createDelegatingPasswordEncoder;

/**
 * @author happy
 * 开启验证
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private LocalUserDetailService localUserDetailService;

    /**
     * 匹配用户时密码规则
     **/
    @Bean(value = "localPassword")
    public PasswordEncoder passwordEncoder() {
        //不能用factory 会报Id 为 null
        return new BCryptPasswordEncoder();
    }

    /**
     * 允许匿名访问所有接口 主要是 oauth 接口
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/**").permitAll();
    }

    /**
     * 实现 OAuth2 的 password 模式必须要指定的授权管理 Bean。
     **/
    @Override
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }


    /**
     * 全局配置
     **/
    @Autowired
    public void configGlobal(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(localUserDetailService)
                .passwordEncoder(passwordEncoder());
    }

    /**
     * 开启全局方法拦截
     **/
    @EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true)
    public static class GlobalSecurityConfiguration extends GlobalMethodSecurityConfiguration {
        @Override
        protected MethodSecurityExpressionHandler createExpressionHandler() {
            return new OAuth2MethodSecurityExpressionHandler();
        }

    }
}