package com.oauth.service.service;

import com.oauth.service.bean.auth.AuthAuthorizationReq;
import com.oauth.service.bean.auth.AuthPasswordReq;
import com.oauth.service.bean.auth.AuthTokenResp;

public interface LoginService {

    AuthTokenResp loginByPassword(AuthPasswordReq req);

    AuthTokenResp loginByAuthAuthorization(AuthAuthorizationReq req);

}
