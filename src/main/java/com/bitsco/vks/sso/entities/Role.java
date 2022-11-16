package com.bitsco.vks.sso.entities;

import com.bitsco.vks.common.constant.Constant;
import com.bitsco.vks.common.util.StringCommon;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Getter
@Setter
@Table(name = Constant.TABLE.ROLE)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Role extends BaseEntity {
    @Column(name = "N_ID")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = Constant.SEQUENCE.SQ_ROLE)
    @SequenceGenerator(name = Constant.SEQUENCE.SQ_ROLE, sequenceName = Constant.SEQUENCE.SQ_ROLE, allocationSize = 1)
    private Long id;
    @Column(name = "n_parent_id")
    private Long parentId;
    @Column(name = "s_name")
    private String name;
    @Column(name = "s_description")
    private String description;
    @Column(name = "s_url")
    private String url;
    @Column(name = "s_icon")
    private String icon;
    @Column(name = "n_type")
    private Integer type;

    @Transient
    private Integer priority;

    @Transient
    List<Role> children;

    @Transient
    private Boolean checked;

    public Role copyFrom(Role role) {
        if (!StringCommon.isNullOrBlank(role.getName())) this.setName(role.getName());
        if (!StringCommon.isNullOrBlank(role.getDescription())) this.setDescription(role.getDescription());
        if (!StringCommon.isNullOrBlank(role.getUrl())) this.setUrl(role.getUrl());
        if (!StringCommon.isNullOrBlank(role.getIcon())) this.setIcon(role.getIcon());
        if (role.getParentId() != null) this.setParentId(role.getParentId());
        if (role.getType() != null) this.setType(role.getType());
        if (role.getStatus() != null) this.setStatus(role.getStatus());
        return this;
    }

    public Role(Long id, String name, String url, Integer status) {
        this.setId(id);
        this.name = name;
        this.url = url;
        this.setStatus(status);
    }

    public void setName(String name) {
        if (!StringCommon.isNullOrBlank(name))
            this.name = name.trim();
    }

    public void setUrl(String url) {
        if (!StringCommon.isNullOrBlank(url))
            this.url = url.trim();
    }

    public Role(Long id, Integer priority) {
        this.setId(id);
        this.priority = priority;
    }

    public Role(Long id, String name, String url, Integer status, Integer priority) {
        this.setId(id);
        this.name = name;
        this.url = url;
        this.setStatus(status);
        this.priority = priority;
    }
}
