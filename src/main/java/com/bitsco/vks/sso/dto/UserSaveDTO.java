package com.bitsco.vks.sso.dto;

import com.bitsco.vks.sso.entities.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Transient;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Nguyen Tai Thanh <taithanh95.dev@gmail.com>
 * Date: 23/05/2022
 * Time: 3:46 PM
 */
@NoArgsConstructor
@Data
public class UserSaveDTO{

    User user;

    String action;

    Date expiredate;

    String inspcode;

    String sppid;

    boolean locked;

    String departid;

    String groupid;
}
