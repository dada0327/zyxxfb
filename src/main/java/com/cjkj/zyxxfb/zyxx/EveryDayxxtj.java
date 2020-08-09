package com.cjkj.zyxxfb.zyxx;

import com.cjkj.zyxxfb.util.sql_data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
*@Description 2.1	日常作业信息统计和发送
*@Author Mr.Ge
*@Date 2020/7/7
*@Time 15:09
*/
public class EveryDayxxtj {


    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
    Date d = new Date();

    String dayBefore = getDayBefore(d,1);//前一锟斤拷
    String dqDay  = sdf.format(d).toString();//锟斤拷前时锟斤拷

    public static void main(String[] args) {
//        long time = System.currentTimeMillis();
//        EveryDayxxtj e = new EveryDayxxtj();
//        System.out.println(e.dmxxtj());
//        System.out.println(e.dmxxtj());
//
//        System.out.println("new 耗时：" + (System.currentTimeMillis() - time));

    }


    /**
    *@Description
    *@Return 统计地面信息
    *@Author Mr.Ge
    *@Date 2020/7/8
    *@Time 17:29
    */
    public String dmxxtj() {

        String sql = "SELECT ssshiID,zylx,COUNT(*) AS cs,SUM(hjydl) AS hjydl,SUM(gpydl) AS gpydl,SUM(ytydl) AS ytydl FROM dmzydxxb WHERE  zysj >='"+dayBefore+"' AND zyrq <='"+dqDay+"'  GROUP BY ssshiID,zylx";
        ResultSet rs  = null;
        String str = "",zylx="",result = "";
        String[] lx = new String[] {"增雨","增雪","防雹"};
        String[] xzq = new String[]{"石家庄市","唐山市","秦皇岛市","邯郸市","邢台市","保定市","张家口市","承德市","沧州市","廊坊市","衡水市","合计"};
        //（增雨、雪）：作业次数、火箭、高炮、烟条用弹量；（防雹）：作业次数、火箭、高炮；
        int[][][] jg = new int[xzq.length][lx.length][4];
        StringBuilder info = new StringBuilder();
       // EveryDayxxtj ed = new EveryDayxxtj();
        System.out.println("dm:"+sql);
        try {
            rs = sql_data.executeQuery(sql);
            if (rs.getRow()>0){
                while (!rs.isAfterLast()){
                    str = rs.getString("ssshiID");
                    for (int i = 0; i < xzq.length-1; i++) {//市
                        if (xzq[i].equals(str)==true) {
                            zylx = rs.getString("zylx");
                            for (int j = 0; j < lx.length; j++) {//类型
                                if (lx[j].equals(zylx) == true) {
                                    jg[i][j][0] = rs.getInt("cs");;//次数
                                    jg[i][j][1] = rs.getInt("hjydl");//火箭用弹量
                                    jg[i][j][2] = rs.getInt("gpydl");//高炮用弹量
                                    jg[i][j][3] = rs.getInt("ytydl");//烟条用弹量
                                }
                            }
                        }
                    }
                    rs.next();
                }

                // 增雨/雪；防雹
                int[][] res2 =new int[xzq.length][7];//合并

                for (int i = 0; i < xzq.length; i++) {
                    for (int j = 0; j < 7; j++) {
                        if (j<4){
                            res2[i][j] = jg[i][0][j]+jg[i][1][j];
                        }//增雨次数 火箭 高炮 烟条
                        if (j>=4){
                            res2[i][j] = jg[i][2][j-4];
                        }//防雹次数 火箭 高炮
                    }

                }
                //合计
                for (int i = 0; i < res2.length-1; i++) {//总计
                    for (int j = 0; j < 7; j++) {
                        res2[res2.length-1][j]+=res2[i][j];
                    }
                }

                String[] tjmc = new String[]{"开展地面增雨作业","发射火箭弹","发射炮弹","燃烧碘化银烟条","开展地面防雹作业","发射火箭弹","发射炮弹"};
                String[] title = new String[]{" 全省共开展地面增雨（雪）作业","发射火箭弹","发射炮弹","燃烧碘化银烟条","共开展地面防雹作业","发射火箭弹","发射炮弹"};
                String[] dw = new String[]{"点次","枚","发","根","点次","枚","发"};
                info.append(dayBefore+"-"+dqDay);
                for (int k =0; k < 7; k++){
                    if (res2[xzq.length-1][k]!=0){
                        info.append(title[k]+res2[xzq.length-1][k]+dw[k]+",");
                    }
                }
                info.replace(info.lastIndexOf(","),info.lastIndexOf(",")+1,"。");
                info.append("\n其中\n");
                String test = "";
                for (int i=0; i < xzq.length-1; i++){

                    for (int k =0; k < 7; k++){
                        if (res2[i][k]!=0){
                            test += tjmc[k]+res2[i][k]+dw[k]+",";
                        }
                    }
                    if (test.length()>0) {
                        info.append(xzq[i]);//只填入有数据的市
                        info.append(test.substring(0, test.lastIndexOf(",")) + ";\n");

                    }
                    test  = "";
                }
                info.replace(info.lastIndexOf(";"),info.lastIndexOf(";")+1,"。");
            }
            rs.close();
            sql_data.close();//关闭连接
            String fj = tjfjxx();//飞机信息
            if (fj.length()>0){//飞机信息
                info.append(dayBefore.substring(4,10)+"-");
                info.append(dqDay.substring(4,10));
                info.append(fj);
            }
            result = info.toString();
            //System.out.println(info);
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }


    public String tjfjxx() {
        String[] zls = new String[]{"bs_chjzl1", "bs_chjzl2", "bs_chjzl3", "bs_chjzl4", "bs_chjzl5", "bs_chjzl6"};//种类
        String[] yls = new String[]{"bs_chjyl1", "bs_chjyl2", "bs_chjyl3", "bs_chjyl4", "bs_chjyl5", "bs_chjyl6"};//数量
        //String[] nu = new String[]{"1","2","3","4","5","6"};

        String sql = " SELECT * FROM fjzyxxb_new WHERE zyrqsj >='"+dayBefore+"' AND zyrqsj <='"+dqDay+"' ";
        System.out.println("fj:"+sql);
        ResultSet rs = null;
        int cs = 0, sc = 0;
        double ytyl = 0.0;
        StringBuilder sb = new StringBuilder();
        try {
            rs = sql_data.executeQuery(sql);
            if (rs.getRow() > 0) {
                while (!rs.isAfterLast()) {
                    cs++;//作业架次
                    sc += rs.getInt("sc");//飞行时长 分钟
                    for (int i = 0; i < zls.length; i++) {
                        if (rs.getString(zls[i]).equals("1") == true) {//烟条用量
                            ytyl += rs.getDouble(yls[i]);
                        }
                    }
                    rs.next();
                }
                sb.append("全省共开展飞机增雨（雪）作业"+cs+"架次，累计飞行时长"+change(sc)+"。");
                if (ytyl>0){
                    sb.replace(sb.lastIndexOf("。"),sb.lastIndexOf("。")+1,"，");
                    sb.append("燃烧碘化银烟条"+ytyl+"根。");
                }
                //System.out.println("次数：" + cs + "，时长：" + sc + ",烟条用量：" + ytyl);
            }//rs.getRow end;
            rs.close();//关闭连接
            sql_data.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
    /**
     * 分钟换算成小时
     * @param second
     * @return
     */
    public static String change(int second){
        int d = 0;
        int s = 0;
        String st="";
        d = second/60;  //小时

        //分钟计算
//        DecimalFormat df = new DecimalFormat(".00");
//        Double fz = (double)second/60;
//        String fzs ="0."+ df.format(fz).substring(df.format(fz).lastIndexOf(".")+1, df.format(fz).length());
//        Double ss = Double.valueOf(fzs)*60;
//        String ss2 = ss.toString().substring(0,ss.toString().lastIndexOf("."));
        //  System.out.println(ss2);
        if(second%60!=0){
            s = second%60;
        }
        if(s<10)
            st="0"+s;
        else
            st=String.valueOf(s);
        return d+"小时"+st+"分钟";
    }

    /**
    *@Description 获取前一日；
    *@Return  前一日时间
    *@Author Mr.Ge
    *@Date 2020/7/8
    *@Time 10:02
    */
    public  String getDayBefore(Date d, int day) {
        Calendar now = Calendar.getInstance();

        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
        String dayBefore = new SimpleDateFormat("yyyyMMddHH").format(now
                .getTime());
        return dayBefore;
    }
}
