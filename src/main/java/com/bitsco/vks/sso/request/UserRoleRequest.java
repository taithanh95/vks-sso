package com.bitsco.vks.sso.request;

import com.bitsco.vks.sso.model.TreeviewItem;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRoleRequest {
    private Long userId;
    private List<TreeviewItem> items;
}
