package com.yida.util;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitMQUtil {

    private static ConnectionFactory connectionFactory;

    static {
        connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.43.61");//haproxy代理服务器地址
        connectionFactory.setPort(5673);//haproxy代理服务器地址
        connectionFactory.setUsername("admin");//rabbitmq服务器的用户名
        connectionFactory.setPassword("admin");//rabbitmq服务器的密码
        connectionFactory.setVirtualHost("vhost01");//设置虚拟节点
    }

    //返回rabbitmq连接对象
    public static Connection getConnection() {
        try {
            Connection connection = connectionFactory.newConnection();
            return connection;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
