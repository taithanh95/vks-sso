package com.bitsco.vks.sso.component;

import com.bitsco.vks.common.constant.Constant;
import com.bitsco.vks.common.util.ArrayListCommon;
import com.bitsco.vks.sso.cache.CacheService;
import com.bitsco.vks.sso.entities.GroupUser;
import com.bitsco.vks.sso.entities.Role;
import com.bitsco.vks.sso.entities.User;
import com.bitsco.vks.sso.entities.UserRole;
import com.bitsco.vks.sso.repository.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class InitComponent {
    private static final Logger LOGGER = LogManager.getLogger(Constant.LOG_APPENDER.APPLICATION);

    @Autowired
    CacheService cacheService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRoleRepository userRoleRepository;

    @Autowired
    GroupUserRepository groupUserRepository;

    @Autowired
    GroupUserGroupRoleRepository groupUserGroupRoleRepository;

    @Autowired
    GroupUserRoleRepository groupUserRoleRepository;

    @PostConstruct
    public void initCache() {
        try {
            List<User> userList = userRepository.findAll();
            if (!ArrayListCommon.isNullOrEmpty(userList))
                userList.stream().forEach(x -> cacheService.addUser2RedisCache(x));
        } catch (Exception e) {
            LOGGER.error("Exception when initCache", e);
        }
        try {
            //Load toàn bộ role phương thức lên redis để check trên gateway
            List<Role> roleList = roleRepository.findByTypeAndStatus(Constant.ROLE.TYPE.METHOD, Constant.STATUS_OBJECT.ACTIVE);
            if (!ArrayListCommon.isNullOrEmpty(roleList)) {
                roleList.forEach(x -> cacheService.addRole2RedisCache(x));
                LOGGER.info("pushAllRoleMethodToRedisCache >>> size = " + roleList.size());
            } else
                LOGGER.info("pushAllRoleMethodToRedisCache >>> size = 0");
        } catch (Exception e) {
            LOGGER.error("Exception when pushAllRoleMethodToRedisCache", e);
        }
        try {
            //Load toàn bộ quyền của user có các phương thức để kiểm tra trên gateway
            List<UserRole> userRoleList = userRoleRepository.getListUserRoleMethod();
            if (!ArrayListCommon.isNullOrEmpty(userRoleList)) {
                userRoleList.forEach(x -> cacheService.addUserRole2RedisCache(x));
                LOGGER.info("pushAllUserRoleMethodToRedisCache >>> size = " + userRoleList.size());
            } else
                LOGGER.info("pushAllUserRoleMethodToRedisCache >>> size = 0");
        } catch (Exception e) {
            LOGGER.error("Exception when pushAllUserRoleMethodToRedisCache", e);
        }
        try {
            //Load toàn bộ nhóm người dùng/tài khoản lên cache redis
            List<GroupUser> groupUserList = groupUserRepository.findByStatus(Constant.STATUS_OBJECT.ACTIVE);
            if (!ArrayListCommon.isNullOrEmpty(groupUserList)) {
                groupUserList.forEach(x -> {
                    x.setGroupUserGroupRoleList(groupUserGroupRoleRepository.findByGroupUserIdAndStatus(x.getId(), Constant.STATUS_OBJECT.ACTIVE));
                    x.setGroupUserRoleList(groupUserRoleRepository.findByGroupUserIdAndStatus(x.getId(), Constant.STATUS_OBJECT.ACTIVE));
                    cacheService.addGroupUser2RedisCache(x);
                });
                LOGGER.info("pushAllUserRoleMethodToRedisCache >>> size = " + groupUserList.size());
            } else
                LOGGER.info("pushAllUserRoleMethodToRedisCache >>> size = 0");
        } catch (Exception e) {
            LOGGER.error("Exception when pushAllUserRoleMethodToRedisCache", e);
        }
    }
}
