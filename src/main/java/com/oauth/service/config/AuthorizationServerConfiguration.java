package com.oauth.service.config;

import com.oauth.service.config.passwordmode.LocalUserDetailService;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
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

    /**
     * 令牌访问端点
     * 用来配置令牌（token）的访问端点和令牌服务(tokenservices)。
     */
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

    /**
     * 配置客户端详情信息(谁来申请令牌)
     * 用来配置客户端详情服务（ClientDetailsService），客户端详情信息在
     * 这里进行初始化，你能够把客户端详情信息写死在这里或者是通过数据库来存储调取详情信息。
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        /**使用内存的方式*/
        clients.inMemory()
                .withClient(properties.getProperty(PROP_CLIENT_ID))
                //加上验证回调地址
                .redirectUris("http://localhost:8080/login/approve")
                // 允许的授权范围（客户端的权限）
                .scopes("all")
                //资源列表，可以访问的资源列表某一个资源服务的ID
                .resourceIds("oauth2-resource")
                // 该client允许的授权类型
                .authorizedGrantTypes("authorization_code", "password","client_credentials","implicit","refresh_token")
                //false跳转到授权页面
                .autoApprove(false)
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
