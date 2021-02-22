package com.oauth.service.controller;

import com.oauth.service.bean.auth.AuthAuthorizationReq;
import com.oauth.service.bean.auth.AuthPasswordReq;
import com.oauth.service.bean.auth.AuthTokenResp;
import com.oauth.service.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author happy
 */
@RestController
@RequestMapping(value = "/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @RequestMapping(value = "/type-password",
            method = RequestMethod.POST,
            name = "密码模式登陆")
    public AuthTokenResp loginByPassword(
            @RequestBody AuthPasswordReq req) {
        return loginService.loginByPassword(req);
    }

    @RequestMapping(value = "/type-authorization",
            method = RequestMethod.POST,
            name = "授权转换token")
    public AuthTokenResp loginByAuthorization(
            @RequestBody AuthAuthorizationReq req) {
        return loginService.loginByAuthAuthorization(req);
    }
}
