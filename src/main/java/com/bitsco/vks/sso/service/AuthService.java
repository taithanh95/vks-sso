package com.bitsco.vks.sso.service;

import com.bitsco.vks.common.response.Response;
import com.bitsco.vks.common.response.Token;
import com.bitsco.vks.sso.dto.UserSaveDTO;
import com.bitsco.vks.sso.entities.User;
import com.bitsco.vks.sso.request.ChangePasswordRequest;
import com.bitsco.vks.sso.request.LoginRequest;

public interface AuthService {

    Token login(LoginRequest loginRequest) throws Exception;

    Response logout(Token token) throws Exception;

    User create(User user) throws Exception;

    User update(User user) throws Exception;

    Response delete(User user) throws Exception;

    Response verifyEmail(String token) throws Exception;

    Response resetPasswordByAdmin(User user) throws Exception;

    Response resetPasswordByUser(User user) throws Exception;

    Response changePasswordByUser(ChangePasswordRequest changePasswordRequest) throws Exception;

    void handleInsOrUpdUserOld(UserSaveDTO user);

    void handleDeleteUserOld(UserSaveDTO user);
}
