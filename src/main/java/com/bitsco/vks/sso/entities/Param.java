package com.bitsco.vks.sso.entities;

import com.bitsco.vks.common.constant.Constant;
import com.bitsco.vks.common.util.StringCommon;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Getter
@Setter
@Table(name = Constant.TABLE.PARAM)
public class Param extends BaseEntity {
    @Column(name = "N_ID")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = Constant.SEQUENCE.SQ_PARAM)
    @SequenceGenerator(name = Constant.SEQUENCE.SQ_PARAM, sequenceName = Constant.SEQUENCE.SQ_PARAM, allocationSize = 1)
    private Long id;
    @Column(name = "s_group", nullable = false)
    private String group;
    @Column(name = "s_code", nullable = false)
    private String code;
    @Column(name = "s_value", length = 3000, nullable = false)
    private String value;
    @Column(name = "s_name")
    private String name;
    @Column(name = "n_type")
    private Integer type;

    public void setGroup(String group) {
        if (!StringCommon.isNullOrBlank(group)) group = group.trim().toUpperCase();
        this.group = group;
    }

    public void setCode(String code) {
        if (!StringCommon.isNullOrBlank(code)) code = code.trim().toUpperCase();
        this.code = code;
    }

    public Param coppyFrom(Param param) {
        if (!StringCommon.isNullOrBlank(param.getValue())) this.setValue(param.getValue());
        if (!StringCommon.isNullOrBlank(param.getName())) this.setName(param.getName());
        if (param.getType() != null) this.setType(param.getType());
        if (param.getStatus() != null) this.setStatus(param.getStatus());
        return this;
    }
}
