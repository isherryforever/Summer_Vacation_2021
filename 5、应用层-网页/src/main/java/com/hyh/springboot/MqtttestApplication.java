package com.hyh.springboot;

//import com.hyh.springboot.Dao.SDao;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
/**
 * 自动配置
 * 1、RabbitAutoConfigation
 * 2、有自动配置了连接工厂ConnectionFactory
 * 3、RabbitProperties封装了RabbitMQ的配置
 * 4、RabbitTemplate：给RabbitMQ发送和接收信息
 * 5、AmqpAdmin：RabbitMQ系统管理功能组件
 * 6、@EnableRabbit+@RabbitListener监听消息队列的内容
 */
@EnableScheduling
@EnableRabbit //开启基于注解的RabbitMQ模式
@SpringBootApplication
public class MqtttestApplication {
	public static void main(String[] args) {
		SpringApplication.run(MqtttestApplication.class, args);
	}
}
	
