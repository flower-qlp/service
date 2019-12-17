package com.oauth.service.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {

    @RequestMapping(value = "/login/page")
    public String login() {
        return "login";
    }

    @RequestMapping(value = "/oauth/my_approval_page")
    public String approval() {
        return "my_approval_page";
    }

    @RequestMapping(value = "/oauth/my_error_page")
    public String error() {
        return "my_error_page";
    }
}
