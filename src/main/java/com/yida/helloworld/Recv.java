package com.yida.helloworld;

import com.rabbitmq.client.*;
import com.yida.util.ContectionUtil;

import java.io.IOException;

/**
 * 接收者
 */
public class Recv {
    private final static String QUEUE_NAME = "queue_hello";

    public static void main(String[] args) throws Exception {
        //获取连接对象
        Connection connection = ContectionUtil.getConnection();
        //从连接中创建通道
        Channel channel = connection.createChannel();
        //创建队列，声明创建一个队列，如果队列已经存在，则使用这个队列
        /**
         * 参数说明：
         * 1.队列名称
         * 2.队列消息是否持久化 true：持久化（指队列里的未消费完的消息在服务器意外终止时，会持久化到硬盘，不会丢失），false：非持久化
         * 3.队列消息是否独占模式  true：独占模式（指当前队列只用于当前连接，如果连接断掉，其它连接将不能继续使用），false：非独占
         * 4.是否自动删除队列中的消息 true：自动删除队列里的消息（连接一旦断开，将会自动删除队列里所有消息），false：则不会
         * 5.其它额外参数
         */
        channel.queueDeclare(QUEUE_NAME,true,false,false,null);

        //定义队列的消费者
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            /**
             *
             * @param consumerTag 同一个会话标记，是固定的，可以理解为当前会话的唯一标识
             * @param envelope 可以通过该对象获取当前消息的编号，发送的队列，交换机信息
             * @param properties 随消息一起发送的其它属性
             * @param body 消息内容
             * @throws IOException
             */
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body);
                System.out.println("接收到的消息：" + msg);
            }
        };

        //创建一个消费者（监听）
        /**
         * 参数说明：
         * 1.队列名称
         * 2.是否自动回执确认（即：向MQ服务器自动发送已接收到消息的确认），true表示自动，false表示手动。
         */
        channel.basicConsume(QUEUE_NAME,true,consumer);
    }
}
