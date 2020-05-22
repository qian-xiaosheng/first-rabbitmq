package com.yida.cluster;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.yida.util.RabbitMQUtil;

/**
 * 生产者
 */
public class Producer {
    private final static String QUEUE_NAME = "test_queue_cluster";
    public static void main(String[] args) throws Exception {
        //获取连接对象
        Connection connection = RabbitMQUtil.getConnection();
        //从连接中创建通道
        Channel channel = connection.createChannel();
        //声明队列，声明创建一个队列，如果队列已经存在，则使用这个队列
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        //消息内容
        String message = "Hello 宜达互联";
        //发送消息（到队列上）
        channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
        System.out.println("消息发送成功!");
        //关闭通道和连接
        channel.close();
        connection.close();
    }
}
