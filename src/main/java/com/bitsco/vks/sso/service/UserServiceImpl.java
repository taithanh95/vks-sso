package com.bitsco.vks.sso.service;

import com.bitsco.vks.common.constant.MessageContent;
import com.bitsco.vks.common.exception.CommonException;
import com.bitsco.vks.common.response.Response;
import com.bitsco.vks.common.util.ArrayListCommon;
import com.bitsco.vks.common.util.MessageCommon;
import com.bitsco.vks.common.util.StringCommon;
import com.bitsco.vks.common.validate.ValidateCommon;
import com.bitsco.vks.sso.cache.CacheService;
import com.bitsco.vks.sso.entities.User;
import com.bitsco.vks.sso.repository.UserRepository;
import com.bitsco.vks.sso.repository.jdbc.AdmUserRepository;
import com.bitsco.vks.sso.thread.GrantRoleThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    CacheService cacheService;
    @Autowired
    GrantRoleThread grantRoleThread;
    @Autowired
    AdmUserRepository admUserRepository;

    @Override
    public User save(User user) throws Exception {
        if (user.getId() != null && user.getUpdatedBy() == null)
            user.setUpdatedBy(cacheService.getUsernameFromHeader());
        else if (user.getCreatedBy() == null) user.setCreatedBy(cacheService.getUsernameFromHeader());
        User response = userRepository.save(user);
        cacheService.addUser2RedisCache(response);
        return response;
    }

    @Override
    public User update(User user) throws Exception {
        ValidateCommon.validateNullObject(user.getId(), "id");
        User userOld = findById(user.getId());
        if (userOld == null)
            throw new CommonException(Response.OBJECT_NOT_FOUND, MessageCommon.getMessage(MessageContent.OBJECT_NOT_FOUND, "user"));
        return save(userOld.coppyFrom(user));
    }

    @Override
    public Response delete(User user) {
        try {
            userRepository.deleteById(user.getId());
            return Response.SUCCESS;
        } catch (CommonException e) {
            return Response.OBJECT_IS_INVALID;
        }
    }

    @Override
    public User findById(Long id) throws Exception {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User findFirstByUsername(String username) throws Exception {
//        User user = cacheService.getUserFromCache(username);
//        if (user != null) return user;
        return userRepository.findFirstByUsername(username);
    }

    @Override
    public User findFirstByUsernameAndEmail(String username, String email) throws Exception {
        return userRepository.findFirstByUsernameAndEmail(username, email);
    }

    @Override
    public List<User> getList(User user) throws Exception {
        List<User> userList = userRepository.getList(
                StringCommon.isNullOrBlank(user.getUsername()) ? null : StringCommon.addLikeRightAndLeft(user.getUsername().trim()).toLowerCase(),
                StringCommon.isNullOrBlank(user.getName()) ? null : StringCommon.addLikeRightAndLeft(user.getName().trim()).toLowerCase(),
                StringCommon.isNullOrBlank(user.getAddress()) ? null : StringCommon.addLikeRightAndLeft(user.getAddress().trim()).toLowerCase(),
                StringCommon.isNullOrBlank(user.getSppid()) ? null : StringCommon.addLikeRightAndLeft(user.getSppid().trim()).toLowerCase(),
                StringCommon.isNullOrBlank(user.getInspcode()) ? null : StringCommon.addLikeRightAndLeft(user.getInspcode().trim()).toLowerCase(),
                user.getGroupid(),
                user.getDepartid(),
                user.getType(),
                user.getStatus(),
                user.getGroupUserId()
        );
        if (ArrayListCommon.isNullOrEmpty(userList))
            throw new CommonException(Response.DATA_NOT_FOUND);
        userList.forEach(x -> {
            if (x.getGroupUserId() != null)
                x.setGroupUser(cacheService.getGroupUserFromCache(x.getGroupUserId()));
        });
        return userList;
    }
}
