package com.bitsco.vks.sso.service;

import com.bitsco.vks.common.constant.Constant;
import com.bitsco.vks.common.constant.MessageContent;
import com.bitsco.vks.common.exception.CommonException;
import com.bitsco.vks.common.request.PageRequest;
import com.bitsco.vks.common.response.PageResponse;
import com.bitsco.vks.common.response.Response;
import com.bitsco.vks.common.util.ArrayListCommon;
import com.bitsco.vks.common.util.MessageCommon;
import com.bitsco.vks.common.util.PageCommon;
import com.bitsco.vks.common.util.StringCommon;
import com.bitsco.vks.common.validate.ValidateCommon;
import com.bitsco.vks.sso.cache.CacheService;
import com.bitsco.vks.sso.entities.*;
import com.bitsco.vks.sso.model.TreeviewItem;
import com.bitsco.vks.sso.repository.*;
import com.bitsco.vks.sso.request.GroupRoleIdAndStatus;
import com.bitsco.vks.sso.request.UserRoleRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    GroupRoleRepository groupRoleRepository;
    @Autowired
    GroupRoleMapRepository groupRoleMapRepository;
    @Autowired
    UserRoleRepository userRoleRepository;
    @Autowired
    UserGroupRoleRepository userGroupRoleRepository;
    @Autowired
    CacheService cacheService;

    @Override
    public Role saveRole(Role role) throws Exception {
        if (role.getId() != null) role.setUpdatedBy(cacheService.getUsernameFromHeader());
        else role.setCreatedBy(cacheService.getUsernameFromHeader());
        Role response = roleRepository.save(role);
        if (response.getType() == Constant.ROLE.TYPE.METHOD)
            if (response.getStatus() == Constant.STATUS_OBJECT.ACTIVE)
                cacheService.addRole2RedisCache(response);
            else cacheService.removeRole2RedisCache(response);
        return response;
    }

    @Override
    public Role createRole(Role role) throws Exception {
        ValidateCommon.validateNullObject(role.getName(), "name");
        ValidateCommon.validateNullObject(role.getUrl(), "url");
        if (role.getType() == null) role.setType(Constant.ROLE.TYPE.MENU);
        role.setStatus(Constant.STATUS_OBJECT.ACTIVE);
        Role roleOld = roleRepository.findFirstByUrl(role.getUrl());
        if (roleOld != null)
            if (roleOld.getStatus() == Constant.STATUS_OBJECT.ACTIVE)
                throw new CommonException(Response.OBJECT_IS_EXISTS, MessageCommon.getMessage(MessageContent.OBJECT_IS_EXISTS, Constant.TABLE.ROLE));
            else return saveRole(roleOld.copyFrom(role));
        return saveRole(role);
    }

    @Override
    public Role findRoleById(long id) throws Exception {
        return roleRepository.findById(id).orElse(null);
    }

    @Override
    public List<Role> findRoleByUserId(long userId) throws Exception {
        return roleRepository.findByUserId(userId);
    }

    @Override
    public List<Role> findRoleByGroupRoleId(Long groupRoleId) throws Exception {
        List<Role> list = new ArrayList<>();
        List<Role> listActive = new ArrayList<>();
        List<Role> listInactive = new ArrayList<>();
        GroupRole groupRole = findGroupRoleById(groupRoleId);
        if (groupRole == null)
            return list;
        List<Role> roleList = roleRepository.findByStatusAndTypeAndUrlLike(Constant.STATUS_OBJECT.ACTIVE, Constant.ROLE.TYPE.MENU, (!StringCommon.isNullOrBlank(groupRole.getUrl()) ? groupRole.getUrl() : "") + "%");
        for (Role x : roleList) {
            Role role = groupRoleMapRepository.findByGroupRoleIdAndRoleIdAndStatus(groupRoleId, x.getId(), Constant.STATUS_OBJECT.ACTIVE);
            if (role != null)
                listActive.add(new Role(x.getId(), x.getName(), x.getUrl(), Constant.STATUS_OBJECT.ACTIVE, role.getPriority() == null ? 0 : role.getPriority()));
            else
                listInactive.add(new Role(x.getId(), x.getName(), x.getUrl(), Constant.STATUS_OBJECT.INACTIVE, null));
        }
        if (!ArrayListCommon.isNullOrEmpty(listActive)) {
            listActive.sort(Comparator.comparing(Role::getPriority, Comparator.nullsFirst(Comparator.reverseOrder())).reversed());
            list.addAll(listActive);
        }
        if (!ArrayListCommon.isNullOrEmpty(listInactive))
            list.addAll(listInactive);
        return list;
    }

    @Override
    public Role updateRole(Role role) throws Exception {
        ValidateCommon.validateNullObject(role.getId(), "id");
        Role roleOld = findRoleById(role.getId());
        if (roleOld == null)
            throw new CommonException(Response.OBJECT_NOT_FOUND, MessageCommon.getMessage(MessageContent.OBJECT_NOT_FOUND, Constant.TABLE.ROLE));
        if (!StringCommon.isNullOrBlank(role.getUrl())) {
            if (!roleOld.getUrl().equals(role.getUrl()) && groupRoleRepository.existsByUrl(role.getUrl()))
                throw new CommonException(Response.OBJECT_IS_EXISTS, MessageCommon.getMessage(MessageContent.OBJECT_IS_EXISTS, Constant.TABLE.GROUP_ROLE));
        }
        return saveRole(roleOld.copyFrom(role));
    }

    @Override
    public UserRole saveUserRole(UserRole userRole) {
        if (userRole.getId() != null) userRole.setUpdatedBy(cacheService.getUsernameFromHeader());
        else userRole.setCreatedBy(cacheService.getUsernameFromHeader());
        UserRole response = userRoleRepository.save(userRole);
        Role role = cacheService.getRoleFromCache(response.getRoleId());
        if (role != null && role.getType() != null && role.getType() == Constant.ROLE.TYPE.METHOD && role.getStatus() != null && role.getStatus() == Constant.STATUS_OBJECT.ACTIVE)
            if (response.getStatus() == Constant.STATUS_OBJECT.ACTIVE)
                cacheService.addUserRole2RedisCache(response);
            else cacheService.removeUserRole2RedisCache(response);
        return response;
    }

    @Override
    public UserRole mergeUserRole(UserRole userRole) throws Exception {
        ValidateCommon.validateNullObject(userRole.getUserId(), "userId");
        ValidateCommon.validateNullObject(userRole.getRoleId(), "roleId");
        ValidateCommon.validateNullObject(userRole.getStatus(), "status");
        UserRole userRoleOld = findUserRoleById(userRole.getUserId(), userRole.getRoleId());
        if (userRoleOld != null) {
            userRoleOld.setStatus(userRole.getStatus());
            return saveUserRole(userRoleOld);
        } else return saveUserRole(userRole);
    }

    @Override
    public UserRole findUserRoleById(long userId, long roleId) throws Exception {
        return userRoleRepository.findFirstByUserIdAndRoleId(userId, roleId);
    }

    @Override
    public UserRole updateStatusUserRole(long userId, long roleId, int status) throws Exception {
        ValidateCommon.validateStatusObject(status);
        UserRole userRoleOld = findUserRoleById(userId, roleId);
        if (userRoleOld != null) {
            userRoleOld.setStatus(status);
            return saveUserRole(userRoleOld);
        } else
            throw new CommonException(Response.OBJECT_NOT_FOUND, MessageCommon.getMessage(MessageContent.OBJECT_NOT_FOUND, Constant.TABLE.USER_ROLE));
    }

    @Override
    public UserGroupRole saveUserGroupRole(UserGroupRole userGroupRole) {
        if (userGroupRole.getId() != null && userGroupRole.getUpdatedBy() == null)
            userGroupRole.setUpdatedBy(cacheService.getUsernameFromHeader());
        else if (userGroupRole.getCreatedBy() == null) userGroupRole.setCreatedBy(cacheService.getUsernameFromHeader());
        return userGroupRoleRepository.save(userGroupRole);
    }

    @Override
    public UserGroupRole findUserGroupRoleById(long userId, long groupRoleId) throws Exception {
        return userGroupRoleRepository.findFirstByUserIdAndGroupRoleId(userId, groupRoleId);
    }

    @Override
    public UserGroupRole mergeUserGroupRole(UserGroupRole userGroupRole) throws Exception {
        ValidateCommon.validateNullObject(userGroupRole.getGroupRoleId(), "groupRoleId");
        ValidateCommon.validateNullObject(userGroupRole.getUserId(), "userId");
        ValidateCommon.validateNullObject(userGroupRole.getStatus(), "status");
        ValidateCommon.validateStatusObject(userGroupRole.getStatus());

        if (userGroupRole.getStatus().equals(Constant.STATUS_OBJECT.ACTIVE) && userGroupRole.getPriority() == null) {
            Integer priority = userGroupRoleRepository.getMaxPriority(userGroupRole.getUserId());
            userGroupRole.setPriority(priority == null ? 1 : (priority + 1));
        }
        UserGroupRole response = null;
        UserGroupRole userGroupRoleOld = findUserGroupRoleById(userGroupRole.getUserId(), userGroupRole.getGroupRoleId());
        if (userGroupRoleOld != null) {
            userGroupRoleOld.setStatus(userGroupRole.getStatus());
            response = saveUserGroupRole(userGroupRoleOld);
        } else response = saveUserGroupRole(userGroupRole);
        List<UserGroupRole> list = userGroupRoleRepository.findByUserIdAndStatus(userGroupRole.getUserId(), Constant.STATUS_OBJECT.ACTIVE);
        if (!ArrayListCommon.isNullOrEmpty(list))
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setPriority(i + 1);
                saveUserGroupRole(list.get(i));
            }
        return response;
    }

    @Override
    public GroupRole saveGroupRole(GroupRole groupRole) throws Exception {
        if (groupRole.getId() != null) groupRole.setUpdatedBy(cacheService.getUsernameFromHeader());
        else groupRole.setCreatedBy(cacheService.getUsernameFromHeader());
        return groupRoleRepository.save(groupRole);
    }

    @Override
    public GroupRole createGroupRole(GroupRole groupRole) throws Exception {
        ValidateCommon.validateNullObject(groupRole.getName(), "name");
        ValidateCommon.validateNullObject(groupRole.getUrl(), "url");
        ValidateCommon.validateNullObject(groupRole.getIcon(), "icon");
        if (groupRole.getType() == null) groupRole.setType(Constant.GROUP_ROLE.TYPE.MENU);
        groupRole.setStatus(Constant.STATUS_OBJECT.ACTIVE);
        GroupRole groupRoleOld = groupRoleRepository.findFirstByUrlAndName(groupRole.getUrl(), groupRole.getName());
        if (groupRoleOld != null)
            if (groupRoleOld.getStatus() == Constant.STATUS_OBJECT.ACTIVE)
                throw new CommonException(Response.OBJECT_IS_EXISTS, MessageCommon.getMessage(MessageContent.OBJECT_IS_EXISTS, Constant.TABLE.ROLE));
            else return saveGroupRole(groupRoleOld.coppyFrom(groupRole));
        return saveGroupRole(groupRole);
    }

    @Override
    public GroupRole updateGroupRole(GroupRole groupRole) throws Exception {
        ValidateCommon.validateNullObject(groupRole.getId(), "id");
        GroupRole groupRoleOld = findGroupRoleById(groupRole.getId());
        if (groupRoleOld == null)
            throw new CommonException(Response.OBJECT_NOT_FOUND, MessageCommon.getMessage(MessageContent.OBJECT_NOT_FOUND, Constant.TABLE.GROUP_ROLE));
        if (!StringCommon.isNullOrBlank(groupRole.getUrl())) {
            groupRole.setUrl(groupRole.getUrl().trim().toLowerCase());
            if (!groupRoleOld.getUrl().equals(groupRole.getUrl()) && groupRoleRepository.existsByUrl(groupRole.getUrl()))
                throw new CommonException(Response.OBJECT_IS_EXISTS, MessageCommon.getMessage(MessageContent.OBJECT_IS_EXISTS, Constant.TABLE.GROUP_ROLE));
        }
        return saveGroupRole(groupRoleOld.coppyFrom(groupRole));
    }

    @Override
    public GroupRole findGroupRoleById(long id) throws Exception {
        return groupRoleRepository.findById(id).orElse(null);
    }

    @Override
    public List<GroupRole> getMenuByUserId(long userId) throws Exception {
        List<UserGroupRole> userGroupRoleList = userGroupRoleRepository.findByUserIdAndStatusOrderByPriority(userId, Constant.STATUS_OBJECT.ACTIVE);
        if (ArrayListCommon.isNullOrEmpty(userGroupRoleList))
            throw new CommonException(Response.DATA_NOT_FOUND);
        List<GroupRole> groupRoleList = new ArrayList<>();
        for (UserGroupRole ugr : userGroupRoleList) {
            GroupRole groupRole = findGroupRoleById(ugr.getGroupRoleId());
            if (groupRole != null && groupRole.getStatus() != null && groupRole.getStatus() == Constant.STATUS_OBJECT.ACTIVE) {
                List<GroupRoleMap> groupRoleMaps = groupRoleMapRepository.findByGroupRoleIdOrderByPriority(groupRole.getId());
                if (!ArrayListCommon.isNullOrEmpty(groupRoleMaps)) {
                    List<Role> roleList = new ArrayList<>();
                    groupRoleMaps.forEach(x -> {
                        if (x.getStatus() != null && x.getStatus() == Constant.STATUS_OBJECT.ACTIVE && userRoleRepository.existsByUserIdAndRoleIdAndStatus(userId, x.getRoleId(), Constant.STATUS_OBJECT.ACTIVE)) {
                            Role role = roleRepository.findById(x.getRoleId()).orElse(null);
                            if (role != null && role.getStatus() != null && role.getStatus() == Constant.STATUS_OBJECT.ACTIVE) {
                                roleList.add(role);
                            }
                        }
                    });
                    groupRole.setChildren(roleList);
                }
                groupRoleList.add(groupRole);
            }
        }
        return groupRoleList;
    }

    @Override
    public List<GroupRole> getMenuByUserIdAndAppCode(long userId, String appCode) throws Exception {
        List<GroupRole> groupRoleList = groupRoleRepository.getListGroupRole(
                userId,
                appCode,
                Constant.STATUS_OBJECT.ACTIVE
        );
        for (GroupRole groupRole : groupRoleList) {
            if (groupRole != null && groupRole.getStatus() != null && groupRole.getStatus() == Constant.STATUS_OBJECT.ACTIVE) {
                groupRole.setChildren(roleRepository.findByGroupRoleId(groupRole.getId(), Constant.STATUS_OBJECT.ACTIVE, Constant.ROLE.TYPE.MENU, Constant.STATUS_OBJECT.ACTIVE));
            }
        }
        if (ArrayListCommon.isNullOrEmpty(groupRoleList))
            throw new CommonException(Response.DATA_NOT_FOUND);
        return groupRoleList;
    }

    @Override
    public List<GroupRole> findUserRole(UserGroupRole userGroupRole) throws Exception {
        ValidateCommon.validateNullObject(userGroupRole.getUserId(), "userId");
        final long userId = userGroupRole.getUserId();
        List<GroupRole> groupRoleList = groupRoleRepository.findByStatus(Constant.STATUS_OBJECT.ACTIVE);
        if (ArrayListCommon.isNullOrEmpty(groupRoleList))
            throw new CommonException(Response.DATA_NOT_FOUND);
        groupRoleList.forEach(x -> {
            x.setChecked(userGroupRoleRepository.existsByUserIdAndGroupRoleIdAndStatus(userId, x.getId(), Constant.STATUS_OBJECT.ACTIVE));
            x.setChildren(roleRepository.findByGroupRoleId(x.getId(), Constant.STATUS_OBJECT.ACTIVE, Constant.ROLE.TYPE.MENU, Constant.STATUS_OBJECT.ACTIVE));
            if (!ArrayListCommon.isNullOrEmpty(x.getChildren()))
                x.getChildren().forEach(y -> {
                    y.setChecked(userRoleRepository.existsByUserIdAndRoleIdAndStatus(userId, y.getId(), Constant.STATUS_OBJECT.ACTIVE));
                    y.setChildren(roleRepository.findByParentIdAndStatus(y.getId(), Constant.STATUS_OBJECT.ACTIVE));
                    if (!ArrayListCommon.isNullOrEmpty(y.getChildren()))
                        y.getChildren().forEach(z -> z.setChecked(userRoleRepository.existsByUserIdAndRoleIdAndStatus(userId, z.getId(), Constant.STATUS_OBJECT.ACTIVE)));
                });
        });
        return groupRoleList;
    }

    @Override
    public Response setUserRole(UserRoleRequest userRoleRequest) throws Exception {
        ValidateCommon.validateNullObject(userRoleRequest.getUserId(), "userId");
        final long userId = userRoleRequest.getUserId();
        List<UserGroupRole> oldUserGroupRoleList = userGroupRoleRepository.findByUserIdAndStatus(userId, Constant.STATUS_OBJECT.ACTIVE);
        if (!ArrayListCommon.isNullOrEmpty(oldUserGroupRoleList))
            oldUserGroupRoleList.forEach(x -> {
                x.setStatus(Constant.STATUS_OBJECT.INACTIVE);
                saveUserGroupRole(x);
            });
        List<UserRole> oldUserRoleList = userRoleRepository.findByUserIdAndStatus(userId, Constant.STATUS_OBJECT.ACTIVE);
        if (!ArrayListCommon.isNullOrEmpty(oldUserRoleList))
            oldUserRoleList.forEach(x -> {
                x.setStatus(Constant.STATUS_OBJECT.INACTIVE);
                saveUserRole(x);
            });
        if (ArrayListCommon.isNullOrEmpty(userRoleRequest.getItems()))
            return Response.SUCCESS;
        for (TreeviewItem x : userRoleRequest.getItems()) {
            boolean groupRoleChecked = false;
            if (!ArrayListCommon.isNullOrEmpty(x.getInternalChildren()))
                for (TreeviewItem y : x.getInternalChildren()) {
                    if (y.isInternalChecked()) {
                        activeUserRole(userId, y.getValue());
                        groupRoleChecked = true;
                    }
                    if (!ArrayListCommon.isNullOrEmpty(y.getInternalChildren()))
                        for (TreeviewItem z : y.getInternalChildren())
                            if (z.isInternalChecked())
                                activeUserRole(userId, z.getValue());
                }
            if (groupRoleChecked || x.isInternalChecked())
                activeUserGroupRole(userId, x.getValue());
        }
        return Response.SUCCESS;
    }

    private void activeUserGroupRole(long userId, long groupRoleId) throws Exception {
        UserGroupRole userGroupRole = findUserGroupRoleById(userId, groupRoleId);
        if (userGroupRole == null)
            saveUserGroupRole(new UserGroupRole(userId, groupRoleId, Constant.STATUS_OBJECT.ACTIVE));
        else {
            userGroupRole.setStatus(Constant.STATUS_OBJECT.ACTIVE);
            saveUserGroupRole(userGroupRole);
        }
    }

    private void activeUserRole(long userId, long roleId) throws Exception {
        UserRole userRole = findUserRoleById(userId, roleId);
        if (userRole == null)
            saveUserRole(new UserRole(userId, roleId, Constant.STATUS_OBJECT.ACTIVE));
        else {
            userRole.setStatus(Constant.STATUS_OBJECT.ACTIVE);
            saveUserRole(userRole);
        }
    }

    @Override
    public List<GroupRole> findGroupRoleByUserId(long userId) throws Exception {
        List<GroupRole> list = new ArrayList<>();
        List<GroupRole> listActive = new ArrayList<>();
        List<GroupRole> listInactive = new ArrayList<>();
        List<GroupRole> groupRoleList = groupRoleRepository.findByStatus(Constant.STATUS_OBJECT.ACTIVE);
        groupRoleList.stream().forEach(x -> {
            UserGroupRole userGroupRole = userGroupRoleRepository.findFirstByUserIdAndGroupRoleId(userId, x.getId());
            if (userGroupRole != null && userGroupRole.getStatus() == Constant.STATUS_OBJECT.ACTIVE)
                listActive.add(new GroupRole(x.getId(), x.getName(), x.getUrl(), Constant.STATUS_OBJECT.ACTIVE, userGroupRole.getPriority() == null ? 0 : userGroupRole.getPriority()));
            else
                listInactive.add(new GroupRole(x.getId(), x.getName(), x.getUrl(), Constant.STATUS_OBJECT.INACTIVE));
        });
        if (!ArrayListCommon.isNullOrEmpty(listActive)) {
            listActive.sort(Comparator.comparing(GroupRole::getPriority, Comparator.nullsFirst(Comparator.reverseOrder())).reversed());
            list.addAll(listActive);
        }
        if (!ArrayListCommon.isNullOrEmpty(listInactive))
            list.addAll(listInactive);
        return list;
    }

    @Override
    public GroupRole updateStatusGroupRole(long id, int status) throws Exception {
        return null;
    }

    @Override
    public GroupRoleMap findGroupRoleMapById(long id) throws Exception {
        return groupRoleMapRepository.findById(id).orElse(null);
    }

    @Override
    public GroupRoleMap saveGroupRoleMap(GroupRoleMap groupRoleMap) throws Exception {
        if (groupRoleMap.getId() != null) groupRoleMap.setUpdatedBy(cacheService.getUsernameFromHeader());
        else groupRoleMap.setCreatedBy(cacheService.getUsernameFromHeader());
        return groupRoleMapRepository.save(groupRoleMap);
    }

    @Override
    public GroupRoleMap mergeGroupRoleMap(GroupRoleMap groupRoleMap) throws Exception {
        ValidateCommon.validateNullObject(groupRoleMap.getGroupRoleId(), "groupRoleId");
        ValidateCommon.validateNullObject(groupRoleMap.getRoleId(), "roleId");
        ValidateCommon.validateNullObject(groupRoleMap.getStatus(), "status");
        ValidateCommon.validateStatusObject(groupRoleMap.getStatus());
        if (groupRoleMap.getStatus().equals(Constant.STATUS_OBJECT.ACTIVE) && groupRoleMap.getPriority() == null) {
            Integer priority = groupRoleMapRepository.getMaxPriority(groupRoleMap.getGroupRoleId());
            groupRoleMap.setPriority(priority == null ? 1 : (priority + 1));
        }
        GroupRoleMap groupRoleMapOld = groupRoleMapRepository.findFirstByGroupRoleIdAndRoleId(groupRoleMap.getGroupRoleId(), groupRoleMap.getRoleId());
        GroupRoleMap response = null;
        if (groupRoleMapOld != null) {
            groupRoleMapOld.setStatus(groupRoleMap.getStatus());
            if (groupRoleMap.getStatus().equals(Constant.STATUS_OBJECT.ACTIVE))
                groupRoleMapOld.setPriority(groupRoleMap.getPriority());
            response = saveGroupRoleMap(groupRoleMapOld);
        } else response = saveGroupRoleMap(groupRoleMap);
        List<GroupRoleMap> list = groupRoleMapRepository.findByGroupRoleIdAndStatusOrderByPriority(groupRoleMap.getGroupRoleId(), Constant.STATUS_OBJECT.ACTIVE);
        if (!ArrayListCommon.isNullOrEmpty(list))
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setPriority(i + 1);
                saveGroupRoleMap(list.get(i));
            }
        return response;
    }

    @Override
    public PageResponse getPageRole(PageRequest pageRequest) throws Exception {
        Role role = (new ObjectMapper()).convertValue(pageRequest.getDataRequest(), Role.class);
        List<Role> list = roleRepository.getPage(StringCommon.isNullOrBlank(role.getName()) ? null : ("%" + role.getName() + "%"), role.getStatus());
        return PageCommon.toPageResponse(list, pageRequest.getPageNumber(), pageRequest.getPageSize());
    }

    @Override
    public PageResponse getPageGroupRole(PageRequest pageRequest) throws Exception {
        GroupRole groupRole = (new ObjectMapper()).convertValue(pageRequest.getDataRequest(), GroupRole.class);
        List<GroupRole> list = groupRoleRepository.getPage(StringCommon.isNullOrBlank(groupRole.getName()) ? null : ("%" + groupRole.getName() + "%"), groupRole.getStatus());
        return PageCommon.toPageResponse(list, pageRequest.getPageNumber(), pageRequest.getPageSize());
    }

    @Override
    public PageResponse getPageGroupRoleMap(PageRequest pageRequest) throws Exception {
        GroupRoleMap groupRoleMap = (new ObjectMapper()).convertValue(pageRequest.getDataRequest(), GroupRoleMap.class);
        List<GroupRoleMap> list = groupRoleMapRepository.getPage(groupRoleMap.getGroupRoleId(), groupRoleMap.getRoleId(), groupRoleMap.getStatus());
        return PageCommon.toPageResponse(list, pageRequest.getPageNumber(), pageRequest.getPageSize());
    }

    @Override
    public PageResponse getPageUserRole(PageRequest pageRequest) throws Exception {
        UserRole userRole = (new ObjectMapper()).convertValue(pageRequest.getDataRequest(), UserRole.class);
        List<UserRole> list = userRoleRepository.getPage(userRole.getRoleId(), userRole.getUserId(), userRole.getStatus());
        return PageCommon.toPageResponse(list, pageRequest.getPageNumber(), pageRequest.getPageSize());
    }

    @Override
    public PageResponse getPageUserGroupRole(PageRequest pageRequest) throws Exception {
        UserGroupRole userGroupRole = (new ObjectMapper()).convertValue(pageRequest.getDataRequest(), UserGroupRole.class);
        List<UserGroupRole> list = userGroupRoleRepository.getPage(userGroupRole.getGroupRoleId(), userGroupRole.getUserId(), userGroupRole.getStatus());
        return PageCommon.toPageResponse(list, pageRequest.getPageNumber(), pageRequest.getPageSize());
    }

    @Override
    public Integer getMaxPriorityGroupRoleMap(GroupRoleMap groupRoleMap) throws Exception {
        Integer max = groupRoleMapRepository.getMaxPriority(groupRoleMap.getGroupRoleId());
        return max == null ? 0 : max;
    }

    @Override
    public List<GroupRole> getListGroupRole(GroupRole groupRole) throws Exception {
        List<GroupRole> groupRoleList = groupRoleRepository.getList(
                StringCommon.isNullOrBlank(groupRole.getName()) ? null : StringCommon.addLikeRightAndLeft(groupRole.getName().toLowerCase()),
                StringCommon.isNullOrBlank(groupRole.getUrl()) ? null : StringCommon.addLikeRightAndLeft(groupRole.getUrl().trim().toLowerCase()),
                groupRole.getStatus()
        );
        if (ArrayListCommon.isNullOrEmpty(groupRoleList))
            throw new CommonException(Response.DATA_NOT_FOUND);
        return groupRoleList;
    }

    @Override
    public List<Role> getListRole(Role role) throws Exception {
        List<Role> roleList = roleRepository.getList(
                StringCommon.isNullOrBlank(role.getName()) ? null : StringCommon.addLikeRightAndLeft(role.getName().toLowerCase()),
                StringCommon.isNullOrBlank(role.getUrl()) ? null : StringCommon.addLikeRightAndLeft(role.getUrl().trim().toLowerCase()),
                role.getStatus()
        );
        if (ArrayListCommon.isNullOrEmpty(roleList))
            throw new CommonException(Response.DATA_NOT_FOUND);
        return roleList;
    }

    @Override
    public List<Role> findByGroupRoleIdAndStatus(GroupRoleIdAndStatus groupRoleIdAndStatus) throws Exception {
        ValidateCommon.validateNullObject(groupRoleIdAndStatus.getGroupRoleId(), "groupRoleId");
        ValidateCommon.validateNullObject(groupRoleIdAndStatus.getRoleStatus(), "roleStatus");
        List<Role> roleList = roleRepository.findByGroupRoleIdAndStatus(groupRoleIdAndStatus.getGroupRoleId(), groupRoleIdAndStatus.getRoleStatus());
        if (ArrayListCommon.isNullOrEmpty(roleList))
            throw new CommonException(Response.DATA_NOT_FOUND);
        return roleList;
    }

    @Override
    public GroupRoleMap upGroupRoleMap(GroupRoleMap groupRoleMap) throws Exception {
        ValidateCommon.validateNullObject(groupRoleMap.getGroupRoleId(), "groupRoleId");
        ValidateCommon.validateNullObject(groupRoleMap.getRoleId(), "roleId");
        ValidateCommon.validateNullObject(groupRoleMap.getPriority(), "priority");
        Integer priority = groupRoleMap.getPriority();
        if (priority.equals(1))
            throw new CommonException(Response.DATA_INVALID, "Bản ghi đang ở vị trí cao nhất, không thể tăng thêm");
        GroupRoleMap source = groupRoleMapRepository.findFirstByGroupRoleIdAndRoleId(groupRoleMap.getGroupRoleId(), groupRoleMap.getRoleId());
        GroupRoleMap des = groupRoleMapRepository.findFirstByGroupRoleIdAndPriorityAndStatus(groupRoleMap.getGroupRoleId(), priority - 1, Constant.STATUS_OBJECT.ACTIVE);
        if (source == null) throw new CommonException(Response.OBJECT_NOT_FOUND);
        if (!source.getPriority().equals(groupRoleMap.getPriority()))
            throw new CommonException(Response.DATA_INVALID, "Vị trí hiện tại không khớp");
        source.setPriority(priority - 1);
        if (des != null) {
            des.setPriority(priority);
            saveGroupRoleMap(des);
        }
        return saveGroupRoleMap(source);
    }

    @Override
    public GroupRoleMap downGroupRoleMap(GroupRoleMap groupRoleMap) throws Exception {
        ValidateCommon.validateNullObject(groupRoleMap.getGroupRoleId(), "groupRoleId");
        ValidateCommon.validateNullObject(groupRoleMap.getRoleId(), "roleId");
        ValidateCommon.validateNullObject(groupRoleMap.getPriority(), "priority");
        Integer priority = groupRoleMap.getPriority();
        if (priority.equals(groupRoleMapRepository.getMaxPriority(groupRoleMap.getGroupRoleId())))
            throw new CommonException(Response.DATA_INVALID, "Bản ghi đang ở vị trí thấp nhất, không thể giảm thêm");
        GroupRoleMap source = groupRoleMapRepository.findFirstByGroupRoleIdAndRoleId(groupRoleMap.getGroupRoleId(), groupRoleMap.getRoleId());
        GroupRoleMap des = groupRoleMapRepository.findFirstByGroupRoleIdAndPriorityAndStatus(groupRoleMap.getGroupRoleId(), priority + 1, Constant.STATUS_OBJECT.ACTIVE);
        if (source == null) throw new CommonException(Response.OBJECT_NOT_FOUND);
        if (!source.getPriority().equals(groupRoleMap.getPriority()))
            throw new CommonException(Response.DATA_INVALID, "Vị trí hiện tại không khớp");
        source.setPriority(priority + 1);
        if (des != null) {
            des.setPriority(priority);
            saveGroupRoleMap(des);
        }
        return saveGroupRoleMap(source);
    }

    @Override
    public UserGroupRole upUserGroupRole(UserGroupRole userGroupRole) throws Exception {
        ValidateCommon.validateNullObject(userGroupRole.getUserId(), "userId");
        ValidateCommon.validateNullObject(userGroupRole.getGroupRoleId(), "groupRoleId");
        ValidateCommon.validateNullObject(userGroupRole.getPriority(), "priority");
        Integer priority = userGroupRole.getPriority();
        if (priority.equals(1))
            throw new CommonException(Response.DATA_INVALID, "Bản ghi đang ở vị trí cao nhất, không thể tăng thêm");
        UserGroupRole source = userGroupRoleRepository.findFirstByUserIdAndGroupRoleId(userGroupRole.getUserId(), userGroupRole.getGroupRoleId());
        UserGroupRole des = userGroupRoleRepository.findFirstByUserIdAndPriorityAndStatus(userGroupRole.getUserId(), priority - 1, Constant.STATUS_OBJECT.ACTIVE);
        if (source == null) throw new CommonException(Response.OBJECT_NOT_FOUND);
        if (!source.getPriority().equals(userGroupRole.getPriority()))
            throw new CommonException(Response.DATA_INVALID, "Vị trí hiện tại không khớp");
        source.setPriority(priority - 1);
        if (des != null) {
            des.setPriority(priority);
            saveUserGroupRole(des);
        }
        return saveUserGroupRole(source);
    }

    @Override
    public UserGroupRole downUserGroupRole(UserGroupRole userGroupRole) throws Exception {
        ValidateCommon.validateNullObject(userGroupRole.getUserId(), "userId");
        ValidateCommon.validateNullObject(userGroupRole.getGroupRoleId(), "groupRoleId");
        ValidateCommon.validateNullObject(userGroupRole.getPriority(), "priority");
        Integer priority = userGroupRole.getPriority();
        if (priority.equals(userGroupRoleRepository.getMaxPriority(userGroupRole.getUserId())))
            throw new CommonException(Response.DATA_INVALID, "Bản ghi đang ở vị trí thấp nhất, không thể giảm thêm");
        UserGroupRole source = userGroupRoleRepository.findFirstByUserIdAndGroupRoleId(userGroupRole.getUserId(), userGroupRole.getGroupRoleId());
        UserGroupRole des = userGroupRoleRepository.findFirstByUserIdAndPriorityAndStatus(userGroupRole.getUserId(), priority + 1, Constant.STATUS_OBJECT.ACTIVE);
        if (source == null) throw new CommonException(Response.OBJECT_NOT_FOUND);
        if (!source.getPriority().equals(userGroupRole.getPriority()))
            throw new CommonException(Response.DATA_INVALID, "Vị trí hiện tại không khớp");
        source.setPriority(priority + 1);
        if (des != null) {
            des.setPriority(priority);
            saveUserGroupRole(des);
        }
        return saveUserGroupRole(source);
    }

    @Override
    public List<Role> getParentRole() throws Exception {
        return roleRepository.findByTypeAndStatus(Constant.ROLE.TYPE.MENU, Constant.STATUS_OBJECT.ACTIVE);
    }
}
