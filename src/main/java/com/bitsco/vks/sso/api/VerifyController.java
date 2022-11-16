package com.bitsco.vks.sso.api;

import com.bitsco.vks.common.constant.Constant;
import com.bitsco.vks.common.exception.CommonException;
import com.bitsco.vks.common.response.Response;
import com.bitsco.vks.common.response.ResponseBody;
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
@RequestMapping("/verify")
public class VerifyController {
    private static final Logger LOGGER = LogManager.getLogger(Constant.LOG_APPENDER.CONTROLLER);
    @Autowired
    AuthService authService;

    @Operation(description = "Xác nhận email")
    @ApiResponses({
            @ApiResponse(responseCode = "0000", description = "Thành công", content = @Content),
            @ApiResponse(responseCode = "0002", description = "Không tìm thấy user tương ứng token", content = @Content),
            @ApiResponse(responseCode = "0006", description = "Thiếu dữ liệu đầu vào bắt buộc", content = @Content),
            @ApiResponse(responseCode = "0012", description = "Token có chữ ký không hợp lệ", content = @Content),
            @ApiResponse(responseCode = "0013", description = "Token không hợp lệ", content = @Content),
            @ApiResponse(responseCode = "0014", description = "Token hết hiệu lực", content = @Content),
            @ApiResponse(responseCode = "0015", description = "Token có định dạng không được hỗ trợ", content = @Content),
            @ApiResponse(responseCode = "9999", description = "Lỗi hệ thống", content = @Content)
    })
    @CrossOrigin(origins = "*")
    @GetMapping("/email")
    public ResponseEntity<?> verifyEmail(@Valid @RequestParam String token) {
        try {
            return new ResponseEntity<ResponseBody>(new ResponseBody(authService.verifyEmail(token)), HttpStatus.OK);
        } catch (CommonException e) {
            return new ResponseEntity<>(new ResponseBody(e.getResponse().getResponseCode(), e.getMessage()), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Exception when /verify/email/ ", e);
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SYSTEM_ERROR, e.getMessage()), HttpStatus.OK);
        }
    }
}
