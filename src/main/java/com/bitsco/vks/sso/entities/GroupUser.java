package com.bitsco.vks.sso.entities;

import com.bitsco.vks.common.constant.Constant;
import com.bitsco.vks.common.util.StringCommon;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Getter
@Setter
@Table(name = Constant.TABLE.GROUP_USER)
public class GroupUser extends BaseEntity {
    @Column(name = "N_ID")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = Constant.SEQUENCE.SQ_GROUP_USER)
    @SequenceGenerator(name = Constant.SEQUENCE.SQ_GROUP_USER, sequenceName = Constant.SEQUENCE.SQ_GROUP_USER, allocationSize = 1)
    private Long id;
    @Column(name = "s_name")
    private String name;
    @Column(name = "s_description")
    private String description;
    @Column(name = "n_status")
    private Integer status;
    @Transient
    private List<GroupUserGroupRole> groupUserGroupRoleList;
    @Transient
    private List<GroupUserRole> groupUserRoleList;

    public GroupUser copyFrom(GroupUser groupUser) {
        if (!StringCommon.isNullOrBlank(groupUser.getName())) this.setName(groupUser.getName());
        if (!StringCommon.isNullOrBlank(groupUser.getDescription())) this.setDescription(groupUser.getDescription());
        if (groupUser.getStatus() != null) this.setStatus(groupUser.getStatus());
        return this;
    }
}
