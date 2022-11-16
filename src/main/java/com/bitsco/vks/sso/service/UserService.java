package com.bitsco.vks.sso.service;

import com.bitsco.vks.common.response.Response;
import com.bitsco.vks.sso.entities.User;

import java.util.List;

public interface UserService {
    User save(User user) throws Exception;

    User update(User user) throws Exception;

    Response delete(User user) throws Exception;

    User findById(Long id) throws Exception;

    List<User> getList(User user) throws Exception;

    User findFirstByUsername(String username) throws Exception;

    User findFirstByUsernameAndEmail(String username, String email) throws Exception;
}
