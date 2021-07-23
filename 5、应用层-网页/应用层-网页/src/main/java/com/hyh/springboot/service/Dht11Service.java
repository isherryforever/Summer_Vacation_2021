package com.hyh.springboot.service;

import com.hyh.springboot.Dao.Dht11Dao;
import com.hyh.springboot.bean.Dht11;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Dht11Service {
    @Autowired
    Dht11Dao dht11Dao;

    @RabbitListener(queues = "queue")
    public void receive(String string)
    {
    	System.out.println("mq接收消息："+string);
        dht11Dao.save(string);
        dht11Dao.get();
    	//System.out.println("display："+dht11Dao.getAll());

    }
}
