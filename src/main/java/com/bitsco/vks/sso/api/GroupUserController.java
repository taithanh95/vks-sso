package com.bitsco.vks.sso.api;

import com.bitsco.vks.common.constant.Constant;
import com.bitsco.vks.common.exception.CommonException;
import com.bitsco.vks.common.response.Response;
import com.bitsco.vks.common.response.ResponseBody;
import com.bitsco.vks.sso.entities.GroupUser;
import com.bitsco.vks.sso.entities.GroupUserGroupRole;
import com.bitsco.vks.sso.entities.GroupUserRole;
import com.bitsco.vks.sso.request.GroupUserRequest;
import com.bitsco.vks.sso.service.GroupUserService;
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

@CrossOrigin("*")
@RestController
@RequestMapping("/groupUser")
public class GroupUserController {
    private static final Logger LOGGER = LogManager.getLogger(Constant.LOG_APPENDER.CONTROLLER);
    @Autowired
    GroupUserService groupUserService;

    @Operation(description = "Thêm mới nhóm người dùng GroupUser")
    @ApiResponses({
            @ApiResponse(responseCode = "0000", description = "Thành công", content = @Content),
            @ApiResponse(responseCode = "0001", description = "Đối tượng đã tồn tại", content = @Content),
            @ApiResponse(responseCode = "0006", description = "Thiếu dữ liệu đầu vào bắt buộc", content = @Content),
            @ApiResponse(responseCode = "9999", description = "Lỗi hệ thống", content = @Content)
    })
    @PostMapping("/createGroupUser/")
    public ResponseEntity<?> createGroupUser(@Valid @NotNull @RequestBody GroupUser groupUser) throws Exception {
        try {
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SUCCESS, groupUserService.createGroupUser(groupUser)), HttpStatus.OK);
        } catch (CommonException e) {
            return new ResponseEntity<ResponseBody>(new ResponseBody(e.getResponse().getResponseCode(), e.getMessage()), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Exception when /groupUser/createGroupUser/ ", e);
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SYSTEM_ERROR, e.getMessage()), HttpStatus.OK);
        }
    }

    @Operation(description = "Tìm kiếm chi tiết nhóm người dùng theo id")
    @ApiResponses({
            @ApiResponse(responseCode = "0000", description = "Thành công", content = @Content),
            @ApiResponse(responseCode = "0006", description = "Thiếu dữ liệu đầu vào bắt buộc", content = @Content),
            @ApiResponse(responseCode = "9999", description = "Lỗi hệ thống", content = @Content)
    })
    @PostMapping("/findById/")
    public ResponseEntity<?> findById(@Valid @NotNull @RequestBody GroupUser groupUser) throws Exception {
        try {
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SUCCESS, groupUserService.findById(groupUser.getId())), HttpStatus.OK);
        } catch (CommonException e) {
            return new ResponseEntity<ResponseBody>(new ResponseBody(e.getResponse().getResponseCode(), e.getMessage()), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Exception when /groupUser/findById/ ", e);
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SYSTEM_ERROR, e.getMessage()), HttpStatus.OK);
        }
    }

    @Operation(description = "Cập nhật nhóm người dùng GroupUser")
    @ApiResponses({
            @ApiResponse(responseCode = "0000", description = "Thành công", content = @Content),
            @ApiResponse(responseCode = "0001", description = "Đối tượng đã tồn tại", content = @Content),
            @ApiResponse(responseCode = "0002", description = "Đối tượng không tồn tại", content = @Content),
            @ApiResponse(responseCode = "9999", description = "Lỗi hệ thống", content = @Content)
    })
    @PostMapping("/updateGroupUser/")
    public ResponseEntity<?> updateGroupUser(@Valid @NotNull @RequestBody GroupUser groupUser) throws Exception {
        try {
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SUCCESS, groupUserService.updateGroupUser(groupUser)), HttpStatus.OK);
        } catch (CommonException e) {
            return new ResponseEntity<ResponseBody>(new ResponseBody(e.getResponse().getResponseCode(), e.getMessage()), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Exception when /groupUser/updateGroupUser/ ", e);
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SYSTEM_ERROR, e.getMessage()), HttpStatus.OK);
        }
    }

    @Operation(description = "Truy vấn danh sách nhóm người dùng GroupUser")
    @ApiResponses({
            @ApiResponse(responseCode = "0000", description = "Thành công", content = @Content),
            @ApiResponse(responseCode = "0001", description = "Đối tượng đã tồn tại", content = @Content),
            @ApiResponse(responseCode = "0002", description = "Đối tượng không tồn tại", content = @Content),
            @ApiResponse(responseCode = "9999", description = "Lỗi hệ thống", content = @Content)
    })
    @PostMapping("/getListGroupUser/")
    public ResponseEntity<?> getListGroupUser(@Valid @NotNull @RequestBody GroupUser groupUser) throws Exception {
        try {
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SUCCESS, groupUserService.getListGroupUser(groupUser)), HttpStatus.OK);
        } catch (CommonException e) {
            return new ResponseEntity<ResponseBody>(new ResponseBody(e.getResponse().getResponseCode(), e.getMessage()), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Exception when /groupUser/getListGroupUser/ ", e);
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SYSTEM_ERROR, e.getMessage()), HttpStatus.OK);
        }
    }

    @Operation(description = "Thêm mới hoặc cập nhật GroupUserGroupRole")
    @ApiResponses({
            @ApiResponse(responseCode = "0000", description = "Thành công", content = @Content),
            @ApiResponse(responseCode = "0001", description = "Đối tượng đã tồn tại", content = @Content),
            @ApiResponse(responseCode = "0002", description = "Đối tượng không tồn tại", content = @Content),
            @ApiResponse(responseCode = "9999", description = "Lỗi hệ thống", content = @Content)
    })
    @PostMapping("/mergeGroupUserGroupRole/")
    public ResponseEntity<?> mergeGroupUserGroupRole(@Valid @NotNull @RequestBody GroupUserGroupRole groupUserGroupRole) throws Exception {
        try {
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SUCCESS, groupUserService.mergeGroupUserGroupRole(groupUserGroupRole)), HttpStatus.OK);
        } catch (CommonException e) {
            return new ResponseEntity<ResponseBody>(new ResponseBody(e.getResponse().getResponseCode(), e.getMessage()), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Exception when /groupUser/mergeGroupUserGroupRole/ ", e);
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SYSTEM_ERROR, e.getMessage()), HttpStatus.OK);
        }
    }

    @Operation(description = "Thêm mới hoặc cập nhật GroupUserRole")
    @ApiResponses({
            @ApiResponse(responseCode = "0000", description = "Thành công", content = @Content),
            @ApiResponse(responseCode = "0001", description = "Đối tượng đã tồn tại", content = @Content),
            @ApiResponse(responseCode = "0002", description = "Đối tượng không tồn tại", content = @Content),
            @ApiResponse(responseCode = "9999", description = "Lỗi hệ thống", content = @Content)
    })
    @PostMapping("/mergeGroupUserRole/")
    public ResponseEntity<?> mergeGroupUserRole(@Valid @NotNull @RequestBody GroupUserRole groupUserRole) throws Exception {
        try {
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SUCCESS, groupUserService.mergeGroupUserRole(groupUserRole)), HttpStatus.OK);
        } catch (CommonException e) {
            return new ResponseEntity<ResponseBody>(new ResponseBody(e.getResponse().getResponseCode(), e.getMessage()), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Exception when /groupUser/mergeGroupUserRole/ ", e);
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SYSTEM_ERROR, e.getMessage()), HttpStatus.OK);
        }
    }

    @Operation(description = "Truy vấn chi tiết GroupUserRole theo groupUserId và roleId ")
    @ApiResponses({
            @ApiResponse(responseCode = "0000", description = "Thành công", content = @Content),
            @ApiResponse(responseCode = "0001", description = "Đối tượng đã tồn tại", content = @Content),
            @ApiResponse(responseCode = "0002", description = "Đối tượng không tồn tại", content = @Content),
            @ApiResponse(responseCode = "9999", description = "Lỗi hệ thống", content = @Content)
    })
    @PostMapping("/findGroupUserRoleById/")
    public ResponseEntity<?> findGroupUserRoleById(@Valid @NotNull @RequestBody GroupUserRole groupUserRole) throws Exception {
        try {
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SUCCESS, groupUserService.findGroupUserRoleById(groupUserRole.getGroupUserId(), groupUserRole.getRoleId())), HttpStatus.OK);
        } catch (CommonException e) {
            return new ResponseEntity<ResponseBody>(new ResponseBody(e.getResponse().getResponseCode(), e.getMessage()), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Exception when /groupUser/findGroupUserRoleById/ ", e);
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SYSTEM_ERROR, e.getMessage()), HttpStatus.OK);
        }
    }

    @Operation(description = "Truy vấn chi tiết GroupUserGroupRole theo groupUserId và groupRoleId ")
    @ApiResponses({
            @ApiResponse(responseCode = "0000", description = "Thành công", content = @Content),
            @ApiResponse(responseCode = "0001", description = "Đối tượng đã tồn tại", content = @Content),
            @ApiResponse(responseCode = "0002", description = "Đối tượng không tồn tại", content = @Content),
            @ApiResponse(responseCode = "9999", description = "Lỗi hệ thống", content = @Content)
    })
    @PostMapping("/findGroupUserGroupRoleById/")
    public ResponseEntity<?> findGroupUserGroupRoleById(@Valid @NotNull @RequestBody GroupUserGroupRole groupUserGroupRole) throws Exception {
        try {
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SUCCESS, groupUserService.findGroupUserGroupRoleById(groupUserGroupRole.getGroupUserId(), groupUserGroupRole.getGroupRoleId())), HttpStatus.OK);
        } catch (CommonException e) {
            return new ResponseEntity<ResponseBody>(new ResponseBody(e.getResponse().getResponseCode(), e.getMessage()), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Exception when /groupUser/findGroupUserGroupRoleById/ ", e);
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SYSTEM_ERROR, e.getMessage()), HttpStatus.OK);
        }
    }

    @Operation(description = "Thực hiện phân quyền cho nhóm người dùng")
    @ApiResponses({
            @ApiResponse(responseCode = "0000", description = "Thành công", content = @Content),
            @ApiResponse(responseCode = "0007", description = "Không tìm thấy dữ liệu cần tra cứu", content = @Content),
            @ApiResponse(responseCode = "9999", description = "Lỗi hệ thống", content = @Content)
    })
    @PostMapping("/setGroupRoleAndRoleByGroupUserId/")
    public ResponseEntity<?> setGroupRoleAndRoleByGroupUserId(@Valid @NotNull @RequestBody GroupUserRequest groupUserRequest) throws Exception {
        try {
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SUCCESS, groupUserService.setGroupRoleAndRoleByGroupUserId(groupUserRequest)), HttpStatus.OK);
        } catch (CommonException e) {
            return new ResponseEntity<ResponseBody>(new ResponseBody(e.getResponse().getResponseCode(), e.getMessage()), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Exception when /groupUser/setGroupRoleAndRoleByGroupUserId/ ", e);
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SYSTEM_ERROR, e.getMessage()), HttpStatus.OK);
        }
    }

    @Operation(description = "Truy vấn danh sách những nhóm quyền và quyền đã phân quyền cho nhóm người dùng để load lên màn hình chuẩn bị phân quyền cho nhóm người dùng")
    @ApiResponses({
            @ApiResponse(responseCode = "0000", description = "Thành công", content = @Content),
            @ApiResponse(responseCode = "0007", description = "Không tìm thấy dữ liệu cần tra cứu", content = @Content),
            @ApiResponse(responseCode = "9999", description = "Lỗi hệ thống", content = @Content)
    })
    @PostMapping("/getGroupRoleByGroupUserId/")
    public ResponseEntity<?> getGroupRoleByGroupUserId(@Valid @NotNull @RequestBody GroupUserRequest groupUserRequest) throws Exception {
        try {
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SUCCESS, groupUserService.getGroupRoleByGroupUserId(groupUserRequest)), HttpStatus.OK);
        } catch (CommonException e) {
            return new ResponseEntity<ResponseBody>(new ResponseBody(e.getResponse().getResponseCode(), e.getMessage()), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Exception when /groupUser/getGroupRoleByGroupUserId/ ", e);
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SYSTEM_ERROR, e.getMessage()), HttpStatus.OK);
        }
    }
    @Operation(description = "Xóa nhóm người dùng báo cáo")
    @CrossOrigin(origins = "*")
    @PostMapping("/deleteGroupUser/")
    public ResponseEntity<?> deleteGroupUser (@Valid @NotNull @RequestBody GroupUser groupUser) throws Exception {
        try{
            return new ResponseEntity<ResponseBody>(new ResponseBody(groupUserService.deleteGroupUser(groupUser)), HttpStatus.OK);
        } catch (CommonException e) {
            return new ResponseEntity<ResponseBody>(new ResponseBody(e.getResponse().getResponseCode(), e.getMessage()), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Exception when /groupUser/deleteGroupUser/ ", e);
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SYSTEM_ERROR, e.getMessage()), HttpStatus.OK);
        }
    }

}
