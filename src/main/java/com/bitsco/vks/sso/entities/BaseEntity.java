package com.bitsco.vks.sso.entities;

import com.bitsco.vks.common.constant.Constant;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseEntity {
    @Column(name = "n_status")
    private Integer status;
    @Column(name = "d_created_at")
    @JsonFormat(pattern = Constant.DATE.FORMAT.DATE_TIME, timezone = "Asia/Ho_Chi_Minh")
    private Date createdAt;
    @Column(name = "d_updated_at")
    @JsonFormat(pattern = Constant.DATE.FORMAT.DATE_TIME, timezone = "Asia/Ho_Chi_Minh")
    private Date updatedAt;

    @Column(name = "s_created_by")
    private String createdBy;
    @Column(name = "s_updated_by")
    private String updatedBy;

    @PrePersist
    public void setCreatedAt() {
        this.createdAt = new Date();
    }

    @PreUpdate
    public void setUpdatedAt() {
        this.updatedAt = new Date();
    }
}
