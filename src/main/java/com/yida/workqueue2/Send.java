package com.yida.workqueue2;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.yida.util.ContectionUtil;

/**
 * 发布者
 */
public class Send {
    private final static String QUEUE_NAME = "queue_world";
    public static void main(String[] args) throws Exception {
        //获取连接对象
        Connection connection = ContectionUtil.getConnection();
        //从连接中创建通道
        Channel channel = connection.createChannel();
        //声明队列，声明创建一个队列，如果队列已经存在，则使用这个队列
        /**
         * 参数说明：
         * 1.队列名称
         * 2.队列消息是否持久化 true：持久化（指队列里的未消费完的消息在服务器意外终止时，会持久化到硬盘，不会丢失），false：非持久化
         * 3.队列消息是否独占模式  true：独占模式（指当前队列只用于当前连接，如果连接断掉，其它连接将不能继续使用），false：非独占
         * 4.是否自动删除队列中的消息 true：自动删除队列里的消息（连接一旦断开，将会自动删除队列里所有消息），false：则不会
         * 5.其它额外参数
         */
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        //发送20条消息（到队列上）
        for (int i = 0; i < 20; i++) {
            String message = "Hello 宜达"+i;
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
            Thread.sleep(10);//休眠十毫秒
        }

        //关闭通道和连接
        channel.close();
        connection.close();
    }
}
