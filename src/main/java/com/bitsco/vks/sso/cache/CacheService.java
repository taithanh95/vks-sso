package com.bitsco.vks.sso.cache;

import com.bitsco.vks.common.constant.Constant;
import com.bitsco.vks.common.model.Otp;
import com.bitsco.vks.common.response.Token;
import com.bitsco.vks.common.util.StringCommon;
import com.bitsco.vks.sso.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: truongnq
 * @date: 06-May-19 4:02 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class CacheService {
    @Autowired
    RedisTemplate<String, Otp> otpRedis;

    @Autowired
    RedisTemplate<String, User> userRedis;

    @Autowired
    RedisTemplate<String, Param> paramRedis;

    @Autowired
    RedisTemplate<String, Token> tokenRedis;

    @Autowired
    RedisTemplate<String, GroupUser> groupUserRedis;

    @Autowired
    RedisTemplate<String, Role> roleRedis;

    @Autowired
    RedisTemplate<String, Role> roleUriRedis;

    @Autowired
    RedisTemplate<String, UserRole> userRoleRedis;

    @Autowired
    HttpServletRequest request;

    public void addOtp2RedisCache(Otp otp) {
        final ValueOperations<String, Otp> opsForValue = otpRedis.opsForValue();
        String key = otp.getCode();
        opsForValue.set(key, otp, otp.getExpiry(), TimeUnit.MILLISECONDS);
    }

    public Otp getOtpFromCache(String otpCode) {
        final ValueOperations<String, Otp> opsForValue = otpRedis.opsForValue();
        if (otpRedis.hasKey(otpCode))
            return opsForValue.get(otpCode);
        else return null;
    }

    public String getValueParamFromCache(String group, String code) {
        final ValueOperations<String, Param> opsForValue = paramRedis.opsForValue();
        String multiKey = group.trim().toUpperCase() + Constant.DASH + code.trim().toUpperCase();
        if (paramRedis.hasKey(multiKey))
            return opsForValue.get(multiKey).getValue();
        else return null;
    }


    public Param getParamFromCache(String group, String code) {
        final ValueOperations<String, Param> opsForValue = paramRedis.opsForValue();
        String multiKey = group.trim().toUpperCase() + Constant.DASH + code.trim().toUpperCase();
        if (paramRedis.hasKey(multiKey))
            return opsForValue.get(multiKey);
        else return null;
    }

    public int getIntParamFromCache(String group, String code, int defaultValue) {
        final ValueOperations<String, Param> opsForValue = paramRedis.opsForValue();
        String multiKey = group.trim().toUpperCase() + Constant.DASH + code.trim().toUpperCase();
        if (paramRedis.hasKey(multiKey)) {
            Param param = opsForValue.get(multiKey);
            if (param.getStatus() == Constant.STATUS_OBJECT.ACTIVE)
                try {
                    return Integer.valueOf(param.getValue());
                } catch (Exception e) {
                    return defaultValue;
                }
        }
        return defaultValue;
    }

    public long getLongParamFromCache(String group, String code, long defaultValue) {
        final ValueOperations<String, Param> opsForValue = paramRedis.opsForValue();
        String multiKey = group.trim().toUpperCase() + Constant.DASH + code.trim().toUpperCase();
        if (paramRedis.hasKey(multiKey)) {
            Param param = opsForValue.get(multiKey);
            if (param.getStatus() == Constant.STATUS_OBJECT.ACTIVE)
                try {
                    return Long.valueOf(param.getValue());
                } catch (Exception e) {
                    return defaultValue;
                }
        }
        return defaultValue;
    }

    public void addUser2RedisCache(User user) {
        final ValueOperations<String, User> opsForValue = userRedis.opsForValue();
        String key = Constant.TABLE.USERS + Constant.DASH + user.getUsername().trim().toLowerCase();
        opsForValue.set(key, user, 365, TimeUnit.DAYS);
    }

    public User getUserFromCache(String username) {
        final ValueOperations<String, User> opsForValue = userRedis.opsForValue();
        String key = Constant.TABLE.USERS + Constant.DASH + username.trim().toLowerCase();
        if (userRedis.hasKey(key))
            return opsForValue.get(key);
        else return null;
    }

    public void addToken2RedisCache(Token token) {
        final ValueOperations<String, Token> opsForValue = tokenRedis.opsForValue();
        opsForValue.set(token.getAccessToken(), token, token.getExpiredAt().getTime() - token.getCreatedAt().getTime(), TimeUnit.MILLISECONDS);
    }

    public void removeToken2RedisCache(Token token) {
        final ValueOperations<String, Token> opsForValue = tokenRedis.opsForValue();
        opsForValue.set(token.getAccessToken(), token, 1, TimeUnit.MILLISECONDS);
    }

    public Token getTokenFromCache(String accessToken) {
        final ValueOperations<String, Token> opsForValue = tokenRedis.opsForValue();
        if (tokenRedis.hasKey(accessToken))
            return opsForValue.get(accessToken);
        else return null;
    }

    public User getUserFromAccessToken(String accessToken) {
        final ValueOperations<String, Token> opsForValue = tokenRedis.opsForValue();
        if (tokenRedis.hasKey(accessToken)) {
            Token token = opsForValue.get(accessToken);
            if (token != null && !StringCommon.isNullOrBlank(token.getUsername()))
                return getUserFromCache(token.getUsername());
        }
        return null;
    }

    public String getHeaderValue(String headerName) {
        try {
            return request.getHeader(headerName);
        } catch (Exception e) {
            return null;
        }
    }

    public String getUsernameFromHeader() {
        return getHeaderValue(Constant.KEY.USERNAME);
    }

    public void addGroupUser2RedisCache(GroupUser groupUser) {
        final ValueOperations<String, GroupUser> opsForValue = groupUserRedis.opsForValue();
        opsForValue.set(Constant.TABLE.GROUP_USER + Constant.DASH + groupUser.getId(), groupUser, 365, TimeUnit.DAYS);
    }

    public void removeGroupUser2RedisCache(GroupUser groupUser) {
        final ValueOperations<String, GroupUser> opsForValue = groupUserRedis.opsForValue();
        opsForValue.set(Constant.TABLE.GROUP_USER + Constant.DASH + groupUser.getId(), groupUser, 1, TimeUnit.MILLISECONDS);
    }

    public GroupUser getGroupUserFromCache(long id) {
        final ValueOperations<String, GroupUser> opsForValue = groupUserRedis.opsForValue();
        if (tokenRedis.hasKey(Constant.TABLE.GROUP_USER + Constant.DASH + id))
            return opsForValue.get(Constant.TABLE.GROUP_USER + Constant.DASH + id);
        else return null;
    }

    public void addRole2RedisCache(Role role) {
        final ValueOperations<String, Role> opsForValue = roleRedis.opsForValue();
        String key = Constant.TABLE.ROLE + Constant.DASH + role.getId();
        opsForValue.set(key, role, 365, TimeUnit.DAYS);

        final ValueOperations<String, Role> opsForValueUri = roleUriRedis.opsForValue();
        String keyUri = Constant.TABLE.ROLE + Constant.DASH + role.getUrl();
        opsForValueUri.set(keyUri, role, 365, TimeUnit.DAYS);
    }

    public void removeRole2RedisCache(Role role) {
        final ValueOperations<String, Role> opsForValue = roleRedis.opsForValue();
        String key = Constant.TABLE.ROLE + Constant.DASH + role.getId();
        opsForValue.set(key, role, 1, TimeUnit.MILLISECONDS);

        final ValueOperations<String, Role> opsForValueUri = roleUriRedis.opsForValue();
        String keyUri = Constant.TABLE.ROLE + Constant.DASH + role.getUrl();
        opsForValueUri.set(keyUri, role, 1, TimeUnit.MILLISECONDS);
    }

    public Role getRoleFromCache(long id) {
        final ValueOperations<String, Role> opsForValue = roleRedis.opsForValue();
        if (userRedis.hasKey(Constant.TABLE.ROLE + Constant.DASH + id))
            return opsForValue.get(Constant.TABLE.ROLE + Constant.DASH + id);
        else return null;
    }

    public void addUserRole2RedisCache(UserRole userRole) {
        final ValueOperations<String, UserRole> opsForValue = userRoleRedis.opsForValue();
        String key = Constant.TABLE.USERS + userRole.getUserId() + Constant.DASH + Constant.TABLE.ROLE + userRole.getRoleId();
        opsForValue.set(key, userRole, 365, TimeUnit.DAYS);
    }

    public void removeUserRole2RedisCache(UserRole userRole) {
        final ValueOperations<String, UserRole> opsForValue = userRoleRedis.opsForValue();
        String key = Constant.TABLE.USERS + userRole.getUserId() + Constant.DASH + Constant.TABLE.ROLE + userRole.getRoleId();
        opsForValue.set(key, userRole, 1, TimeUnit.MILLISECONDS);
    }

    public UserRole getUserRoleFromCache(long userId, long roleId) {
        final ValueOperations<String, UserRole> opsForValue = userRoleRedis.opsForValue();
        String key = Constant.TABLE.USERS + userId + Constant.DASH + Constant.TABLE.ROLE + roleId;
        if (userRedis.hasKey(key))
            return opsForValue.get(key);
        else return null;
    }
}
