package com.bitsco.vks.sso.service;

import com.bitsco.vks.common.response.Response;
import com.bitsco.vks.common.response.Token;

public interface TokenService {
    Response check(Token token) throws Exception;

    Token refresh(Token token) throws Exception;
}
