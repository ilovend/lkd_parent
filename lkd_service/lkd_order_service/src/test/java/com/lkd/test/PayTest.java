package com.lkd.test;

import com.lkd.wxpay.WxPayDTO;
import com.lkd.wxpay.WxPaySDKUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class PayTest {


    @Autowired
    private WxPaySDKUtil wxPaySDKUtil;

    @Test
    public void testPay(){

        WxPayDTO dto=new WxPayDTO();
        dto.setOpenid("11222");
        dto.setBody("测试商品");
        dto.setOutTradeNo("303020193");
        dto.setTotalFee(1);

        String result = wxPaySDKUtil.requestPay(dto);
        System.out.println(result);
    }

}
