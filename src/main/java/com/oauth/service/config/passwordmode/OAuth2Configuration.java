package com.oauth.service.config.passwordmode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * @author happy
 * 配置安全资源服务器
 */
@Configuration
public class OAuth2Configuration {

    @Configuration
    @EnableResourceServer
    protected static class ResourceServerConfiguration
            extends ResourceServerConfigurerAdapter {

        @Autowired
        private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

        @Autowired
        private CustomLogoutSuccessHandler logoutSuccessHandler;

        /**
         * 资源服务器的权限设置
         **/
        @Override
        public void configure(HttpSecurity http) throws Exception {
            http.exceptionHandling()
                    .authenticationEntryPoint(customAuthenticationEntryPoint)
                    .and()
                    .logout()
                    .logoutSuccessHandler(logoutSuccessHandler)
                    .and()
                    .authorizeRequests()
                    .antMatchers("/api/**", "/oauth/**", "/login/**", "/static/**")
                    .permitAll()
                    .anyRequest().authenticated();

        }


    }

}
