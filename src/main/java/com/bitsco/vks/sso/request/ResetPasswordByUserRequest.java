package com.bitsco.vks.sso.request;

import com.bitsco.vks.common.util.StringCommon;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ResetPasswordByUserRequest {
    @Email
    @NotBlank
    private String email;

    private String otp;

    private String password;

    public void setEmail(String email) {
        if (!StringCommon.isNullOrBlank(email)) email = email.trim().toLowerCase();
        this.email = email;
    }
}
