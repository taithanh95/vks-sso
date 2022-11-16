package com.bitsco.vks.sso.service;

import com.bitsco.vks.common.response.Response;
import com.bitsco.vks.common.response.Token;
import com.bitsco.vks.common.validate.ValidateCommon;
import com.bitsco.vks.sso.cache.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    CacheService cacheService;

    @Override
    public Response check(Token token) throws Exception {
        ValidateCommon.validateNullObject(token.getAccessToken(), "accessToken");
        if (cacheService.getTokenFromCache(token.getAccessToken()) == null)
            return Response.TOKEN_NOT_FOUND;
        else return Response.SUCCESS;
    }

    @Override
    public Token refresh(Token token) throws Exception {
        return null;
    }
}
