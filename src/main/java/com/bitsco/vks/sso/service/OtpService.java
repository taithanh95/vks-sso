package com.bitsco.vks.sso.service;

import com.bitsco.vks.common.constant.Constant;
import com.bitsco.vks.common.model.Otp;
import com.bitsco.vks.common.response.Response;
import com.bitsco.vks.common.util.NumberCommon;
import com.bitsco.vks.common.util.StringCommon;
import com.bitsco.vks.sso.cache.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Truong Nguyen
 * Date: 16-Jul-19
 * Time: 11:43 AM
 * To change this template use File | Settings | File Templates.
 */

@Service
public class OtpService {
    @Autowired
    CacheService cacheService;

    public Otp create(Otp otpInput) throws Exception {
        Otp otp = new Otp();
        otp.coppy(otpInput);
        otp.setStatus(Constant.STATUS_OBJECT.ACTIVE);
        String code = "";
        try {
            code = StringCommon.padleft(NumberCommon.getRandomBetweenRange(0, 999999) + "", 6, '0');
        } catch (Exception e) {
            code = NumberCommon.getRandomBetweenRange(100000, 999999) + "";
        }
        otp.setCode(code);
        if (cacheService.getOtpFromCache(code) == null) {
            cacheService.addOtp2RedisCache(otp);
            return otp;
        } else
            return create(otpInput);
    }

    public Response verifyOtp(Otp otpInput) {
        Otp otp = cacheService.getOtpFromCache(otpInput.getCode());
        if (otp == null)
            return Response.OBJECT_NOT_FOUND;
        else if (otp.getStatus() != Constant.STATUS_OBJECT.ACTIVE)
            return Response.OTP_INACTIVE;
        else if (otp.getUserId() != otpInput.getUserId())
            return Response.OTP_INVALID_FOR_USER;
        else if (otp.getTransId() != otpInput.getTransId())
            return Response.OTP_INVALID_FOR_TRANS;
        else if (otp.getType() != otpInput.getType())
            return Response.OTP_INVALID_FOR_TYPE;
        else if (!otp.getPhone().equals(otpInput.getPhone()))
            return Response.OTP_INVALID_FOR_PHONE;
        else if (otp.getExpiryTime().before(new Date()))
            return Response.OTP_EXPIRY;
        else {
            otp.setStatus(Constant.STATUS_OBJECT.INACTIVE);
            cacheService.addOtp2RedisCache(otp);
        }
        return Response.SUCCESS;
    }
}
