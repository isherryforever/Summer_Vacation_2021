package com.hyh.springboot.service;
import com.hyh.springboot.bean.Dht11;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Component
public class SchService {
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    @Qualifier("dataSource")
    DataSource dataSource;
    public static String number = "未知";
    @Scheduled(fixedRate = 2000)
        public void start()
    {
        Connection connection = null;
        try{
            connection= dataSource.getConnection();
           // System.out.println("连接数据库成功");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("连接失败");
        }
        try {
            PreparedStatement ps = null;
            ResultSet rs = null;
            ps = connection.prepareStatement("select * from switch");
            rs = ps.executeQuery();
            String num = "0";
            while (rs.next()) {
                num = rs.getString(1).trim();
                //System.out.println("num=" + num);
                if(num.equals("1"))
                    number = "开启";
                else
                    number = "关闭";
            }
            if(num.equals("1")){
                rabbitTemplate.convertAndSend("exchange.direct","atguigu.news","1");
                //System.out.println("检测到数据库 1 发送到mq队列1");
            }

            else{
                rabbitTemplate.convertAndSend("exchange.direct","atguigu.news","0");
                //System.out.println("检测到数据库 0 发送到mq队列0");
            }

            connection.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    


}
