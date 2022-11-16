package com.bitsco.vks.sso.service;

import com.bitsco.vks.common.request.PageRequest;
import com.bitsco.vks.common.response.PageResponse;
import com.bitsco.vks.common.response.Response;
import com.bitsco.vks.sso.entities.*;
import com.bitsco.vks.sso.request.GroupRoleIdAndStatus;
import com.bitsco.vks.sso.request.UserRoleRequest;

import java.util.List;

public interface RoleService {
    Role saveRole(Role role) throws Exception;

    Role createRole(Role role) throws Exception;

    Role findRoleById(long id) throws Exception;

    List<Role> findRoleByUserId(long userId) throws Exception;

    List<Role> findRoleByGroupRoleId(Long groupRoleId) throws Exception;

    Role updateRole(Role role) throws Exception;

    UserRole saveUserRole(UserRole userRole) throws Exception;

    UserRole mergeUserRole(UserRole userRole) throws Exception;

    UserRole findUserRoleById(long userId, long roleId) throws Exception;

    UserRole updateStatusUserRole(long userId, long roleId, int status) throws Exception;

    UserGroupRole findUserGroupRoleById(long userId, long groupRoleId) throws Exception;

    UserGroupRole saveUserGroupRole(UserGroupRole userGroupRole) throws Exception;

    UserGroupRole mergeUserGroupRole(UserGroupRole userGroupRole) throws Exception;

    GroupRole saveGroupRole(GroupRole groupRole) throws Exception;

    GroupRole createGroupRole(GroupRole groupRole) throws Exception;

    GroupRole updateGroupRole(GroupRole groupRole) throws Exception;

    GroupRole findGroupRoleById(long id) throws Exception;

    List<GroupRole> getMenuByUserId(long userId) throws Exception;

    List<GroupRole> getMenuByUserIdAndAppCode(long userId, String appCode) throws Exception;

    List<GroupRole> findUserRole(UserGroupRole userGroupRole) throws Exception;

    Response setUserRole(UserRoleRequest userRoleRequest) throws Exception;

    List<GroupRole> findGroupRoleByUserId(long userId) throws Exception;

    GroupRole updateStatusGroupRole(long id, int status) throws Exception;

    GroupRoleMap findGroupRoleMapById(long id) throws Exception;

    GroupRoleMap saveGroupRoleMap(GroupRoleMap groupRoleMap) throws Exception;

    GroupRoleMap mergeGroupRoleMap(GroupRoleMap groupRoleMap) throws Exception;

    PageResponse getPageRole(PageRequest requestPage) throws Exception;

    PageResponse getPageGroupRole(PageRequest pageRequest) throws Exception;

    PageResponse getPageGroupRoleMap(PageRequest pageRequest) throws Exception;

    PageResponse getPageUserRole(PageRequest pageRequest) throws Exception;

    PageResponse getPageUserGroupRole(PageRequest pageRequest) throws Exception;

    Integer getMaxPriorityGroupRoleMap(GroupRoleMap groupRoleMap) throws Exception;

    List<GroupRole> getListGroupRole(GroupRole groupRole) throws Exception;

    List<Role> getListRole(Role role) throws Exception;

    List<Role> findByGroupRoleIdAndStatus(GroupRoleIdAndStatus groupRoleIdAndStatus) throws Exception;

    GroupRoleMap upGroupRoleMap(GroupRoleMap groupRoleMap) throws Exception;

    GroupRoleMap downGroupRoleMap(GroupRoleMap groupRoleMap) throws Exception;

    UserGroupRole upUserGroupRole(UserGroupRole userGroupRole) throws Exception;

    UserGroupRole downUserGroupRole(UserGroupRole userGroupRole) throws Exception;

    List<Role> getParentRole() throws Exception;
}
