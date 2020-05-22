package com.yida.cluster;

import com.rabbitmq.client.*;
import com.yida.util.RabbitMQUtil;

import java.io.IOException;

/**
 * 消费者
 */
public class Consumer {
    private final static String QUEUE_NAME = "test_queue_cluster";

    public static void main(String[] args) throws Exception {
        //获取连接对象
        Connection connection = RabbitMQUtil.getConnection();
        //从连接中创建通道
        final Channel channel = connection.createChannel();
        //创建队列，声明创建一个队列，如果队列已经存在，则使用这个队列
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        //定义队列的消费者
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                String msg = new String(body);
                System.out.println("接收到的消息：" + msg);

                //手动返回一条消息的回执确认
                channel.basicAck(envelope.getDeliveryTag(),false);
            }
        };

        //创建一个消费者（监听）
        channel.basicConsume(QUEUE_NAME, false, consumer);
    }
}
