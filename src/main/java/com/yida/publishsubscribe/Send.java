package com.yida.publishsubscribe;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.yida.util.ContectionUtil;

/**
 * 发布者
 */
public class Send {
    private final static String EXCHANGE_NAME = "exchange_fanout";
    public static void main(String[] args) throws Exception {
        //获取连接对象
        Connection connection = ContectionUtil.getConnection();
        //从连接中创建通道
        Channel channel = connection.createChannel();

        //声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

        //发送消息(到交换机上)
        String message = "Hello 宜达";
        channel.basicPublish(EXCHANGE_NAME,"",null,message.getBytes());

        //关闭通道和连接
        channel.close();
        connection.close();
    }
}
