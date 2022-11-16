package com.bitsco.vks.sso.service;

import com.bitsco.vks.common.response.Response;
import com.bitsco.vks.sso.entities.*;
import com.bitsco.vks.sso.request.GroupUserRequest;

import java.util.List;

public interface GroupUserService {
    GroupUser saveGroupUser(GroupUser groupUser) throws Exception;

    GroupUserGroupRole saveGroupUserGroupRole(GroupUserGroupRole groupUserGroupRole) throws Exception;

    GroupUserRole saveGroupUserRole(GroupUserRole groupUserRole) throws Exception;

    GroupUser createGroupUser(GroupUser groupUser) throws Exception;

    GroupUserGroupRole mergeGroupUserGroupRole(GroupUserGroupRole groupUserGroupRole) throws Exception;

    GroupUserRole mergeGroupUserRole(GroupUserRole groupUserRole) throws Exception;

    GroupUser updateGroupUser(GroupUser groupUser) throws Exception;

    GroupUser findById(long id) throws Exception;

    List<GroupUser> getListGroupUser(GroupUser groupUser) throws Exception;

    GroupUserGroupRole findGroupUserGroupRoleById(long groupUserId, long groupRoleId) throws Exception;

    GroupUserRole findGroupUserRoleById(long groupUserId, long roleId) throws Exception;

    List<GroupRole> getGroupRoleByGroupUserId(GroupUserRequest groupUserRequest) throws Exception;

    void setUserRoleByGroupUserId(User user) throws Exception;

    Response setGroupRoleAndRoleByGroupUserId(GroupUserRequest groupUserRequest) throws Exception;

    Response deleteGroupUser(GroupUser groupUser) throws Exception;
}
