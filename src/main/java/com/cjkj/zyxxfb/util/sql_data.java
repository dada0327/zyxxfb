package com.cjkj.zyxxfb.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.sql.*;
import java.util.Properties;

/**
*@Description connection mysql
*@Author Mr.Ge
*@Date 2020/7/7
*@Time 14:19
*/
public class sql_data {

    static Connection con;
        /**
        *@Description 数据库连接
        *@Date 2020/7/7
        *@Time 14:49
        */
        public static Connection connect(){

            Properties properties = readPropertiesFile("application.properties");
            String driver = properties.getProperty("spring.datasource.driver-class-name");
            String url = properties.getProperty("spring.datasource.url");
            String user = properties.getProperty("spring.datasource.username");
            String password = properties.getProperty("spring.datasource.password");

            try {
                Class.forName(driver).newInstance();
                con = DriverManager.getConnection(url, user, password);
            } catch (ClassNotFoundException e) {
                System.out.println(e.toString());
                System.out.println("数据库加载失败");
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return con;
        }

    /**
     * 查询
     */
    static Statement stmt = null;
    public static ResultSet executeQuery(String sql) {
        if (sql == null) return null;
        if (sql.length() < 5) return null;

        Connection conn = null;
        ResultSet rs = null;
        try {
            //conn = DriverManager.getConnection(url, user, password);
            conn = connect();
            stmt = conn.createStatement(
                    ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(sql);
            if (rs != null && !rs.isAfterLast()){rs.first();}

        } catch (SQLException ex) {
            System.err.println("sql_data.executeQuery:" + ex.getMessage());
        }
        return rs;
    }

    public static   void close() {
        try {
            con.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
        /**
        *@Description  从application.properties获取连接数据库信息
        *@Date 2020/7/7
        *@Time 14:47
        */
    public static Properties readPropertiesFile(String fileName){
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
