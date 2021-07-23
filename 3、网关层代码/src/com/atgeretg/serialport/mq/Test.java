package com.atgeretg.serialport.mq;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.atgeretg.serialport.utils.MyUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class Test {
	
public static boolean a=true;

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
	            	 String message = new String(body, "UTF-8");
	            	 if(message.equals("1"))
	            		 a=true;
	            	 else
	            		 a=false;
	                System.out.println(message);
	            }
	        };
	        //自动回复队列应答 -- RabbitMQ中的消息确认机制
	        channel.basicConsume(QUEUE_NAME, true, consumer);
	    }        
	    
	    
public static void main(String[] args) throws IOException, TimeoutException {
	new Test().rec();	
}
}
