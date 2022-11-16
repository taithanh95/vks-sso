package com.bitsco.vks.sso.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupRoleIdAndStatus {
    private long groupRoleId;
    private int roleStatus;
}
