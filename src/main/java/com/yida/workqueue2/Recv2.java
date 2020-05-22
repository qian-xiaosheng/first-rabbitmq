package com.yida.workqueue2;

import com.rabbitmq.client.*;
import com.yida.util.ContectionUtil;

import java.io.IOException;

/**
 * 接收者2
 */
public class Recv2 {
    private final static String QUEUE_NAME = "queue_world";

    public static void main(String[] args) throws Exception {
        //获取连接对象
        Connection connection = ContectionUtil.getConnection();
        //从连接中创建通道
        final Channel channel = connection.createChannel();
        //设置预取的消息数量为1（即：每次只取1条消息，消费完，再取下一条）
        channel.basicQos(1);
        //创建队列，声明创建一个队列，如果队列已经存在，则使用这个队列
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

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
                System.out.println("[Recv2]接收到的消息：" + msg);
                try {
                    Thread.sleep(1000);//休眠100毫秒（1秒）
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //手动返回一条消息的回执确认
                /**
                 * 参数说明：
                 * 1.消息编号
                 * 2.是否批量返回多条消息的回执确认，false表示每次只返回1条(推荐！与上面设置预取的消息数量保持一致)，true表示批量返回
                 */
                channel.basicAck(envelope.getDeliveryTag(),false);
            }
        };

        //创建一个消费者（监听）
        channel.basicConsume(QUEUE_NAME,false,consumer);//这里的参数2要改为false，表示手动发送回执确认
    }
}
