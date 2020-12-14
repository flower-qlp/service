package com.oauth.service.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public class BaseUserDetailService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUserAuthentication user = null;
        if ("admin".equals(username)) {
            //这里可以通过auth 获取 user 值
            //然后根据当前登录方式type 然后创建一个sysuserauthentication 重新设置 username 和 password
            //比如使用手机验证码登录的， username就是手机号 password就是6位的验证码{noop}000000
            //所谓的角色，只是增加ROLE_前缀
            List<GrantedAuthority> list = AuthorityUtils.createAuthorityList("admin_role");
            user = new SysUserAuthentication();
            user.setUsername(username);
            user.setPassword("{noop}123456");
            user.setAuthorities(list);
            user.setAccountNonExpired(true);
            user.setAccountNonLocked(true);
            user.setCredentialsNonExpired(true);
            user.setEnabled(true);

        }
        //返回UserDetails的实现user不为空，则验证通过
        return user;
    }
}
