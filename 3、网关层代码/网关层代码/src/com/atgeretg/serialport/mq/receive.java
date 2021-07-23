package com.atgeretg.serialport.mq;

import java.io.IOException;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.concurrent.TimeoutException;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.atgeretg.serialport.manage.SerialPortManager;
import com.atgeretg.serialport.ui.MainFrame;
import com.rabbitmq.client.AMQP;

public class receive {
	public static byte[] body1;
    private final static String QUEUE_NAME = "atguigu.news";

    public void rec()  throws IOException, TimeoutException {
        // 创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();

        //设置RabbitMQ地址
        factory.setHost("47.102.216.177");
        factory.setPort(5672);
        factory.setUsername("root");
        factory.setPassword("111111");
        //创建一个新的连接
        Connection connection = factory.newConnection();

        //创建一个通道
        Channel channel = connection.createChannel();

        //声明要关注的队列
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        System.out.println("Customer Waiting Received messages");
        
        Consumer consumer = new DefaultConsumer(channel) {
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
            	
            	String s = null;
                //String message = new String(body, "UTF-8");
               // System.out.println("Customer Received '" + message + "'"); 
                if(Test.a==true)
                {s = "00 01 00";}
                else
                {s = "00 00 00";}
                //DecimalFormat g1=new DecimalFormat("00");
                //String s = g1.format(Integer.valueOf(message));
                System.out.println(s);
                
                MainFrame y=new MainFrame();
               y.sendData(s);
               System.out.println("电机命令"+s.toString());
            }
        };
        //自动回复队列应答 -- RabbitMQ中的消息确认机制
        channel.basicConsume(QUEUE_NAME, true, consumer);
        
    }

}
