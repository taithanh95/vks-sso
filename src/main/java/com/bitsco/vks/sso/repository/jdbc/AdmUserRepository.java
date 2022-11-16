package com.bitsco.vks.sso.repository.jdbc;

import com.bitsco.vks.sso.dto.UserSaveDTO;
import com.bitsco.vks.sso.entities.User;

import java.util.List;

public interface AdmUserRepository {

    List<User> getLst(User req);

    String insOrUpd(UserSaveDTO req, String username);

    String delete(UserSaveDTO req, String username);
}
