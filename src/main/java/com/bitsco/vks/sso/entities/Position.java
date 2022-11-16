package com.bitsco.vks.sso.entities;

import com.bitsco.vks.common.constant.Constant;
import com.bitsco.vks.common.util.StringCommon;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = Constant.TABLE.POSITION)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Position extends BaseEntity {
    @Column(name = "N_ID")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = Constant.SEQUENCE.SQ_POSITION)
    @SequenceGenerator(name = Constant.SEQUENCE.SQ_POSITION, sequenceName = Constant.SEQUENCE.SQ_POSITION, allocationSize = 1)
    private Long id;

    @Column(name = "N_SUPPLIER_ID")
    private Long supplierId;

    @Column(name = "N_USER_TYPE")
    private Integer userType;

    @Column(name = "S_NAME")
    private String name;

    @Transient
    private String userTypeName;

    public Position coppyFrom(Position position) {
        if (position.getSupplierId() != null) this.setSupplierId(position.getSupplierId());
        if (position.getUserType() != null) this.setUserType(position.getUserType());
        if (!StringCommon.isNullOrBlank(position.getName())) this.setName(position.getName().trim());
        if (position.getStatus() != null) this.setStatus(position.getStatus());
        return this;
    }
}
