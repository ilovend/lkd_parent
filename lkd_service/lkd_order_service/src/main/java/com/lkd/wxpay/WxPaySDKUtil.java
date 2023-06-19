package com.lkd.wxpay;
import com.github.wxpay.sdk.WXPayRequest;
import com.github.wxpay.sdk.WXPayUtil;
import com.github.wxpay.sdk.WxPaySdkConfig;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.lkd.exception.LogicException;
import com.lkd.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Map;

/**
 * 微信支付工具
 */
@Component
@Slf4j
public class WxPaySDKUtil {

    @Autowired
    private WXConfig wxConfig;

    @Autowired
    private WxPaySdkConfig wxPaySdkConfig;

    /**
     * 下单
     * @param wxPayDTO
     * @return
     */
    public String requestPay(  WxPayDTO wxPayDTO) {
        try{
            String nonce_str = WXPayUtil.generateNonceStr();
            //1.封装请求参数
            Map<String,String> map= Maps.newHashMap();
            map.put("appid",wxPaySdkConfig.getAppID());//公众账号ID
            map.put("mch_id",wxPaySdkConfig.getMchID());//商户号
            map.put("nonce_str", nonce_str);//随机字符串
            map.put("body",wxPayDTO.getBody());//商品描述
            map.put("out_trade_no",wxPayDTO.getOutTradeNo());//订单号
            map.put("total_fee",wxPayDTO.getTotalFee()+"");//金额
            map.put("spbill_create_ip","127.0.0.1");//终端IP
            map.put("notify_url",wxConfig.getNotifyUrl());//回调地址
            map.put("trade_type","JSAPI");//交易类型
            map.put("openid",wxPayDTO.getOpenid());//openId

            if(wxPayDTO.getExpireSecond()>0){
                String time = LocalDateTime.now().plus(wxPayDTO.getExpireSecond(), ChronoUnit.SECONDS)
                        .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
                log.info("过期时间:"+time);
                map.put("time_expire", time);//过期时间
            }


            String xmlParam  = WXPayUtil.generateSignedXml(map, wxPaySdkConfig.getKey());
            System.out.println("参数："+xmlParam);
            //2.发送请求
            WXPayRequest wxPayRequest=new WXPayRequest(wxPaySdkConfig);
            String xmlResult = wxPayRequest.requestWithCert("/pay/unifiedorder", null, xmlParam, false);
            log.info(xmlResult);
            //3.解析返回结果
            Map<String, String> mapResult = WXPayUtil.xmlToMap(xmlResult);
            //返回状态码
            String return_code = mapResult.get("return_code");
            //返回给移动端需要的参数
            Map<String, String> response = Maps.newHashMap();
            if(return_code.equals("SUCCESS")){
                // 业务结果
                String prepay_id = mapResult.get("prepay_id");//返回的预付单信息
                if(Strings.isNullOrEmpty(prepay_id)){
                    log.error("prepay_id is null","当前订单可能已经被支付");
                    throw new LogicException("当前订单可能已经被支付");
                }
                response.put("appId",wxPaySdkConfig.getAppID());
                response.put("package", "prepay_id=" + prepay_id);
                response.put("signType","MD5");
                response.put("nonceStr", WXPayUtil.generateNonceStr());

                Long timeStamp = System.currentTimeMillis() / 1000;
                response.put("timeStamp", timeStamp + "");//要将返回的时间戳转化成字符串，不然小程序端调用wx.requestPayment方法会报签名错误

                //再次签名，这个签名用于小程序端调用wx.requesetPayment方法
                String sign = WXPayUtil.generateSignature(response,wxConfig.getPartnerKey());
                response.put("paySign", sign);
                response.put("appId","");
                response.put("orderNo",wxPayDTO.getOutTradeNo());
                return JsonUtil.serialize(response);
            }else {
                log.error("调用微信统一下单接口失败",response);
                return null;
            }
        }catch (Exception ex){
            log.error("调用微信统一下单接口失败",ex);
            return "";
        }
    }


    /**
     * 验证支付结果
     * @param notifyResult
     * @return 订单号
     * @throws Exception
     */
    public String validPay(String notifyResult) throws Exception {
        //解析
        Map<String, String> map = WXPayUtil.xmlToMap( notifyResult );        //验签
        boolean signatureValid = WXPayUtil.isSignatureValid(map, wxConfig.getPartnerKey());

        if(signatureValid){
            if("SUCCESS".equals(map.get("result_code"))){
                //返回订单号
               return map.get("out_trade_no");
            }else {
                log.error("支付回调出错:"+notifyResult);
                return null;
            }
        }else {
            log.error("支付回调验签失败:"+notifyResult);
            return null;
        }
    }




}
