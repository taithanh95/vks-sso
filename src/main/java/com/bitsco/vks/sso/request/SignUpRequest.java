package com.bitsco.vks.sso.request;

import com.bitsco.vks.common.constant.Constant;
import com.bitsco.vks.common.util.StringCommon;
import com.bitsco.vks.sso.entities.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class SignUpRequest {
    private String name;
    @NotBlank
    private String username;

    private String password;

    @Email
    private String email;

    private String phone;

    private String address;

    @JsonIgnore
    public User getUser() {
        User user = new User();
        user.setName(this.getName());
        user.setUsername(this.getUsername());
        user.setEmail(this.getEmail());
        user.setPhone(this.getPhone());
        user.setPassword(this.getPassword());
        user.setAddress(this.getAddress());
        user.setStatus(Constant.USER.STATUS.WAIT_FOR_CONFIRM_EMAIL);
        return user;
    }

    public void setUsername(String username) {
        if (!StringCommon.isNullOrBlank(username)) username = username.trim().toLowerCase();
        this.username = username;
    }
}
