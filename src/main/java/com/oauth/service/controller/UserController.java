package com.oauth.service.controller;

import com.oauth.service.bean.User;
import com.oauth.service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author itoutsource.cz10
 */
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/api/findUserByName")
    public User findUserApi(@RequestParam(value = "userName", required = false) String userName) {
        return userService.findByUsername(userName);
    }

    @PostMapping(value = "/api/findUserByName")
    public User findUserApiPost(@RequestParam(value = "userName", required = false) String userName) {
        return userService.findByUsername(userName);
    }


    @PostMapping(value = "/api/saveUser")
    public void saveUser(@RequestParam(value = "userName", required = true) String userName,
                         @RequestParam(value = "password", required = true) String password) {
        userService.saveUser(userName,password);
    }

    @GetMapping(value = "/test/findUserByName")
    public User findUserTest(@RequestParam(value = "userName", required = false) String userName) {
        return userService.findByUsername(userName);
    }

}
