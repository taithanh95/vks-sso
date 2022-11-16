package com.bitsco.vks.sso.api;

import com.bitsco.vks.common.constant.Constant;
import com.bitsco.vks.common.exception.CommonException;
import com.bitsco.vks.common.response.Response;
import com.bitsco.vks.common.response.ResponseBody;
import com.bitsco.vks.sso.entities.User;
import com.bitsco.vks.sso.request.ChangePasswordRequest;
import com.bitsco.vks.sso.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/password")
public class PasswordController {
    private static final Logger LOGGER = LogManager.getLogger(Constant.LOG_APPENDER.CONTROLLER);
    @Autowired
    AuthService authService;

    @Operation(description = "Đặt lại mật khẩu bởi admin")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description =
                    "0000 - Thành công" + "\n" +
                            "9999 - Lỗi hệ thống"
            )})
    @CrossOrigin(origins = "*")
    @PostMapping("/resetPasswordByAdmin")
    public ResponseEntity<?> resetPasswordByAdmin(@Valid @RequestBody User user) {
        try {
            return new ResponseEntity<ResponseBody>(new ResponseBody(authService.resetPasswordByAdmin(user)), HttpStatus.OK);
        } catch (CommonException e) {
            return new ResponseEntity<>(new ResponseBody(e.getResponse().getResponseCode(), e.getMessage()), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Exception when /password/resetPasswordByAdmin/ ", e);
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SYSTEM_ERROR, e.getMessage()), HttpStatus.OK);
        }
    }

    @Operation(description = "Người dùng quên mật khẩu")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description =
                    "0000 - Thành công" + "\n" +
                            "9999 - Lỗi hệ thống"
            )})
    @CrossOrigin(origins = "*")
    @PostMapping("/resetPasswordByUser")
    public ResponseEntity<?> resetPasswordByUser(@Valid @RequestBody User user) {
        try {
            return new ResponseEntity<ResponseBody>(new ResponseBody(authService.resetPasswordByUser(user)), HttpStatus.OK);
        } catch (CommonException e) {
            return new ResponseEntity<>(new ResponseBody(e.getResponse().getResponseCode(), e.getMessage()), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Exception when /password/resetPasswordByUser/ ", e);
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SYSTEM_ERROR, e.getMessage()), HttpStatus.OK);
        }
    }

    @Operation(description = "Người dùng thực hiện đổi mật khẩu sau khi đăng nhập")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description =
                    "0000 - Thành công" + "\n" +
                            "9999 - Lỗi hệ thống"
            )})
    @CrossOrigin(origins = "*")
    @PostMapping("/changePasswordByUser")
    public ResponseEntity<?> changePasswordByUser(@Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        try {
            return new ResponseEntity<ResponseBody>(new ResponseBody(authService.changePasswordByUser(changePasswordRequest)), HttpStatus.OK);
        } catch (CommonException e) {
            return new ResponseEntity<>(new ResponseBody(e.getResponse().getResponseCode(), e.getMessage()), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Exception when /password/changePasswordByUser/ ", e);
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SYSTEM_ERROR, e.getMessage()), HttpStatus.OK);
        }
    }

}
