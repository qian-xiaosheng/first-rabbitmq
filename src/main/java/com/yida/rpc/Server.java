package com.yida.rpc;

import com.rabbitmq.client.*;
import com.yida.util.ContectionUtil;

import java.io.IOException;

/**
 * 接收者1
 */
public class Server {
    private final static String RPC_QUEUE_NAME = "queue_rpc";

    public static void main(String[] args) throws Exception {
        //获取连接对象
        Connection connection = ContectionUtil.getConnection();
        //从连接中创建通道
        final Channel channel = connection.createChannel();
        //设置预取的消息数量为1（即：每次只取1条消息，消费完，再取下一条）
        channel.basicQos(1);
        //创建队列，声明创建一个队列，如果队列已经存在，则使用这个队列
        channel.queueDeclare(RPC_QUEUE_NAME,false,false,false,null);

        //定义队列的消费者
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body);
                System.out.println("服务端接收到的消息：" + msg);

                //手动返回一条消息的回执确认
                channel.basicAck(envelope.getDeliveryTag(),false);

                //服务端返回一条消息给客户端
                //取出消息的correlationId（自定义的消息id）
                String correlationId = properties.getCorrelationId();
                //创建与接收消息相同correlationId的消息属性
                AMQP.BasicProperties replayProperties = new AMQP.BasicProperties.Builder().correlationId(correlationId).build();
                //properties.getReplyTo() 获取回调队列名
                channel.basicPublish("",properties.getReplyTo(),replayProperties,"你好！宜达互联".getBytes());
            }
        };

        //创建一个消费者（监听）
        channel.basicConsume(RPC_QUEUE_NAME,false,consumer);//这里的参数2要改为false，表示手动发送回执确认
    }
}
