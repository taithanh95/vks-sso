package com.bitsco.vks.sso.thread;

import com.bitsco.vks.common.constant.Constant;
import com.bitsco.vks.sso.feign.CategoryServiceFeignAPI;
import com.bitsco.vks.sso.service.RoleService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class GrantRoleThread {
    private static final Logger LOGGER = LogManager.getLogger(Constant.LOG_APPENDER.THREAD);

    @Autowired
    RoleService roleService;

    @Autowired
    CategoryServiceFeignAPI categoryServiceFeignAPI;

    ExecutorService executorService;

    @PostConstruct
    public void init() {
        int numThreads = 100;
        executorService = Executors.newFixedThreadPool(numThreads);
    }

//    public void grantRole(long userId, long supplierId, int type) {
//        executorService.execute(new Runnable() {
//            @Override
//            public void run() {
//                long id = System.currentTimeMillis();
//                ResponseBody responseBody = null;
//                try {
//                    responseBody = categoryServiceFeignAPI.getListGroupRoleId(new Position(supplierId, type));
//                    if (responseBody.getResponseCode().equals(Response.SUCCESS.getResponseCode()) && responseBody.getResponseData() != null) {
//                        List<Long> groupRoleIdList = (new ObjectMapper()).convertValue(responseBody.getResponseData(), new TypeReference<List<Long>>() {
//                        });
//                        if (!ArrayListCommon.isNullOrEmpty(groupRoleIdList))
//                            groupRoleIdList.stream().forEach(x -> {
//                                try {
//                                    roleService.mergeUserGroupRole(new UserGroupRole(x, userId, Constant.STATUS_OBJECT.ACTIVE));
//                                } catch (Exception e) {
//                                    LOGGER.info("[Exception][" + id + "] when grantRole.mergeUserGroupRole ", e);
//                                }
//                            });
//                    }
//                } catch (Exception e) {
//                    LOGGER.info("[Exception][" + id + "] when grantRole ", e);
//                } finally {
//                    LOGGER.info("[" + userId + "][Duration = " + (System.currentTimeMillis() - id) + "] grantRole");
//                }
//            }
//        });
//    }
//
//    public void unGrantRole(long userId, long supplierId, int type) {
//        executorService.execute(new Runnable() {
//            @Override
//            public void run() {
//                long id = System.currentTimeMillis();
//                ResponseBody responseBody = null;
//                try {
//                    responseBody = categoryServiceFeignAPI.getListGroupRoleId(new Position(supplierId, type));
//                    if (responseBody.getResponseCode().equals(Response.SUCCESS.getResponseCode()) && responseBody.getResponseData() != null) {
//                        List<Long> positionGroupRoleList = (new ObjectMapper()).convertValue(responseBody.getResponseData(), new TypeReference<List<Long>>() {
//                        });
//                        if (!ArrayListCommon.isNullOrEmpty(positionGroupRoleList))
//                            positionGroupRoleList.stream().forEach(x -> {
//                                try {
//                                    roleService.mergeUserGroupRole(new UserGroupRole(x, userId, Constant.STATUS_OBJECT.INACTIVE));
//                                } catch (Exception e) {
//                                    LOGGER.info("[Exception][" + id + "] when unGrantRole.mergeUserGroupRole ", e);
//                                }
//                            });
//                    }
//                } catch (Exception e) {
//                    LOGGER.info("[Exception][" + id + "] when unGrantRole ", e);
//                } finally {
//                    LOGGER.info("[" + userId + "][Duration = " + (System.currentTimeMillis() - id) + "] unGrantRole");
//                }
//            }
//        });
//    }

}
