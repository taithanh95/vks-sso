package com.bitsco.vks.sso.service;

import com.bitsco.vks.common.constant.Constant;
import com.bitsco.vks.common.constant.MessageContent;
import com.bitsco.vks.common.crypt.Base64;
import com.bitsco.vks.common.exception.CommonException;
import com.bitsco.vks.common.response.Response;
import com.bitsco.vks.common.response.ResponseBody;
import com.bitsco.vks.common.response.Token;
import com.bitsco.vks.common.util.MessageCommon;
import com.bitsco.vks.common.util.StringCommon;
import com.bitsco.vks.common.validate.ValidateCommon;
import com.bitsco.vks.sso.cache.CacheService;
import com.bitsco.vks.sso.dto.UserSaveDTO;
import com.bitsco.vks.sso.entities.User;
import com.bitsco.vks.sso.feign.ManageServiceFeignAPI;
import com.bitsco.vks.sso.model.Spp;
import com.bitsco.vks.sso.repository.jdbc.AdmUserRepository;
import com.bitsco.vks.sso.request.ChangePasswordRequest;
import com.bitsco.vks.sso.request.LoginRequest;
import com.bitsco.vks.sso.security.TokenProvider;
import com.bitsco.vks.sso.thread.GrantRoleThread;
import com.bitsco.vks.sso.thread.SendEmailThread;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {
    @Autowired
    SendEmailThread sendEmailThread;
    @Autowired
    GrantRoleThread grantRoleThread;
    @Autowired
    OtpService otpService;
    @Autowired
    CacheService cacheService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    ManageServiceFeignAPI manageServiceFeignAPI;
    @Autowired
    GroupUserService groupUserService;

    @Autowired
    AdmUserRepository admUserRepository;

    @Override
    public Token login(LoginRequest loginRequest) throws Exception {
        Authentication authentication = null;
        Token tokenResponse = null;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            Base64.decodeBase64(loginRequest.getPassword())
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            tokenResponse = tokenProvider.createToken(authentication);
            if (tokenResponse == null || StringCommon.isNullOrBlank(tokenResponse.getAccessToken()))
                throw new CommonException(Response.SYSTEM_ERROR, "Kh??ng th??? kh???i t???o token cho t??i kho???n");
            User user = userService.findFirstByUsername(tokenResponse.getUsername());
            if (user == null)
                throw new CommonException(Response.OBJECT_NOT_FOUND, "Kh??ng t??m th???y th??ng tin t??i kho???n");
            else if (user.getStatus() == null || user.getStatus() == Constant.USER.STATUS.INACTIVE)
                throw new CommonException(Response.USER_INACTIVE);
            //L???y th??ng tin Spp c???a user
            ResponseBody responseBody = manageServiceFeignAPI.findFirstByUsername(user);
            if (responseBody == null || StringCommon.isNullOrBlank(responseBody.getResponseCode()) || responseBody.getResponseData() == null)
                throw new CommonException(Response.SYSTEM_ERROR, "L???i khi th???c hi???n truy v???n th??ng tin danh m???c vi???n ki???m s??t c???a t??i kho???n");
            Spp spp = (new ObjectMapper()).convertValue(responseBody.getResponseData(), Spp.class);
            user.setSpp(spp);
            tokenResponse.setUser(user);
            cacheService.addToken2RedisCache(tokenResponse);
        } catch (Exception e) {
            throw new CommonException(Response.LOGIN_FAIL);
        }
        return tokenResponse;
    }

    @Override
    public Response logout(Token token) throws Exception {
        ValidateCommon.validateNullObject(token.getAccessToken(), "accessToken");
        cacheService.removeToken2RedisCache(token);
        return Response.SUCCESS;
    }

    @Override
    public User create(User user) throws Exception {
        ValidateCommon.validateNullObject(user.getUsername(), "username");
        ValidateCommon.validateNullObject(user.getPassword(), "password");
        ValidateCommon.validateNullObject(user.getName(), "name");
        user.setEmailVerified(Constant.STATUS_OBJECT.ACTIVE);
        user.setStatus(Constant.USER.STATUS.ACTIVE);
        user.setCreatedBy(cacheService.getUsernameFromHeader());

        if (userService.findFirstByUsername(user.getUsername()) != null)
            throw new CommonException(Response.OBJECT_IS_EXISTS, MessageCommon.getMessage(MessageContent.OBJECT_IS_EXISTS, "username"));
        try {
            user.setPasswordDecode(Base64.decodeBase64(user.getPassword()));
        } catch (Exception e) {
            throw new CommonException(Response.DATA_INVALID, "Kh??ng th??? gi???i m?? m???t kh???u");
        }
        user.setPassword(passwordEncoder.encode(user.getPasswordDecode()));
        user = userService.save(user);
        //Add user create/update to redis cache
        cacheService.addUser2RedisCache(user);
        //Lay token
        Authentication authentication = null;
        Token tokenResponse = null;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUsername(),
                            user.getPasswordDecode()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            tokenResponse = tokenProvider.createToken(authentication);
        } catch (Exception e) {
            throw new CommonException(Response.INVALID_JWT_TOKEN);
        }
        if (tokenResponse == null || StringCommon.isNullOrBlank(tokenResponse.getAccessToken()))
            throw new CommonException(Response.SYSTEM_ERROR, "Kh??ng t???o ???????c token khi ????ng k?? m???i t??i kho???n");

        //Gui email xac thuc tai khoan
