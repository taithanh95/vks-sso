package com.bitsco.vks.sso.entities;

import com.bitsco.vks.common.constant.Constant;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: truongnq
 * @date: 26-Dec-18 2:55 PM
 * To change this template use File | Settings | File Templates.
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = Constant.TABLE.USER_GROUP_ROLE)
public class UserGroupRole extends BaseEntity {
    @Column(name = "N_ID")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = Constant.SEQUENCE.SQ_USER_GROUP_ROLE)
    @SequenceGenerator(name = Constant.SEQUENCE.SQ_USER_GROUP_ROLE, sequenceName = Constant.SEQUENCE.SQ_USER_GROUP_ROLE, allocationSize = 1)
    private Long id;
    @Column(name = "n_group_role_id")
    private Long groupRoleId;
    @Column(name = "n_user_id")
    private Long userId;
    @Column(name = "n_priority")
    private Integer priority;
    @Transient
    private String appCode;


    public UserGroupRole(Long groupRoleId, Long userId, Integer status) {
        this.groupRoleId = groupRoleId;
        this.userId = userId;
        this.setStatus(status);
    }
}
