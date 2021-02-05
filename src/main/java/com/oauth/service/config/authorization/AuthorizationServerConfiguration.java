package com.oauth.service.config.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author happy
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration
        extends AuthorizationServerConfigurerAdapter
        implements EnvironmentAware {

    private static final String ENV_OAUTH = "authentication.oauth";

    private static final String PROP_CLIENT_ID = "clientId";

    private static final String PROP_SECRET = "secret";

    private static final String PROP_TOKEN_VALIDITY_SECONDS = "tokenValiditySeconds";

    private Properties properties;

    @Autowired
    @Qualifier("localPassword")
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DataSource dataSource;

    @Autowired
    @Qualifier("authenticationManager")
    private AuthenticationManager authenticationManager;

    @Autowired
    private LocalUserDetailService localUserDetailService;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Bean
    TokenStore tokenStore() {
        return new RedisTokenStore(redisConnectionFactory);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        //指定 token 的存储方式。
        endpoints.tokenStore(tokenStore())
                //设置用户验证服务。
                .userDetailsService(localUserDetailService)
                //调用此方法才能支持 password 模式。
                .authenticationManager(authenticationManager);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()")
                .checkTokenAccess("permitAll()")
                .allowFormAuthenticationForClients();
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient(properties.getProperty(PROP_CLIENT_ID))
                .redirectUris("http://www.baidu.com")
                .scopes("read", "write")
                .authorities("ROLE_CLIENT")
                .authorizedGrantTypes("password", "refresh_token")
                .secret(passwordEncoder.encode(properties.getProperty(PROP_SECRET)))
                .accessTokenValiditySeconds(Integer.valueOf(properties.getProperty(PROP_TOKEN_VALIDITY_SECONDS)));
    }

    @Override
    public void setEnvironment(Environment environment) {
        Iterable<ConfigurationPropertySource> sources =
                ConfigurationPropertySources.get(environment);
        Binder binder = new Binder(sources);
        BindResult<Properties> bindResult = binder.bind(ENV_OAUTH, Properties.class);
        properties = bindResult.get();
    }
}
