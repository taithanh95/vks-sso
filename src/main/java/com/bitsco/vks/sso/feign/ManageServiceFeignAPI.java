package com.bitsco.vks.sso.feign;

import com.bitsco.vks.common.constant.Constant;
import com.bitsco.vks.common.response.ResponseBody;
import com.bitsco.vks.sso.entities.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(Constant.FEIGN_CLIENT.MANAGE)
public interface ManageServiceFeignAPI {
    @PostMapping("/spp/findFirstByUsername/")
    ResponseBody findFirstByUsername(@RequestBody User user);
}
