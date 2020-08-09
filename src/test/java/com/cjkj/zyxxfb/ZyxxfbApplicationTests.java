package com.cjkj.zyxxfb;

import com.cjkj.zyxxfb.weixin.WeChatMsgSend;
import com.cjkj.zyxxfb.weixin.WeChatUrlData;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;
import java.util.ResourceBundle;

@SpringBootTest
class ZyxxfbApplicationTests {

    @Test
    void contextLoads() {
        //Properties properties = readPropertiesFile("application.properties");
        //System.out.println(properties.getProperty("spring.datasource.username")+"\n"+properties.getProperty("spring.datasource.url"));
//        WeChatMsgSend swx = new WeChatMsgSend();
//        try {
//            String token = swx.getToken("wwc06cbe0e24760c68","hKem6wteUg7OxSpFGJyB5tD_QFAPUdRlQZldro7rSxc");
//            String postdata = swx.createpostdata("Gechunmei|LiuShuoYin", "text", 1000002, "content","This cjkj test info22");
//            String resp = swx.post("utf-8", WeChatMsgSend.CONTENT_TYPE,(new WeChatUrlData()).getSendMessage_Url(), postdata, token);
//            System.out.println("获取到的token======>" + token);
//            System.out.println("请求数据======>" + postdata);
//            System.out.println("发送微信的响应数据======>" + resp);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public Properties readPropertiesFile(String fileName){
        try {
            Resource resource = (Resource) new ClassPathResource(fileName);
            Properties props = PropertiesLoaderUtils.loadProperties(resource);

            return props;
        } catch (Exception e) {
            System.out.println("————读取配置文件：" + fileName + "出现异常，读取失败————");
            e.printStackTrace();
        }
        return null;
    }
}
