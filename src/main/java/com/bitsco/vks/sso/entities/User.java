package com.bitsco.vks.sso.entities;

import com.bitsco.vks.common.constant.Constant;
import com.bitsco.vks.common.util.StringCommon;
import com.bitsco.vks.sso.model.Spp;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Entity
@Getter
@Setter
@Table(name = Constant.TABLE.USERS)
public class User extends BaseEntity {
    @Column(name = "N_ID")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = Constant.SEQUENCE.SQ_USERS)
    @SequenceGenerator(name = Constant.SEQUENCE.SQ_USERS, sequenceName = Constant.SEQUENCE.SQ_USERS, allocationSize = 1)
    private Long id;
    @Column(name = "s_username")
    private String username;
    @Column(name = "s_password")
    private String password;
    @Column(name = "s_name")
    private String name;
    @Column(name = "s_email")
    private String email;
    @Column(name = "s_phone")
    private String phone;
    @Column(name = "s_address")
    private String address;
    @Column(name = "n_group_user_id")
    private Long groupUserId;
    @Column(name = "n_type")
    private Integer type;
    @Column(name = "n_email_verified")
    private Integer emailVerified;
    @Column(name = "s_image_Url")
    private String imageUrl;
    @Column(name = "d_expiredate")
    private Date expiredate;
    @Column(name = "s_inspcode")
    private String inspcode;
    @Column(name = "s_sppid")
    private String sppid;
    @Column(name = "s_departid")
    private String departid;
    @Column(name = "s_groupid")
    private String groupid;
    @Column(name = "s_sppname")
    private String sppname;
    @Column(name = "s_departname")
    private String departname;
    @Column(name = "s_inspectname")
    private String inspectname;
    @Column(name = "s_groupidname")
    private String groupidname;

    @Transient
    String typeName;

    @Transient
    private GroupUser groupUser;

    @Transient
    @JsonIgnore
    private String passwordDecode;

    public User coppyFrom(User user) {
        if (!StringCommon.isNullOrBlank(user.getName())) this.setName(user.getName());
        if (!StringCommon.isNullOrBlank(user.getAddress())) this.setAddress(user.getAddress());
        if (!StringCommon.isNullOrBlank(user.getPhone())) this.setPhone(user.getPhone());
        if (!StringCommon.isNullOrBlank(user.getEmail())) this.setEmail(user.getEmail());
        if (!StringCommon.isNullOrBlank(user.getInspcode())) this.setInspcode(user.getInspcode());
        if (!StringCommon.isNullOrBlank(user.getSppid())) this.setSppid(user.getSppid());
        if (!StringCommon.isNullOrBlank(user.getDepartid())) this.setDepartid(user.getDepartid());
        if (!StringCommon.isNullOrBlank(user.getPassword())) this.setPassword(user.getPassword());
        if (!StringCommon.isNullOrBlank(user.getGroupid())) this.setGroupid(user.getGroupid());
        if (!StringCommon.isNullOrBlank(user.getSppname())) this.setSppname(user.getSppname());
        if (!StringCommon.isNullOrBlank(user.getDepartname())) this.setDepartname(user.getDepartname());
        if (!StringCommon.isNullOrBlank(user.getInspectname())) this.setInspectname(user.getInspectname());
        if (!StringCommon.isNullOrBlank(user.getGroupidname())) this.setGroupidname(user.getGroupidname());
        if (user.getGroupUserId() != null) this.setGroupUserId(user.getGroupUserId());
        if (user.getType() != null) this.setType(user.getType());
        if (user.getStatus() != null) this.setStatus(user.getStatus());
        if (user.getExpiredate() != null) this.setExpiredate(user.getExpiredate());
        return this;
    }

    @Transient
    private Spp spp;
}
