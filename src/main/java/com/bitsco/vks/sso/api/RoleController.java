package com.bitsco.vks.sso.api;

import com.bitsco.vks.common.constant.Constant;
import com.bitsco.vks.common.exception.CommonException;
import com.bitsco.vks.common.request.PageRequest;
import com.bitsco.vks.common.response.Response;
import com.bitsco.vks.common.response.ResponseBody;
import com.bitsco.vks.sso.entities.*;
import com.bitsco.vks.sso.request.GroupRoleIdAndStatus;
import com.bitsco.vks.sso.request.UserRoleRequest;
import com.bitsco.vks.sso.service.RoleService;
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
@RequestMapping("/role")
public class RoleController {
    private static final Logger LOGGER = LogManager.getLogger(Constant.LOG_APPENDER.CONTROLLER);
    @Autowired
    RoleService roleService;

    @Operation(description = "Thêm mới quyền Role")
    @ApiResponses({
            @ApiResponse(responseCode = "0000", description = "Thành công", content = @Content),
            @ApiResponse(responseCode = "0001", description = "Đối tượng đã tồn tại", content = @Content),
            @ApiResponse(responseCode = "0006", description = "Thiếu dữ liệu đầu vào bắt buộc", content = @Content),
            @ApiResponse(responseCode = "9999", description = "Lỗi hệ thống", content = @Content)
    })
    @PostMapping("/createRole/")
    public ResponseEntity<?> createRole(@Valid @NotNull @RequestBody Role role) throws Exception {
        try {
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SUCCESS, roleService.createRole(role)), HttpStatus.OK);
        } catch (CommonException e) {
            return new ResponseEntity<ResponseBody>(new ResponseBody(e.getResponse().getResponseCode(), e.getMessage()), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Exception when /role/createRole/ ", e);
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SYSTEM_ERROR, e.getMessage()), HttpStatus.OK);
        }
    }

    @Operation(description = "Tìm kiếm chi tiết quyền theo id")
    @ApiResponses({
            @ApiResponse(responseCode = "0000", description = "Thành công", content = @Content),
            @ApiResponse(responseCode = "0006", description = "Thiếu dữ liệu đầu vào bắt buộc", content = @Content),
            @ApiResponse(responseCode = "9999", description = "Lỗi hệ thống", content = @Content)
    })
    @PostMapping("/findRoleById/")
    public ResponseEntity<?> findRoleById(@Valid @NotNull @RequestBody Role role) throws Exception {
        try {
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SUCCESS, roleService.findRoleById(role.getId())), HttpStatus.OK);
        } catch (CommonException e) {
            return new ResponseEntity<ResponseBody>(new ResponseBody(e.getResponse().getResponseCode(), e.getMessage()), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Exception when /role/findRoleById/ ", e);
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SYSTEM_ERROR, e.getMessage()), HttpStatus.OK);
        }
    }

    @Operation(description = "Cập nhật Role")
    @ApiResponses({
            @ApiResponse(responseCode = "0000", description = "Thành công", content = @Content),
            @ApiResponse(responseCode = "0001", description = "Đối tượng đã tồn tại", content = @Content),
            @ApiResponse(responseCode = "0002", description = "Đối tượng không tồn tại", content = @Content),
            @ApiResponse(responseCode = "9999", description = "Lỗi hệ thống", content = @Content)
    })
    @PostMapping("/updateRole/")
    public ResponseEntity<?> updateRole(@Valid @NotNull @RequestBody Role role) throws Exception {
        try {
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SUCCESS, roleService.updateRole(role)), HttpStatus.OK);
        } catch (CommonException e) {
            return new ResponseEntity<ResponseBody>(new ResponseBody(e.getResponse().getResponseCode(), e.getMessage()), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Exception when /role/updateRole/ ", e);
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SYSTEM_ERROR, e.getMessage()), HttpStatus.OK);
        }
    }

    @Operation(description = "Truy vấn danh sách Role nằm trong 1 GroupRole")
    @ApiResponses({
            @ApiResponse(responseCode = "0000", description = "Thành công", content = @Content),
            @ApiResponse(responseCode = "9999", description = "Lỗi hệ thống", content = @Content)
    })
    @PostMapping("/findRoleByGroupRoleId/")
    public ResponseEntity<?> findRoleByGroupRoleId(@Valid @NotNull @RequestBody GroupRoleMap request) throws Exception {
        try {
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SUCCESS, roleService.findRoleByGroupRoleId(request.getGroupRoleId())), HttpStatus.OK);
        } catch (CommonException e) {
            return new ResponseEntity<ResponseBody>(new ResponseBody(e.getResponse().getResponseCode(), e.getMessage()), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Exception when /role/findRoleByGroupRoleId/ ", e);
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SYSTEM_ERROR, e.getMessage()), HttpStatus.OK);
        }
    }

    @Operation(description = "Tìm kiếm toàn bộ quyền theo user id")
    @ApiResponses({
            @ApiResponse(responseCode = "0000", description = "Thành công", content = @Content),
            @ApiResponse(responseCode = "9999", description = "Lỗi hệ thống", content = @Content)
    })
    @PostMapping("/findRoleByUserId/")
    public ResponseEntity<?> findRoleByUserId(@Valid @NotNull @RequestBody UserRole userRole) throws Exception {
        try {
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SUCCESS, roleService.findRoleByUserId(userRole.getUserId())), HttpStatus.OK);
        } catch (CommonException e) {
            return new ResponseEntity<ResponseBody>(new ResponseBody(e.getResponse().getResponseCode(), e.getMessage()), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Exception when /role/findRoleByUserId/ ", e);
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SYSTEM_ERROR, e.getMessage()), HttpStatus.OK);
        }
    }

    @Operation(description = "Gán trực tiếp hoặc bỏ gán 1 role vào 1 user")
    @ApiResponses({
            @ApiResponse(responseCode = "0000", description = "Thành công", content = @Content),
            @ApiResponse(responseCode = "0006", description = "Thiếu dữ liệu đầu vào bắt buộc", content = @Content),
            @ApiResponse(responseCode = "9999", description = "Lỗi hệ thống", content = @Content)
    })
    @PostMapping("/mergeUserRole/")
    public ResponseEntity<?> mergeUserRole(@Valid @NotNull @RequestBody UserRole userRole) throws Exception {
        try {
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SUCCESS, roleService.mergeUserRole(userRole)), HttpStatus.OK);
        } catch (CommonException e) {
            return new ResponseEntity<ResponseBody>(new ResponseBody(e.getResponse().getResponseCode(), e.getMessage()), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Exception when /role/mergeUserRole/ ", e);
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SYSTEM_ERROR, e.getMessage()), HttpStatus.OK);
        }
    }

    @Operation(description = "Cập nhật nhóm quyền GroupRole")
    @ApiResponses({
            @ApiResponse(responseCode = "0000", description = "Thành công", content = @Content),
            @ApiResponse(responseCode = "0001", description = "Đối tượng đã tồn tại", content = @Content),
            @ApiResponse(responseCode = "0002", description = "Đối tượng không tồn tại", content = @Content),
            @ApiResponse(responseCode = "0006", description = "Thiếu dữ liệu đầu vào bắt buộc", content = @Content),
            @ApiResponse(responseCode = "9999", description = "Lỗi hệ thống", content = @Content)
    })
    @PostMapping("/updateGroupRole/")
    public ResponseEntity<?> updateGroupRole(@Valid @NotNull @RequestBody GroupRole groupRole) throws Exception {
        try {
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SUCCESS, roleService.updateGroupRole(groupRole)), HttpStatus.OK);
        } catch (CommonException e) {
            return new ResponseEntity<ResponseBody>(new ResponseBody(e.getResponse().getResponseCode(), e.getMessage()), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Exception when /role/updateGroupRole/ ", e);
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SYSTEM_ERROR, e.getMessage()), HttpStatus.OK);
        }
    }

    @Operation(description = "Cập nhật trạng thái UserRole")
    @ApiResponses({
            @ApiResponse(responseCode = "0000", description = "Thành công", content = @Content),
            @ApiResponse(responseCode = "0002", description = "Đối tượng không tồn tại", content = @Content),
            @ApiResponse(responseCode = "0008", description = "Dữ liệu đầu vào không đúng định dạng", content = @Content),
            @ApiResponse(responseCode = "9999", description = "Lỗi hệ thống", content = @Content)
    })
    @PostMapping("/updateStatusUserRole/")
    public ResponseEntity<?> updateStatusUserRole(@Valid @NotNull @RequestBody UserRole userRole) throws Exception {
        try {
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SUCCESS, roleService.updateStatusUserRole(userRole.getUserId(), userRole.getRoleId(), userRole.getStatus())), HttpStatus.OK);
        } catch (CommonException e) {
            return new ResponseEntity<ResponseBody>(new ResponseBody(e.getResponse().getResponseCode(), e.getMessage()), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Exception when /role/createUserRole/ ", e);
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SYSTEM_ERROR, e.getMessage()), HttpStatus.OK);
        }
    }

    @Operation(description = "Lấy danh sách menu cho 1 user")
    @ApiResponses({
            @ApiResponse(responseCode = "0000", description = "Thành công", content = @Content),
            @ApiResponse(responseCode = "0007", description = "Không tìm thấy dữ liệu cần tra cứu", content = @Content),
            @ApiResponse(responseCode = "9999", description = "Lỗi hệ thống", content = @Content)
    })
    @PostMapping("/getMenuByUserId/")
    public ResponseEntity<?> getMenuByUserId(@Valid @NotNull @RequestBody UserGroupRole userGroupRole) throws Exception {
        try {
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SUCCESS, roleService.getMenuByUserId(userGroupRole.getUserId())), HttpStatus.OK);
        } catch (CommonException e) {
            return new ResponseEntity<ResponseBody>(new ResponseBody(e.getResponse().getResponseCode(), e.getMessage()), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Exception when /role/getMenuByUserId/ ", e);
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SYSTEM_ERROR, e.getMessage()), HttpStatus.OK);
        }
    }

    @Operation(description = "Lấy danh sách menu theo userId + appCode")
    @ApiResponses({
            @ApiResponse(responseCode = "0000", description = "Thành công", content = @Content),
            @ApiResponse(responseCode = "0007", description = "Không tìm thấy dữ liệu cần tra cứu", content = @Content),
            @ApiResponse(responseCode = "9999", description = "Lỗi hệ thống", content = @Content)
    })
    @PostMapping("/getMenuByUserIdAndAppCode/")
    public ResponseEntity<?> getMenuByUserIdAndAppCode(@Valid @NotNull @RequestBody UserGroupRole userGroupRole) throws Exception {
        try {
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SUCCESS,
                    roleService.getMenuByUserIdAndAppCode(userGroupRole.getUserId(), userGroupRole.getAppCode())), HttpStatus.OK);
        } catch (CommonException e) {
            return new ResponseEntity<ResponseBody>(new ResponseBody(e.getResponse().getResponseCode(), e.getMessage()), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Exception when /role/getMenuByUserIdAndAppCode/ ", e);
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SYSTEM_ERROR, e.getMessage()), HttpStatus.OK);
        }
    }

    @Operation(description = "Thêm mới nhóm quyền GroupRole")
    @ApiResponses({
            @ApiResponse(responseCode = "0000", description = "Thành công", content = @Content),
            @ApiResponse(responseCode = "0001", description = "Đối tượng đã tồn tại", content = @Content),
            @ApiResponse(responseCode = "0006", description = "Thiếu dữ liệu đầu vào bắt buộc", content = @Content),
            @ApiResponse(responseCode = "9999", description = "Lỗi hệ thống", content = @Content)
    })
    @PostMapping("/createGroupRole/")
    public ResponseEntity<?> createGroupRole(@Valid @NotNull @RequestBody GroupRole groupRole) throws Exception {
        try {
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SUCCESS, roleService.createGroupRole(groupRole)), HttpStatus.OK);
        } catch (CommonException e) {
            return new ResponseEntity<ResponseBody>(new ResponseBody(e.getResponse().getResponseCode(), e.getMessage()), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Exception when /role/createGroupRole/ ", e);
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SYSTEM_ERROR, e.getMessage()), HttpStatus.OK);
        }
    }

    @Operation(description = "Cập nhật trạng thái nhóm quyền GroupRole")
    @ApiResponses({
            @ApiResponse(responseCode = "0000", description = "Thành công", content = @Content),
            @ApiResponse(responseCode = "0001", description = "Đối tượng đã tồn tại", content = @Content),
            @ApiResponse(responseCode = "0002", description = "Đối tượng không tồn tại", content = @Content),
            @ApiResponse(responseCode = "0006", description = "Thiếu dữ liệu đầu vào bắt buộc", content = @Content),
            @ApiResponse(responseCode = "9999", description = "Lỗi hệ thống", content = @Content)
    })
    @PostMapping("/updateStatusGroupRole/")
    public ResponseEntity<?> updateStatusGroupRole(@Valid @NotNull @RequestBody GroupRole groupRole) throws Exception {
        try {
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SUCCESS, roleService.updateStatusGroupRole(groupRole.getId(), groupRole.getStatus())), HttpStatus.OK);
        } catch (CommonException e) {
            return new ResponseEntity<ResponseBody>(new ResponseBody(e.getResponse().getResponseCode(), e.getMessage()), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Exception when /role/updateStatusGroupRole/ ", e);
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SYSTEM_ERROR, e.getMessage()), HttpStatus.OK);
        }
    }

    @Operation(description = "Thêm mới hoặc loại bỏ 1 menu nhỏ vào nhóm menu")
    @ApiResponses({
            @ApiResponse(responseCode = "0000", description = "Thành công", content = @Content),
            @ApiResponse(responseCode = "0006", description = "Thiếu dữ liệu đầu vào bắt buộc", content = @Content),
            @ApiResponse(responseCode = "9999", description = "Lỗi hệ thống", content = @Content)
    })
    @PostMapping("/mergeGroupRoleMap/")
    public ResponseEntity<?> mergeGroupRoleMap(@Valid @NotNull @RequestBody GroupRoleMap groupRoleMap) throws Exception {
        try {
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SUCCESS, roleService.mergeGroupRoleMap(groupRoleMap)), HttpStatus.OK);
        } catch (CommonException e) {
            return new ResponseEntity<ResponseBody>(new ResponseBody(e.getResponse().getResponseCode(), e.getMessage()), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Exception when /role/mergeGroupRoleMap/ ", e);
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SYSTEM_ERROR, e.getMessage()), HttpStatus.OK);
        }
    }

    @Operation(description = "Truy vấn phân trang danh sách quyền role")
    @ApiResponses({
            @ApiResponse(responseCode = "0000", description = "Thành công", content = @Content),
            @ApiResponse(responseCode = "9999", description = "Lỗi hệ thống", content = @Content)
    })
    @PostMapping("/getPageRole/")
    public ResponseEntity<?> getPageRole(@Valid @NotNull @RequestBody PageRequest pageRequest) throws Exception {
        try {
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SUCCESS, roleService.getPageRole(pageRequest)), HttpStatus.OK);
        } catch (CommonException e) {
            return new ResponseEntity<ResponseBody>(new ResponseBody(e.getResponse().getResponseCode(), e.getMessage()), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Exception when /role/getPageRole/ ", e);
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SYSTEM_ERROR, e.getMessage()), HttpStatus.OK);
        }
    }

    @Operation(description = "Truy vấn phân trang danh sách nhóm quyền GroupRole")
    @ApiResponses({
            @ApiResponse(responseCode = "0000", description = "Thành công", content = @Content),
            @ApiResponse(responseCode = "9999", description = "Lỗi hệ thống", content = @Content)
    })
    @PostMapping("/getPageGroupRole/")
    public ResponseEntity<?> getPageGroupRole(@Valid @NotNull @RequestBody PageRequest pageRequest) throws Exception {
        try {
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SUCCESS, roleService.getPageGroupRole(pageRequest)), HttpStatus.OK);
        } catch (CommonException e) {
            return new ResponseEntity<ResponseBody>(new ResponseBody(e.getResponse().getResponseCode(), e.getMessage()), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Exception when /role/getPageGroupRole/ ", e);
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SYSTEM_ERROR, e.getMessage()), HttpStatus.OK);
        }
    }

    @Operation(description = "Truy vấn phân trang danh sách map nhóm quyền và quyền GroupRoleMap")
    @ApiResponses({
            @ApiResponse(responseCode = "0000", description = "Thành công", content = @Content),
            @ApiResponse(responseCode = "9999", description = "Lỗi hệ thống", content = @Content)
    })
    @PostMapping("/getPageGroupRoleMap/")
    public ResponseEntity<?> getPageGroupRoleMap(@Valid @NotNull @RequestBody PageRequest pageRequest) throws Exception {
        try {
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SUCCESS, roleService.getPageGroupRoleMap(pageRequest)), HttpStatus.OK);
        } catch (CommonException e) {
            return new ResponseEntity<ResponseBody>(new ResponseBody(e.getResponse().getResponseCode(), e.getMessage()), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Exception when /role/getPageGroupRoleMap/ ", e);
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SYSTEM_ERROR, e.getMessage()), HttpStatus.OK);
        }
    }

    @Operation(description = "Truy vấn phân trang danh sách map quyền vào người dùng user UserRole")
    @ApiResponses({
            @ApiResponse(responseCode = "0000", description = "Thành công", content = @Content),
            @ApiResponse(responseCode = "9999", description = "Lỗi hệ thống", content = @Content)
    })
    @PostMapping("/getPageUserRole/")
    public ResponseEntity<?> getPageUserRole(@Valid @NotNull @RequestBody PageRequest pageRequest) throws Exception {
        try {
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SUCCESS, roleService.getPageUserRole(pageRequest)), HttpStatus.OK);
        } catch (CommonException e) {
            return new ResponseEntity<ResponseBody>(new ResponseBody(e.getResponse().getResponseCode(), e.getMessage()), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Exception when /role/getPageUserRole/ ", e);
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SYSTEM_ERROR, e.getMessage()), HttpStatus.OK);
        }
    }

    @Operation(description = "Truy vấn phân trang danh sách map nhóm quyền vào người dùng user UserGroupRole")
    @ApiResponses({
            @ApiResponse(responseCode = "0000", description = "Thành công", content = @Content),
            @ApiResponse(responseCode = "9999", description = "Lỗi hệ thống", content = @Content)
    })
    @PostMapping("/getPageUserGroupRole/")
    public ResponseEntity<?> getPageUserGroupRole(@Valid @NotNull @RequestBody PageRequest pageRequest) throws Exception {
        try {
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SUCCESS, roleService.getPageUserGroupRole(pageRequest)), HttpStatus.OK);
        } catch (CommonException e) {
            return new ResponseEntity<ResponseBody>(new ResponseBody(e.getResponse().getResponseCode(), e.getMessage()), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Exception when /role/getPageUserGroupRole/ ", e);
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SYSTEM_ERROR, e.getMessage()), HttpStatus.OK);
        }
    }

    @Operation(description = "Truy vấn toàn bộ menu, trong đó phân loại menu nào đã được gán với userId đầu vào")
    @ApiResponses({
            @ApiResponse(responseCode = "0000", description = "Thành công", content = @Content),
            @ApiResponse(responseCode = "9999", description = "Lỗi hệ thống", content = @Content)
    })
    @PostMapping("/findGroupRoleByUserId/")
    public ResponseEntity<?> findGroupRoleByUserId(@Valid @NotNull @RequestBody UserGroupRole userGroupRole) throws Exception {
        try {
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SUCCESS, roleService.findGroupRoleByUserId(userGroupRole.getUserId())), HttpStatus.OK);
        } catch (CommonException e) {
            return new ResponseEntity<ResponseBody>(new ResponseBody(e.getResponse().getResponseCode(), e.getMessage()), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Exception when /role/findGroupRoleByUserId/ ", e);
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SYSTEM_ERROR, e.getMessage()), HttpStatus.OK);
        }
    }

    @Operation(description = "Truy vấn tree view phân quyền  cho 1 user")
    @ApiResponses({
            @ApiResponse(responseCode = "0000", description = "Thành công", content = @Content),
            @ApiResponse(responseCode = "0007", description = "Không tìm thấy dữ liệu cần tra cứu", content = @Content),
            @ApiResponse(responseCode = "9999", description = "Lỗi hệ thống", content = @Content)
    })
    @PostMapping("/findUserRole/")
    public ResponseEntity<?> findUserRole(@Valid @NotNull @RequestBody UserGroupRole userGroupRole) throws Exception {
        try {
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SUCCESS, roleService.findUserRole(userGroupRole)), HttpStatus.OK);
        } catch (CommonException e) {
            return new ResponseEntity<ResponseBody>(new ResponseBody(e.getResponse().getResponseCode(), e.getMessage()), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Exception when /role/findUserRole/ ", e);
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SYSTEM_ERROR, e.getMessage()), HttpStatus.OK);
        }
    }

    @Operation(description = "Thực hiện phân quyền cho người dùng")
    @ApiResponses({
            @ApiResponse(responseCode = "0000", description = "Thành công", content = @Content),
            @ApiResponse(responseCode = "0007", description = "Không tìm thấy dữ liệu cần tra cứu", content = @Content),
            @ApiResponse(responseCode = "9999", description = "Lỗi hệ thống", content = @Content)
    })
    @PostMapping("/setUserRole/")
    public ResponseEntity<?> setUserRole(@Valid @NotNull @RequestBody UserRoleRequest userRoleRequest) throws Exception {
        try {
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SUCCESS, roleService.setUserRole(userRoleRequest)), HttpStatus.OK);
        } catch (CommonException e) {
            return new ResponseEntity<ResponseBody>(new ResponseBody(e.getResponse().getResponseCode(), e.getMessage()), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Exception when /role/setUserRole/ ", e);
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SYSTEM_ERROR, e.getMessage()), HttpStatus.OK);
        }
    }

    @Operation(description = "Gán/Hủy Gán menu vào user")
    @ApiResponses({
            @ApiResponse(responseCode = "0000", description = "Thành công", content = @Content),
            @ApiResponse(responseCode = "0006", description = "Thiếu dữ liệu đầu vào bắt buộc", content = @Content),
            @ApiResponse(responseCode = "9999", description = "Lỗi hệ thống", content = @Content)
    })
    @PostMapping("/mergeUserGroupRole/")
    public ResponseEntity<?> mergeUserGroupRole(@Valid @NotNull @RequestBody UserGroupRole userGroupRole) throws Exception {
        try {
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SUCCESS, roleService.mergeUserGroupRole(userGroupRole)), HttpStatus.OK);
        } catch (CommonException e) {
            return new ResponseEntity<ResponseBody>(new ResponseBody(e.getResponse().getResponseCode(), e.getMessage()), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Exception when /role/mergeUserGroupRole/ ", e);
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SYSTEM_ERROR, e.getMessage()), HttpStatus.OK);
        }
    }

    @Operation(description = "Lấy thứ tự cao nhất hiện tại của role quyền trong 1 nhóm quyền")
    @ApiResponses({
            @ApiResponse(responseCode = "0000", description = "Thành công", content = @Content),
            @ApiResponse(responseCode = "0006", description = "Thiếu dữ liệu đầu vào bắt buộc", content = @Content),
            @ApiResponse(responseCode = "9999", description = "Lỗi hệ thống", content = @Content)
    })
    @PostMapping("/getMaxPriorityGroupRoleMap/")
    public ResponseEntity<?> getMaxPriorityGroupRoleMap(@Valid @NotNull @RequestBody GroupRoleMap groupRoleMap) throws Exception {
        try {
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SUCCESS, roleService.getMaxPriorityGroupRoleMap(groupRoleMap)), HttpStatus.OK);
        } catch (CommonException e) {
            return new ResponseEntity<ResponseBody>(new ResponseBody(e.getResponse().getResponseCode(), e.getMessage()), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Exception when /role/getMaxPriorityGroupRoleMap/ ", e);
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SYSTEM_ERROR, e.getMessage()), HttpStatus.OK);
        }
    }

    @Operation(description = "Truy vấn danh sách nhóm quyền")
    @ApiResponses({
            @ApiResponse(responseCode = "0000", description = "Thành công", content = @Content),
            @ApiResponse(responseCode = "0006", description = "Thiếu dữ liệu đầu vào bắt buộc", content = @Content),
            @ApiResponse(responseCode = "9999", description = "Lỗi hệ thống", content = @Content)
    })
    @PostMapping("/getListGroupRole/")
    public ResponseEntity<?> getListGroupRole(@Valid @NotNull @RequestBody GroupRole groupRole) throws Exception {
        try {
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SUCCESS, roleService.getListGroupRole(groupRole)), HttpStatus.OK);
        } catch (CommonException e) {
            return new ResponseEntity<ResponseBody>(new ResponseBody(e.getResponse().getResponseCode(), e.getMessage()), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Exception when /role/getListGroupRole/", e);
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SYSTEM_ERROR, e.getMessage()), HttpStatus.OK);
        }
    }


    @Operation(description = "Truy vấn danh sách chức năng")
    @ApiResponses({
            @ApiResponse(responseCode = "0000", description = "Thành công", content = @Content),
            @ApiResponse(responseCode = "0006", description = "Thiếu dữ liệu đầu vào bắt buộc", content = @Content),
            @ApiResponse(responseCode = "9999", description = "Lỗi hệ thống", content = @Content)
    })
    @PostMapping("/getListRole/")
    public ResponseEntity<?> getListRole(@Valid @NotNull @RequestBody Role role) throws Exception {
        try {
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SUCCESS, roleService.getListRole(role)), HttpStatus.OK);
        } catch (CommonException e) {
            return new ResponseEntity<ResponseBody>(new ResponseBody(e.getResponse().getResponseCode(), e.getMessage()), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Exception when /role/getListRole/", e);
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SYSTEM_ERROR, e.getMessage()), HttpStatus.OK);
        }
    }

    @Operation(description = "Truy vấn danh sách chức năng theo GroupRoleId và RoleStatus")
    @ApiResponses({
            @ApiResponse(responseCode = "0000", description = "Thành công", content = @Content),
            @ApiResponse(responseCode = "0006", description = "Thiếu dữ liệu đầu vào bắt buộc", content = @Content),
            @ApiResponse(responseCode = "9999", description = "Lỗi hệ thống", content = @Content)
    })
    @PostMapping("/findByGroupRoleIdAndStatus/")
    public ResponseEntity<?> findByGroupRoleIdAndStatus(@Valid @NotNull @RequestBody GroupRoleIdAndStatus groupRoleIdAndStatus) throws Exception {
        try {
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SUCCESS, roleService.findByGroupRoleIdAndStatus(groupRoleIdAndStatus)), HttpStatus.OK);
        } catch (CommonException e) {
            return new ResponseEntity<ResponseBody>(new ResponseBody(e.getResponse().getResponseCode(), e.getMessage()), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Exception when /role/getListRole/", e);
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SYSTEM_ERROR, e.getMessage()), HttpStatus.OK);
        }
    }


    @Operation(description = "Đưa vị trí của 1 menu trong nhóm menu lên trên. Nghĩa là làm giảm giá trị GroupRoleMap.priority")
    @ApiResponses({
            @ApiResponse(responseCode = "0000", description = "Thành công", content = @Content),
            @ApiResponse(responseCode = "0006", description = "Thiếu dữ liệu đầu vào bắt buộc", content = @Content),
            @ApiResponse(responseCode = "9999", description = "Lỗi hệ thống", content = @Content)
    })
    @PostMapping("/upGroupRoleMap/")
    public ResponseEntity<?> upGroupRoleMap(@Valid @NotNull @RequestBody GroupRoleMap groupRoleMap) throws Exception {
        try {
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SUCCESS, roleService.upGroupRoleMap(groupRoleMap)), HttpStatus.OK);
        } catch (CommonException e) {
            return new ResponseEntity<ResponseBody>(new ResponseBody(e.getResponse().getResponseCode(), e.getMessage()), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Exception when /role/upGroupRoleMap/ ", e);
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SYSTEM_ERROR, e.getMessage()), HttpStatus.OK);
        }
    }

    @Operation(description = "Đưa vị trí của 1 menu trong nhóm menu xuống dưới. Nghĩa là làm tăng giá trị GroupRoleMap.priority")
    @ApiResponses({
            @ApiResponse(responseCode = "0000", description = "Thành công", content = @Content),
            @ApiResponse(responseCode = "0006", description = "Thiếu dữ liệu đầu vào bắt buộc", content = @Content),
            @ApiResponse(responseCode = "9999", description = "Lỗi hệ thống", content = @Content)
    })
    @PostMapping("/downGroupRoleMap/")
    public ResponseEntity<?> downGroupRoleMap(@Valid @NotNull @RequestBody GroupRoleMap groupRoleMap) throws Exception {
        try {
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SUCCESS, roleService.downGroupRoleMap(groupRoleMap)), HttpStatus.OK);
        } catch (CommonException e) {
            return new ResponseEntity<ResponseBody>(new ResponseBody(e.getResponse().getResponseCode(), e.getMessage()), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Exception when /role/downGroupRoleMap/ ", e);
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SYSTEM_ERROR, e.getMessage()), HttpStatus.OK);
        }
    }

    @Operation(description = "Đưa vị trí của 1 nhóm menu trong tài khoản lên trên. Nghĩa là làm giảm giá trị UserGroupRole.priority")
    @ApiResponses({
            @ApiResponse(responseCode = "0000", description = "Thành công", content = @Content),
            @ApiResponse(responseCode = "0006", description = "Thiếu dữ liệu đầu vào bắt buộc", content = @Content),
            @ApiResponse(responseCode = "9999", description = "Lỗi hệ thống", content = @Content)
    })
    @PostMapping("/upUserGroupRole/")
    public ResponseEntity<?> upUserGroupRole(@Valid @NotNull @RequestBody UserGroupRole userGroupRole) throws Exception {
        try {
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SUCCESS, roleService.upUserGroupRole(userGroupRole)), HttpStatus.OK);
        } catch (CommonException e) {
            return new ResponseEntity<ResponseBody>(new ResponseBody(e.getResponse().getResponseCode(), e.getMessage()), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Exception when /role/upUserGroupRole/ ", e);
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SYSTEM_ERROR, e.getMessage()), HttpStatus.OK);
        }
    }

    @Operation(description = "Đưa vị trí của 1 nhóm menu trong tài khoản xuống dưới. Nghĩa là làm tăng giá trị UserGroupRole.priority")
    @ApiResponses({
            @ApiResponse(responseCode = "0000", description = "Thành công", content = @Content),
            @ApiResponse(responseCode = "0006", description = "Thiếu dữ liệu đầu vào bắt buộc", content = @Content),
            @ApiResponse(responseCode = "9999", description = "Lỗi hệ thống", content = @Content)
    })
    @PostMapping("/downUserGroupRole/")
    public ResponseEntity<?> downUserGroupRole(@Valid @NotNull @RequestBody UserGroupRole userGroupRole) throws Exception {
        try {
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SUCCESS, roleService.downUserGroupRole(userGroupRole)), HttpStatus.OK);
        } catch (CommonException e) {
            return new ResponseEntity<ResponseBody>(new ResponseBody(e.getResponse().getResponseCode(), e.getMessage()), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Exception when /role/downUserGroupRole/ ", e);
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SYSTEM_ERROR, e.getMessage()), HttpStatus.OK);
        }
    }

    @Operation(description = "Danh sách role quyền là menu và trạng thái hoạt động")
    @ApiResponses({
            @ApiResponse(responseCode = "0000", description = "Thành công", content = @Content),
            @ApiResponse(responseCode = "9999", description = "Lỗi hệ thống", content = @Content)
    })
    @PostMapping("/getParentRole/")
    public ResponseEntity<?> getParentRole() throws Exception {
        try {
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SUCCESS, roleService.getParentRole()), HttpStatus.OK);
        } catch (CommonException e) {
            return new ResponseEntity<ResponseBody>(new ResponseBody(e.getResponse().getResponseCode(), e.getMessage()), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Exception when /role/getParentRole/ ", e);
            return new ResponseEntity<ResponseBody>(new ResponseBody(Response.SYSTEM_ERROR, e.getMessage()), HttpStatus.OK);
        }
    }
}
