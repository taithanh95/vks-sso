package com.bitsco.vks.sso.service;

import com.bitsco.vks.common.constant.Constant;
import com.bitsco.vks.common.exception.CommonException;
import com.bitsco.vks.common.model.Otp;
import com.bitsco.vks.common.response.Response;
import com.bitsco.vks.common.util.DateCommon;
import com.bitsco.vks.common.validate.ValidateCommon;
import com.bitsco.vks.sso.entities.Email;
import com.bitsco.vks.sso.entities.User;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.MailPreparationException;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class FreemarkerService {

    @Autowired
    @Qualifier("freemarkerConfig")
    private Configuration freemarkerConfig;

    public String getContentFromFreemarkerTemplate(Map<String, Object> templateTokens, String templateName) throws Exception {
        freemarkerConfig.setClassForTemplateLoading(FreemarkerService.class, "/");
        Template htmlTemplate = freemarkerConfig.getTemplate("/template/email/" + templateName);
        final StringWriter htmlWriter = new StringWriter();
        try {
            htmlTemplate.process(templateTokens, htmlWriter);
        } catch (TemplateException e) {
            throw new MailPreparationException("Không thể tạo nội dung email ", e);
        }
        return htmlWriter.toString();
    }

    public Email createEmailByFreemarker(String toAddress, String subject, Map<String, Object> templateTokens, String templateName, int type, Date intendAt) throws Exception {
        ValidateCommon.validateNullObject(toAddress, "toAddress");
        toAddress = toAddress.trim().toLowerCase();
        ValidateCommon.validateNullObject(subject, "subject");
        ValidateCommon.validateNullObject(templateName, "templateName");
        templateName = templateName.trim().toLowerCase();
        if (!templateName.endsWith(".ftl"))
            throw new CommonException(Response.DATA_INVALID, "File mẫu phải có định dạng .ftl");
        Email email = new Email();
        email.setToAddress(toAddress);
        email.setSubject("[" + DateCommon.convertDateTimeToString(new Date()) + "] " + subject);
        email.setContent(getContentFromFreemarkerTemplate(templateTokens, templateName));
        email.setStatus(Constant.EMAIL.STATUS.NEW);
        email.setIntendAt(intendAt);
        return email;
    }

    public Email sendLoginSuccess(String name, String username, String password, String toAddress) throws Exception {
        Map<String, Object> templateTokens = new HashMap<>();
        Date currentDate = new Date();
        templateTokens.put("NAME", name);
        templateTokens.put(TemplateEmail.LOGIN.USERNAME, username);
        templateTokens.put(TemplateEmail.LOGIN.PASSWORD, password);
        templateTokens.put(TemplateEmail.LOGIN.DATE_TIME, DateCommon.convertDateToStringByPattern(currentDate, Constant.DATE.FORMAT.DATE_TIME));
        return createEmailByFreemarker(toAddress, TemplateEmail.LOGIN.SUBJECT, templateTokens, TemplateEmail.LOGIN.TEMPLATE_NAME, Constant.EMAIL.TYPE.NOTIFICATION, currentDate);
    }

    public Email sendSignupSuccess(User user, String token) throws Exception {
        Map<String, Object> templateTokens = new HashMap<>();
        Date currentDate = new Date();
        templateTokens.put(TemplateEmail.SIGNUP.TOKEN, token);
        templateTokens.put(TemplateEmail.SIGNUP.USERNAME, user.getUsername());
        templateTokens.put(TemplateEmail.SIGNUP.PASSWORD, user.getPasswordDecode());
        templateTokens.put(TemplateEmail.SIGNUP.FIRST_NAME, user.getName());
        templateTokens.put(TemplateEmail.SIGNUP.LAST_NAME, user.getName());
        templateTokens.put(TemplateEmail.SIGNUP.EMAIL, user.getEmail());
        templateTokens.put(TemplateEmail.SIGNUP.ADDRESS, user.getAddress() == null ? "" : user.getAddress());
        templateTokens.put(TemplateEmail.SIGNUP.PHONE, user.getPhone());
        templateTokens.put(TemplateEmail.SIGNUP.DATE_TIME, DateCommon.convertDateToStringByPattern(currentDate, Constant.DATE.FORMAT.DATE_TIME));
        return createEmailByFreemarker(user.getEmail(), TemplateEmail.SIGNUP.SUBJECT, templateTokens, TemplateEmail.SIGNUP.TEMPLATE_NAME, Constant.EMAIL.TYPE.CONFIRM, currentDate);
    }

    public Email sendVerifyEmailSuccess(User user) throws Exception {
        Map<String, Object> templateTokens = new HashMap<>();
        Date currentDate = new Date();
        templateTokens.put(TemplateEmail.VERIFY_EMAIL.USERNAME, user.getUsername());
        templateTokens.put(TemplateEmail.VERIFY_EMAIL.FIRST_NAME, user.getName());
        templateTokens.put(TemplateEmail.VERIFY_EMAIL.LAST_NAME, user.getName());
        templateTokens.put(TemplateEmail.VERIFY_EMAIL.EMAIL, user.getEmail());
        templateTokens.put(TemplateEmail.VERIFY_EMAIL.ADDRESS, user.getAddress() == null ? "" : user.getAddress());
        templateTokens.put(TemplateEmail.VERIFY_EMAIL.PHONE, user.getPhone());
        templateTokens.put(TemplateEmail.VERIFY_EMAIL.DATE_TIME, DateCommon.convertDateToStringByPattern(currentDate, Constant.DATE.FORMAT.DATE_TIME));
        return createEmailByFreemarker(user.getEmail(), TemplateEmail.VERIFY_EMAIL.SUBJECT, templateTokens, TemplateEmail.VERIFY_EMAIL.TEMPLATE_NAME, Constant.EMAIL.TYPE.NOTIFICATION, currentDate);
    }

    public Email sendEmailResetPasswordByAdmin(String username, String password, String toAddress) throws Exception {
        Map<String, Object> templateTokens = new HashMap<>();
        Date currentDate = new Date();
        templateTokens.put(TemplateEmail.RESET_PASSWORD_BY_ADMIN.USERNAME, username);
        templateTokens.put(TemplateEmail.RESET_PASSWORD_BY_ADMIN.PASSWORD, password);
        templateTokens.put(TemplateEmail.RESET_PASSWORD_BY_ADMIN.DATE_TIME, DateCommon.convertDateToStringByPattern(currentDate, Constant.DATE.FORMAT.DATE_TIME));
        return createEmailByFreemarker(toAddress, TemplateEmail.RESET_PASSWORD_BY_ADMIN.SUBJECT, templateTokens, TemplateEmail.RESET_PASSWORD_BY_ADMIN.TEMPLATE_NAME, Constant.EMAIL.TYPE.NOTIFICATION, currentDate);
    }

    public Email sendOtpResetPasswordByUser(String username, Otp otp, String toAddress) throws Exception {
        Map<String, Object> templateTokens = new HashMap<>();
        Date currentDate = new Date();
        templateTokens.put(TemplateEmail.OTP_RESET_PASSWORD_BY_USER.USERNAME, username);
        templateTokens.put(TemplateEmail.OTP_RESET_PASSWORD_BY_USER.OTP, otp.getCode());
        templateTokens.put(TemplateEmail.OTP_RESET_PASSWORD_BY_USER.EXPIRY_TIME, DateCommon.convertDateToStringByPattern(otp.getExpiryTime(), Constant.DATE.FORMAT.DATE_TIME));
        templateTokens.put(TemplateEmail.OTP_RESET_PASSWORD_BY_USER.DATE_TIME, DateCommon.convertDateToStringByPattern(currentDate, Constant.DATE.FORMAT.DATE_TIME));
        return createEmailByFreemarker(toAddress, TemplateEmail.OTP_RESET_PASSWORD_BY_USER.SUBJECT, templateTokens, TemplateEmail.OTP_RESET_PASSWORD_BY_USER.TEMPLATE_NAME, Constant.EMAIL.TYPE.CONFIRM, currentDate);
    }

    public Email sendEmailResetPasswordByUserSuccess(String username, String password, String toAddress) throws Exception {
        Map<String, Object> templateTokens = new HashMap<>();
        Date currentDate = new Date();
        templateTokens.put(TemplateEmail.RESET_PASSWORD_BY_USER_SUCCESS.USERNAME, username);
        templateTokens.put(TemplateEmail.RESET_PASSWORD_BY_USER_SUCCESS.PASSWORD, password);
        templateTokens.put(TemplateEmail.RESET_PASSWORD_BY_USER_SUCCESS.DATE_TIME, DateCommon.convertDateToStringByPattern(currentDate, Constant.DATE.FORMAT.DATE_TIME));
        return createEmailByFreemarker(toAddress, TemplateEmail.RESET_PASSWORD_BY_USER_SUCCESS.SUBJECT, templateTokens, TemplateEmail.RESET_PASSWORD_BY_USER_SUCCESS.TEMPLATE_NAME, Constant.EMAIL.TYPE.NOTIFICATION, currentDate);
    }

    public Email sendEmailChangePasswordByUserSuccess(String username, String passwordOld, String passwordNew, String toAddress) throws Exception {
        Map<String, Object> templateTokens = new HashMap<>();
        Date currentDate = new Date();
        templateTokens.put(TemplateEmail.CHANGE_PASSWORD_BY_USER_SUCCESS.USERNAME, username);
        templateTokens.put(TemplateEmail.CHANGE_PASSWORD_BY_USER_SUCCESS.PASSWORD_OLD, passwordOld);
        templateTokens.put(TemplateEmail.CHANGE_PASSWORD_BY_USER_SUCCESS.PASSWORD_NEW, passwordNew);
        templateTokens.put(TemplateEmail.CHANGE_PASSWORD_BY_USER_SUCCESS.DATE_TIME, DateCommon.convertDateToStringByPattern(currentDate, Constant.DATE.FORMAT.DATE_TIME));
        return createEmailByFreemarker(toAddress, TemplateEmail.CHANGE_PASSWORD_BY_USER_SUCCESS.SUBJECT, templateTokens, TemplateEmail.CHANGE_PASSWORD_BY_USER_SUCCESS.TEMPLATE_NAME, Constant.EMAIL.TYPE.NOTIFICATION, currentDate);
    }
}
