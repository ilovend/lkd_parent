package com.lkd.utils;

import com.google.common.base.Strings;
import org.springframework.web.client.RestTemplate;

/**
 * 获取openId工具类
 */
public class OpenIDUtil {

    public static String getOpenId(String appId, String secret, String jsCode) {
        String getOpenIdUrl = "https://api.weixin.qq.com/sns/jscode2session?appid="+appId+"&secret="+secret+"&js_code="+jsCode+"&grant_type=authorization_code";
        RestTemplate restTemplate = new RestTemplate();
        String respResult = restTemplate.getForObject(getOpenIdUrl,String.class);
        System.out.println(respResult+"--------------------------------------");
        if( Strings.isNullOrEmpty(respResult)) return "";
        try{
            String errorCode = JsonUtil.getValueByNodeName("errcode",respResult) ;
            if(!Strings.isNullOrEmpty(errorCode)){
                int errorCodeInt = Integer.valueOf(errorCode).intValue();
                if(errorCodeInt != 0) return "";
            }
            return JsonUtil.getValueByNodeName("openid",respResult);
        }catch (Exception ex){
            return "";
        }
    }

}
