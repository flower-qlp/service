package com.oauth.service.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.oauth.service.bean.auth.AuthAuthorizationReq;
import com.oauth.service.bean.auth.AuthPasswordReq;
import com.oauth.service.bean.auth.AuthTokenResp;
import com.oauth.service.service.LoginService;
import com.oauth.service.utils.PropertiesUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static com.oauth.service.utils.RestTemplateUtils.postForAuth;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private PropertiesUtils propertiesUtils;

    @Override
    public AuthTokenResp loginByPassword(AuthPasswordReq req) {
        //不能使用hashMap
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("client_id", propertiesUtils.getClientId());
        map.add("client_secret", propertiesUtils.getSecret());
        map.add("grant_type", "password");
        map.add("username", req.getUserName());
        map.add("password", req.getPassWord());
        String resp = postForAuth(propertiesUtils.getAuthToken(), map);
        System.out.println(JSON.toJSONString(resp));
        return JSONObject.parseObject(resp, AuthTokenResp.class);
    }

    /**
     * 授权码只能使用一次
     **/
    @Override
    public AuthTokenResp loginByAuthAuthorization(AuthAuthorizationReq req) {
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("client_id", propertiesUtils.getClientId());
        map.add("client_secret", propertiesUtils.getSecret());
        map.add("grant_type", "authorization_code");
        map.add("state", req.getState());
        map.add("redirect_uri", "http://localhost:8080/login/approve");
        map.add("code", req.getCode());
        String resp = postForAuth(propertiesUtils.getAuthToken(), map);
        System.out.println(JSON.toJSONString(resp));
        return JSONObject.parseObject(resp, AuthTokenResp.class);
    }
}
