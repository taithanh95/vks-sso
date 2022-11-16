package com.bitsco.vks.sso.entities;

import com.bitsco.vks.common.constant.Constant;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: truongnq
 * @date: 26-Dec-18 2:53 PM
 * To change this template use File | Settings | File Templates.
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Getter
@Setter
@Table(name = Constant.TABLE.GROUP_ROLE_MAP)
public class GroupRoleMap extends BaseEntity {
    @Column(name = "N_ID")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = Constant.SEQUENCE.SQ_GROUP_ROLE_MAP)
    @SequenceGenerator(name = Constant.SEQUENCE.SQ_GROUP_ROLE_MAP, sequenceName = Constant.SEQUENCE.SQ_GROUP_ROLE_MAP, allocationSize = 1)
    private Long id;
    @Column(name = "n_group_role_id")
    private Long groupRoleId;
    @Column(name = "n_role_id")
    private Long roleId;
    @Column(name = "n_priority")
    private Integer priority;
}
