package com.cjkj.zyxxfb.timedtask;

import com.cjkj.zyxxfb.weixin.WeChatMsgSend;
import com.cjkj.zyxxfb.weixin.WeChatUrlData;
import com.cjkj.zyxxfb.zyxx.EveryDayxxtj;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class StSchedule {

    private static final SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private EveryDayxxtj ed  = null;
    @Scheduled(cron = "0 0 08,20 * * ?")
    public void cron(){
        long time = System.currentTimeMillis();
        ed = new EveryDayxxtj();
       // System.out.println(ed.dmxxtj());
        if (ed.dmxxtj().length()>0) {
            WeChatMsgSend swx = new WeChatMsgSend();
            try {
                String token = swx.getToken("wwa1f8306aa4803574", "VwDDcRnu7AE7HkaVeXRBvk15YG16NIqr4WT2s9y72EY");
                String postdata = swx.createpostdata("XiaoMiaoMiao|DongXiaoBo|HuangYi|RiBuBuBuLuo|SunYuWen|XueXueWu|ZhaoZhiJun|LiBaoDong", "text", 1000013, "content", ed.dmxxtj());
                String resp = swx.post("utf-8", WeChatMsgSend.CONTENT_TYPE, (new WeChatUrlData()).getSendMessage_Url(), postdata, token);
                System.out.println("获取到的token======>" + token);
                System.out.println("请求数据======>" + postdata);
                System.out.println("发送微信的响应数据======>" + resp);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("new 耗时：" + (System.currentTimeMillis() - time));
        }

    }

}