//        sendEmailThread.sendSignupSuccess(user, tokenResponse.getAccessToken());
        //Grant quyen cho user
        groupUserService.setUserRoleByGroupUserId(user);
        return user;
    }

    @Override
    public User update(User user) throws Exception {
        if(!StringCommon.isNullOrBlank(user.getPassword())){
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
//        //Update l???i quy???n cho user
//        groupUserService.setUserRoleByGroupUserId(user);
        return userService.update(user);
    }

    @Override
    public Response delete(User user) throws Exception {
        return userService.delete(user);
    }

    @Override
    public Response verifyEmail(String token) throws Exception {
        ValidateCommon.validateNullObject(token, "token");
        Response response = tokenProvider.checkToken(token);
        if (!response.getResponseCode().equals(Response.SUCCESS.getResponseCode())) throw new CommonException(response);
        String username = tokenProvider.getUsernameFromToken(token);
        if (username == null)
            throw new CommonException(Response.SYSTEM_ERROR, "Kh??ng t??m th???y ng?????i d??ng t????ng ???ng v???i token");
        User user = userService.findFirstByUsername(username);
        if (user == null)
            throw new CommonException(Response.OBJECT_NOT_FOUND, MessageCommon.getMessage(MessageContent.OBJECT_NOT_FOUND_BY_FIELD_VALUE, new String[]{"user", "id", username + ""}));
        if (user.getStatus() == null || user.getStatus() != Constant.USER.STATUS.WAIT_FOR_CONFIRM_EMAIL)
            throw new CommonException(Response.DATA_INVALID, "Ng?????i d??ng kh??ng ??? tr???ng th??i ch??? x??c nh???n");
        user.setEmailVerified(Constant.STATUS_OBJECT.ACTIVE);
        user.setStatus(Constant.STATUS_OBJECT.ACTIVE);
        user.setUpdatedBy(username);
        userService.save(user);
        //send email
//        sendEmailThread.sendVerifyEmailSuccess(user);
        return response;
    }

    @Override
    public Response resetPasswordByAdmin(User user) throws Exception {
        ValidateCommon.validateNullObject(user.getId(), "id");
        User userOld = userService.findById(user.getId());
        if (userOld == null)
            throw new CommonException(Response.OBJECT_NOT_FOUND, "T??i kho???n kh??ng t???n t???i");
//        String passwordNew = RandomStringUtils.randomAlphanumeric(6).toLowerCase();
        String passwordNew = "123456";
        userOld.setPassword(passwordEncoder.encode(passwordNew));
        userOld.setUpdatedBy(cacheService.getUsernameFromHeader());
        userService.save(userOld);
        //Send email
//        sendEmailThread.sendEmailResetPasswordByAdmin(userOld.getUsername(), passwordNew, userOld.getEmail());
        return Response.SUCCESS;
    }

    @Override
    public Response resetPasswordByUser(User user) throws Exception {
        ValidateCommon.validateNullObject(user.getEmail(), "email");
        ValidateCommon.validateNullObject(user.getUsername(), "username");
        User userOld = userService.findFirstByUsernameAndEmail(user.getUsername().trim().toLowerCase(), user.getEmail().trim().toLowerCase());
        if (userOld == null)
            throw new CommonException(Response.OBJECT_NOT_FOUND, "T??i kho???n kh??ng t???n t???i");
//        String passwordNew = RandomStringUtils.randomAlphanumeric(6).toLowerCase();
        String passwordNew = "123456";
        userOld.setPassword(passwordEncoder.encode(passwordNew));
        userService.save(userOld);
        //Send email
//        sendEmailThread.sendEmailResetPasswordByUserSuccess(userOld.getUsername(), passwordNew, userOld.getEmail());
        return Response.SUCCESS;
    }

    @Override
    public Response changePasswordByUser(ChangePasswordRequest changePasswordRequest) throws Exception {
        String username = cacheService.getUsernameFromHeader();
        if (username == null)
            throw new CommonException(Response.LOGIN_FAIL);
        User user = userService.findFirstByUsername(username);
        if (user == null || user.getStatus() == Constant.STATUS_OBJECT.INACTIVE)
            throw new CommonException(Response.LOGIN_FAIL);
        Authentication authentication = null;
        Token tokenResponse = null;
        try {
            changePasswordRequest.setPasswordOldDecode(Base64.decodeBase64(changePasswordRequest.getPasswordOld()));
            changePasswordRequest.setPasswordNewDecode(Base64.decodeBase64(changePasswordRequest.getPasswordNew()));
        } catch (Exception e) {
            throw new CommonException(Response.DATA_INVALID, "Kh??ng th??? gi???i m?? m???t kh???u");
        }
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUsername(),
                            changePasswordRequest.getPasswordOldDecode()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            tokenResponse = tokenProvider.createToken(authentication);
        } catch (Exception e) {
            throw new CommonException(Response.DATA_INVALID, "M???t kh???u c?? kh??ng ????ng");
        }
        if (tokenResponse == null) throw new CommonException(Response.DATA_INVALID, "Kh??ng th??? kh???i t???o token");
        user.setPassword(passwordEncoder.encode(changePasswordRequest.getPasswordNewDecode()));
        userService.save(user);
        //Send email
//        sendEmailThread.sendEmailChangePasswordByUserSuccess(user.getUsername(), changePasswordRequest.getPasswordOld(), changePasswordRequest.getPasswordNew(), user.getEmail());
        return Response.SUCCESS;
    }

    @Override
    public void handleInsOrUpdUserOld(UserSaveDTO user) {
        String _res = admUserRepository.insOrUpd(user, cacheService.getUsernameFromHeader());
        if (StringCommon.isNullOrBlank(_res)) return;

        if ("adm_Users.messages.accountExist".equalsIgnoreCase(_res))
            throw new CommonException(Response.OBJECT_IS_EXISTS, "T??i kho???n ???? t???n t???i trong CSDL");
        if ("adm_Users.messages.canNotEdit".equalsIgnoreCase(_res))
            throw new CommonException(Response.OBJECT_INACTIVE, "B???n kh??ng ???????c ph??p c???p nh???t ng??y h???t h???n, t??nh tr???ng kh??a ho???c thay ?????i VKS c???a t??i kho???n n??y.");
        if ("adm_Users.messages.valueTooLarge".equalsIgnoreCase(_res))
            throw new CommonException(Response.REQUIRED_MAX_LENGTH, "Gi?? tr??? nh???p v??o c?? ????? d??i qu?? l???n. B???n h??y ki???m tra l???i.");

        throw new CommonException(Response.SYSTEM_ERROR, _res);
    }

    @Override
    public void handleDeleteUserOld(UserSaveDTO user) {
        String _res = admUserRepository.delete(user, cacheService.getUsernameFromHeader());
        if (!StringCommon.isNullOrBlank(_res)) return;

        if ("adm_Users.messages.canNotDelete".equalsIgnoreCase(_res))
            throw new CommonException(Response.SUPPLIER_ERROR, "Kh??ng th??? x??a (nh???ng) t??i kho???n n??y (admin, danhmuc, fpt).");
        throw new CommonException(Response.SYSTEM_ERROR, _res);
    }
}
