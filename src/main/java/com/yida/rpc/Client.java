package com.yida.rpc;

import com.rabbitmq.client.*;
import com.yida.util.ContectionUtil;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

/**
 * 发布者
 */
public class Client {
    private final static String RPC_QUEUE_NAME = "queue_rpc";
    public static void main(String[] args) throws Exception {
        //获取连接对象
        final Connection connection = ContectionUtil.getConnection();
        //从连接中创建通道
        final Channel channel = connection.createChannel();
        //声明一个队列，如果队列已经存在，则使用这个队列
        channel.queueDeclare(RPC_QUEUE_NAME,false,false,false,null);

        //生成一个32位随机字符串做为correlationId(自定义的消息id)
        final String correlationId = UUID.randomUUID().toString();

        //声明回调队列
        String callbackQueue = channel.queueDeclare().getQueue();
        //创建一个新消费者从回调队列中接收服务端传送的消息
        DefaultConsumer newConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String receivedCorrelationId = properties.getCorrelationId();
                if (receivedCorrelationId.equals(correlationId)) {
                    System.out.println("客户端接收的回调消息："+new String(body)+"，receivedCorrelationId="+receivedCorrelationId);

                    //关闭通道和连接
                    try {
                        channel.close();
                        connection.close();
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        //新消费者监听callbackQueue队列
        channel.basicConsume(callbackQueue,true,newConsumer);

        //创建带有correlationId和callbackQueue的消息属性
        AMQP.BasicProperties properties =
                new AMQP.BasicProperties.Builder().correlationId(correlationId).replyTo(callbackQueue).build();
        //发送消息（到队列上）
        String message = "Hello 宜达";
        channel.basicPublish("",RPC_QUEUE_NAME,properties,message.getBytes());
        System.out.println("客户端发送的消息为："+message+"，correlationId="+correlationId);
    }
}
