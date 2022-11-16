package com.bitsco.vks.sso.service;

import com.bitsco.vks.common.constant.Constant;
import com.bitsco.vks.common.exception.CommonException;
import com.bitsco.vks.common.response.Response;
import com.bitsco.vks.common.util.ArrayListCommon;
import com.bitsco.vks.common.util.StringCommon;
import com.bitsco.vks.common.validate.ValidateCommon;
import com.bitsco.vks.sso.cache.CacheService;
import com.bitsco.vks.sso.entities.*;
import com.bitsco.vks.sso.model.TreeviewItem;
import com.bitsco.vks.sso.repository.*;
import com.bitsco.vks.sso.request.GroupUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class GroupUserServiceImpl implements GroupUserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    GroupUserRepository groupUserRepository;

    @Autowired
    GroupUserRoleRepository groupUserRoleRepository;

    @Autowired
    GroupUserGroupRoleRepository groupUserGroupRoleRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    GroupRoleRepository groupRoleRepository;

    @Autowired
    GroupRoleMapRepository groupRoleMapRepository;

    @Autowired
    RoleService roleService;

    @Autowired
    CacheService cacheService;

    @Override
    public GroupUser saveGroupUser(GroupUser groupUser) throws Exception {
        if (groupUser.getId() != null) groupUser.setUpdatedBy(cacheService.getUsernameFromHeader());
        else groupUser.setCreatedBy(cacheService.getUsernameFromHeader());
        GroupUser response = groupUserRepository.save(groupUser);
        response.setGroupUserGroupRoleList(groupUserGroupRoleRepository.findByGroupUserIdAndStatus(response.getId(), Constant.STATUS_OBJECT.ACTIVE));
        response.setGroupUserRoleList(groupUserRoleRepository.findByGroupUserIdAndStatus(response.getId(), Constant.STATUS_OBJECT.ACTIVE));
        cacheService.addGroupUser2RedisCache(response);
        return response;
    }

    @Override
    public GroupUserGroupRole saveGroupUserGroupRole(GroupUserGroupRole groupUserGroupRole) {
        if (groupUserGroupRole.getId() != null && groupUserGroupRole.getUpdatedBy() == null)
            groupUserGroupRole.setUpdatedBy(cacheService.getUsernameFromHeader());
        else if (groupUserGroupRole.getCreatedBy() == null)
            groupUserGroupRole.setCreatedBy(cacheService.getUsernameFromHeader());
        return groupUserGroupRoleRepository.save(groupUserGroupRole);
    }

    @Override
    public GroupUserRole saveGroupUserRole(GroupUserRole groupUserRole) {
        if (groupUserRole.getId() != null && groupUserRole.getUpdatedBy() == null)
            groupUserRole.setUpdatedBy(cacheService.getUsernameFromHeader());
        else if (groupUserRole.getCreatedBy() == null) groupUserRole.setCreatedBy(cacheService.getUsernameFromHeader());
        return groupUserRoleRepository.save(groupUserRole);
    }

    @Override
    public GroupUser createGroupUser(GroupUser groupUser) throws Exception {
        ValidateCommon.validateNullObject(groupUser.getName(), "name");
        groupUser.setStatus(Constant.STATUS_OBJECT.ACTIVE);
        return saveGroupUser(groupUser);
    }

    @Override
    public GroupUser updateGroupUser(GroupUser groupUser) throws Exception {
        GroupUser old = groupUserRepository.findById(groupUser.getId()).orElse(null);
        if (old == null)
            throw new CommonException(Response.OBJECT_NOT_FOUND, Response.OBJECT_NOT_FOUND.getResponseMessage());
        else return saveGroupUser(old.copyFrom(groupUser));
    }

    @Override
    public GroupUser findById(long id) throws Exception {
        GroupUser groupUser = cacheService.getGroupUserFromCache(id);
        if (groupUser == null)
            return groupUserRepository.findById(id).orElse(null);
        else return groupUser;
    }

    @Override
    public List<GroupUser> getListGroupUser(GroupUser groupUser) throws Exception {
        List<GroupUser> list = groupUserRepository.getList(
                StringCommon.isNullOrBlank(groupUser.getName()) ? null : StringCommon.addLikeRightAndLeft(groupUser.getName().toLowerCase()),
                StringCommon.isNullOrBlank(groupUser.getDescription()) ? null : StringCommon.addLikeRightAndLeft(groupUser.getDescription().toLowerCase()),
                groupUser.getStatus());
        if (ArrayListCommon.isNullOrEmpty(list))
            throw new CommonException(Response.DATA_NOT_FOUND, Response.DATA_NOT_FOUND.getResponseMessage());
        return list;
    }

    @Override
    public void setUserRoleByGroupUserId(User user) throws Exception {
        if (user.getId() == null || user.getGroupUserId() == null)
            return;
        GroupUser groupUser = findById(user.getGroupUserId());
        if (groupUser == null || groupUser.getStatus() == null || groupUser.getStatus() != Constant.STATUS_OBJECT.ACTIVE)
            throw new CommonException(Response.DATA_INVALID, "Nhóm tài khoản không xác định");
        if (ArrayListCommon.isNullOrEmpty(groupUser.getGroupUserGroupRoleList()))
            groupUser.setGroupUserGroupRoleList(groupUserGroupRoleRepository.findByGroupUserIdAndStatus(groupUser.getId(), Constant.STATUS_OBJECT.ACTIVE));
        if (ArrayListCommon.isNullOrEmpty(groupUser.getGroupUserRoleList()))
            groupUser.setGroupUserRoleList(groupUserRoleRepository.findByGroupUserIdAndStatus(groupUser.getId(), Constant.STATUS_OBJECT.ACTIVE));
        if (!ArrayListCommon.isNullOrEmpty(groupUser.getGroupUserGroupRoleList()))
            for (GroupUserGroupRole x : groupUser.getGroupUserGroupRoleList())
                roleService.mergeUserGroupRole(new UserGroupRole(x.getGroupRoleId(), user.getId(), Constant.STATUS_OBJECT.ACTIVE));
        if (!ArrayListCommon.isNullOrEmpty(groupUser.getGroupUserRoleList()))
            for (GroupUserRole y : groupUser.getGroupUserRoleList())
                roleService.mergeUserRole(new UserRole(user.getId(), y.getRoleId(), Constant.STATUS_OBJECT.ACTIVE));
    }

    @Override
    public Response setGroupRoleAndRoleByGroupUserId(GroupUserRequest groupUserRequest) throws Exception {
        //Trước tiên xóa hết các nhóm quyền và quyền của nhóm người dùng
        ValidateCommon.validateNullObject(groupUserRequest.getGroupUserId(), "groupUserId");
        final long groupUserId = groupUserRequest.getGroupUserId();
        List<GroupUserGroupRole> oldGroupUserGroupRoleList = groupUserGroupRoleRepository.findByGroupUserIdAndStatus(groupUserId, Constant.STATUS_OBJECT.ACTIVE);
        if (!ArrayListCommon.isNullOrEmpty(oldGroupUserGroupRoleList))
            oldGroupUserGroupRoleList.forEach(x -> {
                x.setStatus(Constant.STATUS_OBJECT.INACTIVE);
                saveGroupUserGroupRole(x);
            });
        List<GroupUserRole> oldGroupUserRoleList = groupUserRoleRepository.findByGroupUserIdAndStatus(groupUserId, Constant.STATUS_OBJECT.ACTIVE);
        if (!ArrayListCommon.isNullOrEmpty(oldGroupUserRoleList))
            oldGroupUserRoleList.forEach(x -> {
                x.setStatus(Constant.STATUS_OBJECT.INACTIVE);
                saveGroupUserRole(x);
            });
        if (ArrayListCommon.isNullOrEmpty(groupUserRequest.getItems()))
            return Response.SUCCESS;
        //Thực hiện active các nhóm quyền và quyền được checked(true)
        for (TreeviewItem x : groupUserRequest.getItems()) {
            boolean groupRoleChecked = false;
            if (!ArrayListCommon.isNullOrEmpty(x.getInternalChildren()))
                for (TreeviewItem y : x.getInternalChildren()) {
                    if (y.isInternalChecked()) {
                        activeGroupUserRole(groupUserId, y.getValue());
                        groupRoleChecked = true;
                    }
                    if (!ArrayListCommon.isNullOrEmpty(y.getInternalChildren()))
                        for (TreeviewItem z : y.getInternalChildren())
                            if (z.isInternalChecked())
                                activeGroupUserRole(groupUserId, z.getValue());
                }
            if (groupRoleChecked || x.isInternalChecked())
                activeGroupUserGroupRole(groupUserId, x.getValue());
        }
        //Load lại cache cho GroupUser vào redis
        return Response.SUCCESS;
    }

    @Override
    public Response deleteGroupUser(GroupUser groupUser) throws Exception {
        try{
            groupUserRepository.deleteById(groupUser.getId());
            return Response.SUCCESS;
        }catch (CommonException e){
            return Response.OBJECT_IS_INVALID;
        }

    }

    private void activeGroupUserGroupRole(long groupUserId, long groupRoleId) throws Exception {
        GroupUserGroupRole groupUserGroupRole = findGroupUserGroupRoleById(groupUserId, groupRoleId);
        if (groupUserGroupRole == null)
            saveGroupUserGroupRole(new GroupUserGroupRole(groupUserId, groupRoleId, Constant.STATUS_OBJECT.ACTIVE));
        else {
            groupUserGroupRole.setStatus(Constant.STATUS_OBJECT.ACTIVE);
            saveGroupUserGroupRole(groupUserGroupRole);
        }
    }

    private void activeGroupUserRole(long groupUserId, long roleId) throws Exception {
        GroupUserRole groupUserRole = findGroupUserRoleById(groupUserId, roleId);
        if (groupUserRole == null)
            saveGroupUserRole(new GroupUserRole(groupUserId, roleId, Constant.STATUS_OBJECT.ACTIVE));
        else {
            groupUserRole.setStatus(Constant.STATUS_OBJECT.ACTIVE);
            saveGroupUserRole(groupUserRole);
        }
    }

    @Override
    public GroupUserGroupRole mergeGroupUserGroupRole(GroupUserGroupRole groupUserGroupRole) throws Exception {
        GroupUserGroupRole old = findGroupUserGroupRoleById(groupUserGroupRole.getGroupUserId(), groupUserGroupRole.getGroupRoleId());
        if (old != null) {
            old.setStatus(Constant.STATUS_OBJECT.ACTIVE);
            return saveGroupUserGroupRole(old);
        } else return saveGroupUserGroupRole(groupUserGroupRole);
    }

    @Override
    public GroupUserRole mergeGroupUserRole(GroupUserRole groupUserRole) throws Exception {
        GroupUserRole old = findGroupUserRoleById(groupUserRole.getGroupUserId(), groupUserRole.getRoleId());
        if (old != null) {
            old.setStatus(Constant.STATUS_OBJECT.ACTIVE);
            return saveGroupUserRole(old);
        } else return saveGroupUserRole(groupUserRole);
    }

    @Override
    public GroupUserRole findGroupUserRoleById(long groupUserId, long roleId) {
        return groupUserRoleRepository.findFristByGroupUserIdAndRoleId(groupUserId, roleId);
    }

    @Override
    public GroupUserGroupRole findGroupUserGroupRoleById(long groupUserId, long groupRoleId) {
        return groupUserGroupRoleRepository.findFirstByGroupUserIdAndGroupRoleId(groupUserId, groupRoleId);
    }

    @Override
    public List<GroupRole> getGroupRoleByGroupUserId(GroupUserRequest groupUserRequest) throws Exception {
        ValidateCommon.validateNullObject(groupUserRequest.getGroupUserId(), "groupUserId");
        final long groupUserId = groupUserRequest.getGroupUserId();
        List<GroupRole> groupRoleList = groupRoleRepository.findByStatus(Constant.STATUS_OBJECT.ACTIVE);
        if (ArrayListCommon.isNullOrEmpty(groupRoleList))
            throw new CommonException(Response.DATA_NOT_FOUND);
        groupRoleList.forEach(x -> {
            x.setChecked(groupUserGroupRoleRepository.existsByGroupUserIdAndGroupRoleIdAndStatus(groupUserId, x.getId(), Constant.STATUS_OBJECT.ACTIVE));
            x.setChildren(roleRepository.findByGroupRoleId(x.getId(), Constant.STATUS_OBJECT.ACTIVE, Constant.ROLE.TYPE.MENU, Constant.STATUS_OBJECT.ACTIVE));
            if (!ArrayListCommon.isNullOrEmpty(x.getChildren()))
                x.getChildren().forEach(y -> {
                    y.setChecked(groupUserRoleRepository.existsByGroupUserIdAndRoleIdAndStatus(groupUserId, y.getId(), Constant.STATUS_OBJECT.ACTIVE));
                    y.setChildren(roleRepository.findByParentIdAndStatus(y.getId(), Constant.STATUS_OBJECT.ACTIVE));
                    if (!ArrayListCommon.isNullOrEmpty(y.getChildren()))
                        y.getChildren().forEach(z -> z.setChecked(groupUserRoleRepository.existsByGroupUserIdAndRoleIdAndStatus(groupUserId, z.getId(), Constant.STATUS_OBJECT.ACTIVE)));
                });
        });
        return groupRoleList;
    }
}
