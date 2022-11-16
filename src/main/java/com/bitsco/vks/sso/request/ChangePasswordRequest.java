package com.bitsco.vks.sso.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ChangePasswordRequest {

    @NotBlank
    private String passwordOld;

    @NotBlank
    private String passwordNew;

    @JsonIgnore
    private String passwordOldDecode;

    @JsonIgnore
    private String passwordNewDecode;
}
