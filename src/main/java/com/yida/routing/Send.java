package com.yida.routing;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.yida.util.ContectionUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 发布者
 */
public class Send {
    private final static String EXCHANGE_NAME = "exchange_direct";
    public static void main(String[] args) throws Exception {
        //获取连接对象
        Connection connection = ContectionUtil.getConnection();
        //从连接中创建通道
        Channel channel = connection.createChannel();

        //声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");

        //发送消息(到交换机上)
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String message = "Hello 宜达"+time;
        channel.basicPublish(EXCHANGE_NAME,"error",null,message.getBytes());//注意：参数2表示routing-key

        //关闭通道和连接
        channel.close();
        connection.close();
    }
}
