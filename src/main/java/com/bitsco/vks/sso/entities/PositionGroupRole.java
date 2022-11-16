package com.bitsco.vks.sso.entities;

import com.bitsco.vks.common.constant.Constant;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = Constant.TABLE.POSITION_GROUP_ROLE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PositionGroupRole extends BaseEntity {
    @Column(name = "N_ID")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = Constant.SEQUENCE.SQ_POSITION_GROUP_ROLE)
    @SequenceGenerator(name = Constant.SEQUENCE.SQ_POSITION_GROUP_ROLE, sequenceName = Constant.SEQUENCE.SQ_POSITION_GROUP_ROLE, allocationSize = 1)
    private Long id;
    @Column(name = "N_SUPPLIER_ID")
    private Long supplierId;
    @Column(name = "N_POSITION_ID")
    private Long positionId;
    @Column(name = "N_GROUP_ROLE_ID")
    private Long groupRoleId;

    public PositionGroupRole coppyFrom(PositionGroupRole positionGroupRole) {
        if (positionGroupRole.getStatus() != null) this.setStatus(positionGroupRole.getStatus());
        return this;
    }
}
