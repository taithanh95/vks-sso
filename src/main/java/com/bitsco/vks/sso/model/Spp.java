package com.bitsco.vks.sso.model;

import com.bitsco.vks.common.constant.Constant;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Spp {
    private String sppId;
    private String name;
    private String address;
    private String tel;
    private String fax;
    private String director;
    private String status;
    private String spcId;
    private String spcName;
    private String polId;
    private String polName;
    private String locaId;
    private String position;
    private String shortName;
    private String locaName;
    private String sppCode;
    private String sppLevel;
    private String sppParent;
    private String oderCode;
    private String isDePart;
    private String sppIdFOX;
    private String nameSync;
    @JsonFormat(pattern = Constant.DATE.FORMAT.DATE_TIME, timezone = "Asia/Ho_Chi_Minh")
    private Date createdAt;
    @JsonFormat(pattern = Constant.DATE.FORMAT.DATE_TIME, timezone = "Asia/Ho_Chi_Minh")
    private Date updatedAt;
    private String createdBy;
    private String updatedBy;
}
