package com.bitsco.vks.sso.thread;

import com.bitsco.vks.common.constant.Constant;
import com.bitsco.vks.common.crypt.Base64;
import com.bitsco.vks.common.response.ResponseBody;
import com.bitsco.vks.common.util.JsonCommon;
import com.bitsco.vks.sso.entities.User;
import com.bitsco.vks.sso.feign.NotificationServiceFeignAPI;
import com.bitsco.vks.sso.service.FreemarkerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class SendEmailThread {
    private static final Logger LOGGER = LogManager.getLogger(Constant.LOG_APPENDER.THREAD);

    @Autowired
    FreemarkerService freemarkerService;

    @Autowired
    NotificationServiceFeignAPI notificationServiceFeignAPI;

    ExecutorService executorService;

    @PostConstruct
    public void init() {
        int numThreads = 100;
        executorService = Executors.newFixedThreadPool(numThreads);
    }

    public void sendLoginSuccess(String name, String username, String password, String toAddress) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                long id = System.currentTimeMillis();
                ResponseBody responseBody = null;
                try {
                    responseBody = notificationServiceFeignAPI.create(freemarkerService.sendLoginSuccess(name, username, Base64.decodeBase64(password), toAddress));
                } catch (Exception e) {
                    LOGGER.info("[Exception][" + id + "] when sendLoginSuccess ", e);
                } finally {
                    LOGGER.info("[" + username + "][Duration = " + (System.currentTimeMillis() - id) + "] sendLoginSuccess responseBody = " + JsonCommon.objectToJsonNotNull(responseBody));
                }
            }
        });
    }

    public void sendSignupSuccess(User user, String token) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                long id = System.currentTimeMillis();
                ResponseBody responseBody = null;
                try {
                    responseBody = notificationServiceFeignAPI.create(freemarkerService.sendSignupSuccess(user, token));
                } catch (Exception e) {
                    LOGGER.info("[Exception][" + id + "] when sendSignupSuccess ", e);
                } finally {
                    LOGGER.info("[" + user.getUsername() + "][Duration = " + (System.currentTimeMillis() - id) + "] sendSignupSuccess responseBody = " + JsonCommon.objectToJsonNotNull(responseBody));
                }
            }
        });
    }

    public void sendVerifyEmailSuccess(User user) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                long id = System.currentTimeMillis();
                ResponseBody responseBody = null;
                try {
                    responseBody = notificationServiceFeignAPI.create(freemarkerService.sendVerifyEmailSuccess(user));
                } catch (Exception e) {
                    LOGGER.info("[Exception][" + id + "] when sendVerifyEmailSuccess ", e);
                } finally {
                    LOGGER.info("[" + user.getUsername() + "][Duration = " + (System.currentTimeMillis() - id) + "] sendVerifyEmailSuccess responseBody = " + JsonCommon.objectToJsonNotNull(responseBody));
                }
            }
        });
    }

    public void sendEmailResetPasswordByAdmin(String username, String password, String toAddress) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                long id = System.currentTimeMillis();
                ResponseBody responseBody = null;
                try {
                    responseBody = notificationServiceFeignAPI.create(freemarkerService.sendEmailResetPasswordByAdmin(username, password, toAddress));
                } catch (Exception e) {
                    LOGGER.info("[Exception][" + id + "] when sendEmailResetPasswordByAdmin ", e);
                } finally {
                    LOGGER.info("[" + username + "][Duration = " + (System.currentTimeMillis() - id) + "] sendEmailResetPasswordByAdmin responseBody = " + JsonCommon.objectToJsonNotNull(responseBody));
                }
            }
        });
    }

    public void sendEmailResetPasswordByUserSuccess(String username, String password, String toAddress) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                long id = System.currentTimeMillis();
                ResponseBody responseBody = null;
                try {
                    responseBody = notificationServiceFeignAPI.create(freemarkerService.sendEmailResetPasswordByUserSuccess(username, password, toAddress));
                } catch (Exception e) {
                    LOGGER.info("[Exception][" + id + "] when sendEmailResetPasswordByUserSuccess ", e);
                } finally {
                    LOGGER.info("[" + username + "][Duration = " + (System.currentTimeMillis() - id) + "] sendEmailResetPasswordByUserSuccess responseBody = " + JsonCommon.objectToJsonNotNull(responseBody));
                }
            }
        });
    }

    public void sendEmailChangePasswordByUserSuccess(String username, String passwordOld, String passwordNew, String toAddress) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                long id = System.currentTimeMillis();
                ResponseBody responseBody = null;
                try {
                    responseBody = notificationServiceFeignAPI.create(freemarkerService.sendEmailChangePasswordByUserSuccess(username, Base64.decodeBase64(passwordOld), Base64.decodeBase64(passwordNew), toAddress));
                } catch (Exception e) {
                    LOGGER.info("[Exception][" + id + "] when sendEmailChangePasswordByUserSuccess ", e);
                } finally {
                    LOGGER.info("[" + username + "][Duration = " + (System.currentTimeMillis() - id) + "] sendEmailChangePasswordByUserSuccess responseBody = " + JsonCommon.objectToJsonNotNull(responseBody));
                }
            }
        });
    }
}
