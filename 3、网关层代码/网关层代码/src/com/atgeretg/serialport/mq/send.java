package com.atgeretg.serialport.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


public class send {
	   public  void sendmqtt(String string) throws java.io.IOException, TimeoutException
	    {
	        /**
	         * 创建连接连接到MabbitMQ
	         */    	
	        ConnectionFactory factory = new ConnectionFactory();
	        //设置MabbitMQ所在主机ip或者主机名
	        factory.setHost("47.102.216.177");
	        factory.setPort(5672);
	        factory.setUsername("root");
	        factory.setPassword("111111");
	        //创建一个连接
	        Connection connection = factory.newConnection();
	        //创建一个频道
	        Channel channel = connection.createChannel();
	        //指定一个队列
	        channel.queueDeclare("queue", false, false, false, null);
	        //发送的消息
	        String message = "hello world!";
	        //往队列中发出一条消息
	        channel.basicPublish("", "queue", null,string.getBytes());
	        if(string!=null)
	        	//System.out.println("Sent '" + message + "'");
	        //关闭频道和连接
	        channel.close();
	        connection.close();
	    }
	   
	    public static void main(String[] args) throws IOException, TimeoutException {
			new send().sendmqtt("hello");
			System.out.println("OK");
	   }
}
