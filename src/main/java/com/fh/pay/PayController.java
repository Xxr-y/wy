package com.fh.pay;

import com.fh.common.ServerResponse;
import com.fh.sdk.MyConfig;
import com.fh.sdk.WXPay;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("pay")
public class PayController {

    @RequestMapping("createNative")
    public ServerResponse createNative(String orderNo, BigDecimal totalPrice){
        try {
            MyConfig config = new MyConfig();
            WXPay wxpay = new WXPay(config);

            Map<String, String> data = new HashMap<String, String>();
            data.put("body", "腾讯充值中心-QQ会员充值"); //支付中的主题
            data.put("out_trade_no", orderNo);  //商户订单号
            data.put("device_info", "WEB");  //设备信息
            data.put("fee_type", "CNY");  //货币单位 : 分
            data.put("total_fee", "1");  //支付金额 : 1分
            //终端ip ,记录ip,可以发现攻击我们的ip并进行 屏蔽
            data.put("spbill_create_ip", "123.12.12.123");
            //重点：回调地址，用来通知支付结果的地址
            data.put("notify_url", "http://www.example.com/wxpay/notify");
            data.put("trade_type", "NATIVE");  // 此处指定为扫码支付
            data.put("product_id", "12");
            // 调用微信接口
            Map<String, String> resp = wxpay.unifiedOrder(data);


            // 判断是否通信成功
            if(!resp.get("return_code").equalsIgnoreCase("SUCCESS")){
                return ServerResponse.error("微信支付平台报错"+resp.get("return_msg"));
            }
            // 判断业务是否正确
            if(!resp.get("result_code").equalsIgnoreCase("SUCCESS")){
                return ServerResponse.error("微信支付平台报错"+resp.get("err_code_des"));
            }
            // 如果return_code 和 result_code都为SUCCESS时
            String url = resp.get("code_url");
                return ServerResponse.success(url);

        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponse.error(e.getMessage());
        }

    }


    @RequestMapping("queryOrderStatus")
    public ServerResponse queryOrderStatus(String orderNo) {
        try {
            MyConfig config = new MyConfig();
            WXPay wxPay = new WXPay(config);
            Map<String,String> map = new HashMap<>();
            map.put("out_trade_no",orderNo);

            int count = 0;
            for(;;){
                Map<String, String> resp = wxPay.orderQuery(map);
                System.out.println(resp);


                // 判断是否通信成功
                if(!resp.get("return_code").equalsIgnoreCase("SUCCESS")){
                    return ServerResponse.error("微信支付平台报错"+resp.get("return_msg"));
                }
                // 判断业务是否正确
                if(!resp.get("result_code").equalsIgnoreCase("SUCCESS")){
                    return ServerResponse.error("微信支付平台报错"+resp.get("err_code_des"));
                }
                // 交易状态
                if(resp.get("trade_state").equalsIgnoreCase("SUCCESS")){
                    return ServerResponse.success();
                }

                count ++;

                Thread.sleep(3000);
                if(count > 40) {
                    return ServerResponse.error("支付超时");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponse.error(e.getMessage());
        }
    }

}
