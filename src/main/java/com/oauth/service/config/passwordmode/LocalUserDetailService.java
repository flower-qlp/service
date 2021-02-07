package com.oauth.service.config.passwordmode;

import com.oauth.service.bean.Role;
import com.oauth.service.bean.User;
import com.oauth.service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class LocalUserDetailService implements UserDetailsService {

    @Autowired
    private UserService userService;


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        System.out.println("s-->" + s);
        User user = userService.findByUsername(s);
        if (null == user) {
            throw new RuntimeException("user" + s + "not found in database!");
        }
        List<Role> roles = new ArrayList<>();
        return new User(user.getUsername(), user.getPassword(), roles);
    }
}
