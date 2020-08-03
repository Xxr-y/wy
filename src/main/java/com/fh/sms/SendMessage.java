package com.fh.sms;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.fh.common.ServerResponse;
import com.fh.util.MessageVerifyUtils;
import com.fh.util.RedisUtil;
import com.fh.util.SystemConstant;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("sms")
public class SendMessage {

    @RequestMapping("SendMessage")
    public ServerResponse SendMessage(String phone){
        String code = MessageVerifyUtils.getNewcode();
        try {
            SendSmsResponse sendSms = MessageVerifyUtils.sendSms(phone, code);
            if(sendSms != null && "OK".equals(sendSms.getCode())){
                //把code放到redis中
                RedisUtil.setEx(phone,code, SystemConstant.REDIS_EXPIRY_TIME);
                return ServerResponse.success();
            }
        } catch (ClientException e) {
            e.printStackTrace();
            return ServerResponse.error(e.getErrMsg());
        }
        return null;
    }

}
