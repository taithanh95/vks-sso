package com.bitsco.vks.sso.api;

import com.bitsco.vks.common.constant.Constant;
import com.bitsco.vks.common.exception.CommonException;
import com.bitsco.vks.common.response.Response;
import com.bitsco.vks.common.response.ResponseBody;
import com.bitsco.vks.common.response.Token;
import com.bitsco.vks.sso.request.LoginRequest;
import com.bitsco.vks.sso.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private static final Logger LOGGER = LogManager.getLogger(Constant.LOG_APPENDER.CONTROLLER);
    @Autowired
    AuthService authService;

    @Operation(description = "Đăng nhập")
    @ApiResponses({
            @ApiResponse(responseCode = "0000", description = "Thành công", content = @Content),
            @ApiResponse(responseCode = "0011", description = "Tài khoản đăng nhập hoặc mật khẩu không chính xác", content = @Content),
            @ApiResponse(responseCode = "9999", description = "Lỗi hệ thống", content = @Content)
    })
    @CrossOrigin(origins = "*")
    @PostMapping("/login/")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SUCCESS, authService.login(loginRequest)), HttpStatus.OK);
        } catch (CommonException e) {
            return new ResponseEntity<>(new ResponseBody(e.getResponse().getResponseCode(), e.getMessage()), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Exception when /auth/login/ ", e);
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SYSTEM_ERROR, e.getMessage()), HttpStatus.OK);
        }
    }

    @Operation(description = "Đăng xuất")
    @ApiResponses({
            @ApiResponse(responseCode = "0000", description = "Thành công", content = @Content),
            @ApiResponse(responseCode = "0006", description = "Thiếu đầu vào bắt buộc", content = @Content),
            @ApiResponse(responseCode = "9999", description = "Lỗi hệ thống", content = @Content)
    })
    @CrossOrigin(origins = "*")
    @PostMapping("/logout/")
    public ResponseEntity<?> logout(@Valid @RequestBody Token token) {
        try {
            return new ResponseEntity<ResponseBody>(new ResponseBody(authService.logout(token)), HttpStatus.OK);
        } catch (CommonException e) {
            return new ResponseEntity<>(new ResponseBody(e.getResponse().getResponseCode(), e.getMessage()), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Exception when /auth/logout/ ", e);
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SYSTEM_ERROR, e.getMessage()), HttpStatus.OK);
        }
    }
}
