package com.bitsco.vks.sso.entities;

import com.bitsco.vks.common.constant.Constant;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Truong Nguyen
 * Date: 26-Sep-18
 * Time: 4:22 PM
 * To change this template use File | Settings | File Templates.
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = Constant.TABLE.EMAIL)
public class Email extends BaseEntity {
    @Column(name = "N_ID")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = Constant.SEQUENCE.SQ_USERS)
    @SequenceGenerator(name = Constant.SEQUENCE.SQ_USERS, sequenceName = Constant.SEQUENCE.SQ_USERS, allocationSize = 1)
    private Long id;
    @Column(name = "s_from_address")
    private String fromAddress;
    @Column(name = "s_to_address")
    private String toAddress;
    @Column(name = "s_cc_address")
    private String ccAddress;
    @Column(name = "s_subject")
    private String subject;
    @Column(name = "c_content", columnDefinition = "CLOB")
    private String content;
    @Column(name = "s_file_name")
    private String fileName;
    @Column(name = "n_type")
    private Integer type;
    @Column(name = "s_response_message")
    private String responseMessage;
    @Column(name = "d_intend_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.DATE.FORMAT.DATE_TIME, timezone = "Asia/Ho_Chi_Minh")
    private Date intendAt;
    @Column(name = "d_sent_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.DATE.FORMAT.DATE_TIME, timezone = "Asia/Ho_Chi_Minh")
    private Date sentAt;

    public Email(String toAddress, String subject, String content, Integer status, Date intendAt) {
        this.toAddress = toAddress;
        this.subject = subject;
        this.content = content;
        this.setStatus(status);
        this.intendAt = intendAt;
    }
}
