package com.oauth.service.controller;

import com.oauth.service.bean.User;
import com.oauth.service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/findUserByName")
    public User findUser(@RequestParam(value = "userName",required = false)String userName){
        return userService.findByUsername(userName);
    }



}
