package com.yida.util;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 创建连接对象
 */
public class ContectionUtil {
    public static Connection getConnection() {
        try {
            //定义连接工厂
            ConnectionFactory factory = new ConnectionFactory();
            //设置服务地址（haproxy代理服务器地址）
            factory.setHost("192.168.43.61");
            //端口(haproxy代理服务器端口）
            factory.setPort(5673);
            //设置账号信息，用户名，密码，vhost
            factory.setVirtualHost("vhost01");
            factory.setUsername("admin");
            factory.setPassword("admin");
            //通过工厂对象获取连接
            Connection connection = factory.newConnection();
            return connection;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
