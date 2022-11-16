package com.bitsco.vks.sso.entities;

import com.bitsco.vks.common.constant.Constant;
import com.bitsco.vks.common.util.StringCommon;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.persistence.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: truongnq
 * @date: 26-Dec-18 2:52 PM
 * To change this template use File | Settings | File Templates.
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Getter
@Setter
@Table(name = Constant.TABLE.GROUP_ROLE)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupRole extends BaseEntity {
    @Column(name = "N_ID")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = Constant.SEQUENCE.SQ_GROUP_ROLE)
    @SequenceGenerator(name = Constant.SEQUENCE.SQ_GROUP_ROLE, sequenceName = Constant.SEQUENCE.SQ_GROUP_ROLE, allocationSize = 1)
    private Long id;
    @Transient
    List<Role> children;
    @Column(name = "s_app_code")
    private String appCode;
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
    private Boolean checked;

    public GroupRole coppyFrom(GroupRole groupRole) {
        if (!StringCommon.isNullOrBlank(groupRole.getName())) this.setName(groupRole.getName());
        if (!StringCommon.isNullOrBlank(groupRole.getDescription())) this.setDescription(groupRole.getDescription());
        if (!StringCommon.isNullOrBlank(groupRole.getUrl())) this.setUrl(groupRole.getUrl());
        if (!StringCommon.isNullOrBlank(groupRole.getIcon())) this.setIcon(groupRole.getIcon());
        if (groupRole.getType() != null) this.setType(groupRole.getType());
        if (groupRole.getStatus() != null) this.setStatus(groupRole.getStatus());
        if (groupRole.getPriority() != null) this.setPriority(groupRole.getPriority());
        return this;
    }

    public GroupRole(Long id, String name, String url, Integer status) {
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
            this.url = url.toLowerCase().trim();
    }

    public GroupRole(Long id, String name, String url, Integer status, Integer priority) {
        this.setId(id);
        this.name = name;
        this.url = url;
        this.setStatus(status);
        this.priority = priority;
    }
}
