package com.bitsco.vks.sso.feign;

import com.bitsco.vks.common.constant.Constant;
import com.bitsco.vks.common.response.ResponseBody;
import com.bitsco.vks.sso.entities.Email;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(Constant.FEIGN_CLIENT.NOTIFICATION)
public interface NotificationServiceFeignAPI {
    @GetMapping(value = "ping")
    String ping();

    @PostMapping("/email/send/")
    ResponseBody send(@RequestBody Email email);

    @PostMapping("/email/create/")
    ResponseBody create(@RequestBody Email email);
}
