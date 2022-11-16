package com.bitsco.vks.sso.api;

import com.bitsco.vks.common.constant.Constant;
import com.bitsco.vks.common.exception.CommonException;
import com.bitsco.vks.common.response.Response;
import com.bitsco.vks.common.response.ResponseBody;
import com.bitsco.vks.sso.dto.UserSaveDTO;
import com.bitsco.vks.sso.entities.User;
import com.bitsco.vks.sso.service.AuthService;
import com.bitsco.vks.sso.service.UserService;
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
import javax.validation.constraints.NotNull;

@CrossOrigin
@RestController
@RequestMapping(value = "user")
public class UserController {
    private static final Logger LOGGER = LogManager.getLogger(Constant.LOG_APPENDER.CONTROLLER);
    @Autowired
    AuthService authService;

    @Autowired
    UserService userService;

    @Operation(description = "Thêm mới tài khoản")
    @ApiResponses({
            @ApiResponse(responseCode = "0000", description = "Thành công", content = @Content),
            @ApiResponse(responseCode = "0002", description = "Đối tượng không tồn tại", content = @Content),
            @ApiResponse(responseCode = "0006", description = "Thiếu dữ liệu đầu vào bắt buộc", content = @Content),
            @ApiResponse(responseCode = "9999", description = "Lỗi hệ thống", content = @Content)
    })
    @CrossOrigin(origins = "*")
    @PostMapping("/create/")
    public ResponseEntity<?> create(@RequestBody UserSaveDTO user) throws Exception {
        try {
//            user.setAction("I");
//            authService.handleInsOrUpdUserOld(user);
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SUCCESS, authService.create(user.getUser())), HttpStatus.OK);
        } catch (CommonException e) {
            return new ResponseEntity<ResponseBody>(new ResponseBody(e.getResponse().getResponseCode(), e.getMessage()), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Exception when /user/create/ ", e);
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SYSTEM_ERROR, e.getMessage()), HttpStatus.OK);
        }
    }

    @Operation(description = "Cập nhật thông tin tài khoản")
    @ApiResponses({
            @ApiResponse(responseCode = "0000", description = "Thành công", content = @Content),
            @ApiResponse(responseCode = "0002", description = "Đối tượng không tồn tại", content = @Content),
            @ApiResponse(responseCode = "0006", description = "Thiếu dữ liệu đầu vào bắt buộc", content = @Content),
            @ApiResponse(responseCode = "9999", description = "Lỗi hệ thống", content = @Content)
    })
    @CrossOrigin(origins = "*")
    @PostMapping("/update/")
    public ResponseEntity<?> update(@RequestBody UserSaveDTO user) throws Exception {
        try {
//            user.setAction("U");
//            authService.handleInsOrUpdUserOld(user);
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SUCCESS, authService.update(user.getUser())), HttpStatus.OK);
        } catch (CommonException e) {
            return new ResponseEntity<ResponseBody>(new ResponseBody(e.getResponse().getResponseCode(), e.getMessage()), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Exception when /user/update/ ", e);
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SYSTEM_ERROR, e.getMessage()), HttpStatus.OK);
        }
    }

    @Operation(description = "Xóa thông tin tài khoản")
    @ApiResponses({
            @ApiResponse(responseCode = "0000", description = "Thành công", content = @Content),
            @ApiResponse(responseCode = "0002", description = "Đối tượng không tồn tại", content = @Content),
            @ApiResponse(responseCode = "0006", description = "Thiếu dữ liệu đầu vào bắt buộc", content = @Content),
            @ApiResponse(responseCode = "9999", description = "Lỗi hệ thống", content = @Content)
    })
    @CrossOrigin(origins = "*")
    @PostMapping("/delete/")
    public ResponseEntity<?> delete(@RequestBody UserSaveDTO user) throws Exception {
        try {
//            authService.handleDeleteUserOld(user);
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SUCCESS, authService.delete(user.getUser())), HttpStatus.OK);
        } catch (CommonException e) {
            return new ResponseEntity<ResponseBody>(new ResponseBody(e.getResponse().getResponseCode(), e.getMessage()), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Exception when /user/update/ ", e);
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SYSTEM_ERROR, e.getMessage()), HttpStatus.OK);
        }
    }

    @Operation(description = "Tra cứu danh sách tài khoản")
    @ApiResponses({
            @ApiResponse(responseCode = "0000", description = "Thành công", content = @Content),
            @ApiResponse(responseCode = "0006", description = "Thiếu dữ liệu đầu vào bắt buộc", content = @Content),
            @ApiResponse(responseCode = "0007", description = "Không tìm thấy dữ liệu cần tra cứu", content = @Content),
            @ApiResponse(responseCode = "9999", description = "Lỗi hệ thống", content = @Content)
    })
    @PostMapping("/getList/")
    public ResponseEntity<?> getList(@Valid @NotNull @RequestBody User user) throws Exception {
        try {
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SUCCESS, userService.getList(user)), HttpStatus.OK);
        } catch (CommonException e) {
            return new ResponseEntity<ResponseBody>(new ResponseBody(e.getResponse().getResponseCode(), e.getMessage()), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Exception when /user/getList/ ", e);
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SYSTEM_ERROR, e.getMessage()), HttpStatus.OK);
        }
    }
}
