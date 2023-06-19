package com.lkd.wxpay;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("wxpay")
@Data
public class WXConfig {
    private String appId;
    private String appSecret;
    private String mchId;
    private String partnerKey;
    private String notifyUrl = "";

    public static String RESULT="<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>"; //回调成功后返回XML内容
}