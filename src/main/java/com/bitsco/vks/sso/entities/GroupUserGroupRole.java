package com.bitsco.vks.sso.entities;

import com.bitsco.vks.common.constant.Constant;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = Constant.TABLE.GROUP_USER_GROUP_ROLE)
public class GroupUserGroupRole extends BaseEntity {
    @Column(name = "N_ID")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = Constant.SEQUENCE.SQ_GROUP_USER_GROUP_ROLE)
    @SequenceGenerator(name = Constant.SEQUENCE.SQ_GROUP_USER_GROUP_ROLE, sequenceName = Constant.SEQUENCE.SQ_GROUP_USER_GROUP_ROLE, allocationSize = 1)
    private Long id;
    @Column(name = "n_group_role_id")
    private Long groupRoleId;
    @Column(name = "n_group_user_id")
    private Long groupUserId;
    @Column(name = "n_status")
    private Integer status;

    public GroupUserGroupRole(Long groupUserId, Long groupRoleId, Integer status) {
        this.groupUserId = groupUserId;
        this.groupRoleId = groupRoleId;
        this.status = status;
    }
}

