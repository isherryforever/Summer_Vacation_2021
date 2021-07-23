package com.hyh.springboot.Dao;

import com.hyh.springboot.bean.Dht11;
//import com.hyh.springboot.bean.LinkToSql;
import com.mysql.cj.jdbc.ConnectionWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class Dht11Dao {
    @Autowired
    @Qualifier("dataSource")
    DataSource dataSource;
    public static Map<Integer,Dht11> data = null;
    public static Map<Integer,Dht11> sdata = null;
    public static int count = 1;
    public static String timer ="1970-01-01 00:00:00";
    static
    {
        data = new HashMap<Integer,Dht11>();
        sdata = new HashMap<Integer, Dht11>();
    }

    public void schon(){

        Connection connection = null;
        try{
            connection= dataSource.getConnection();
            System.out.println("save连接数据库成功");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("连接失败");
        }
        try
        {
            Statement sta = null;
            String a1 = "update switch set  num = 1";
            //System.out.println(a1);
            sta = connection.createStatement();
            sta.executeUpdate(a1);
            connection.close();

        }
        catch(Exception e)
        {	   e.printStackTrace(); }
    }

    public void schoff(){
        Connection connection = null;
        try{
            connection= dataSource.getConnection();
            System.out.println("save连接数据库成功");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("连接失败");
        }
        try
        {
            Statement sta = null;
            String a1 = "update switch set  num = 0";
            System.out.println(a1);
            sta = connection.createStatement();
            sta.executeUpdate(a1);
            connection.close();

        }
        catch(Exception e)
        {	   e.printStackTrace(); }
    }

    public void get() {
        Connection connection = null;
        try{
        connection= dataSource.getConnection();
            System.out.print("get连接数据库成功");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("连接失败");
        }
        try {
            PreparedStatement ps = null;
            ResultSet rs = null;
            ps = connection.prepareStatement("select * from dht11 where "+"time>"+"'"+timer+"'"+" order by time ASC");
            rs = ps.executeQuery();
            while (rs.next()) {
                String temperature = rs.getString(1).trim();
                String humidity = rs.getString(2).trim();
                String smog = rs.getString(3).trim();
                String infrared = rs.getString(4).trim();
                String time = rs.getString(5).trim();
                
                String str = "temperature " + temperature + " humidity " + humidity +" smog " +smog+" infrared " +infrared+" time "+time;
                    data.put(count, new Dht11(temperature,humidity, smog,infrared,time));
                    count++;
                    timer=time;
            }
            System.out.println("count="+count);
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Collection<Dht11> getAll()
    {
        return data.values();
    }
    public Collection<Dht11> getNow()
    {
    	sdata.put(1,data.get(count));
    	return sdata.values();
    }
    public Collection<Dht11> getfifty()
    {
        if (count>50) {//如果有五十条
            int j=1;
            for (int i = count-1; i >= count-50; i--) {
                sdata.put(j, data.get(i));
                j++;
            }
        }
        else
            {     int j=1;
            for (int i = count-1; i >= 1; i--) {
                sdata.put(j, data.get(i));
                j++;
        }
        }
        return sdata.values();
     }
        public Dht11 getone()
        {int temp = count-1;
            return data.get(temp);
        }

    public void save(String string)
    {
        Connection connection = null;
        try{
            connection= dataSource.getConnection();
            System.out.println("save连接数据库成功");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("连接失败");
        }
        try
        {
            Statement sta = null;
            String[] s1=string.split(" ");
            //String a1 = "insert into dht11(temperature,humidity,t) select "+"'"+dht11.getTemperature()+"'"+","+"'"+dht11.getHumidity()+"'"+","+"getdate()";
            s1[0]=s1[0].replace("Node1:Tem=","");
            s1[1]=s1[1].replace("Hu=:","");
            s1[3]=s1[3].replace("Node2:DO=", "");
            s1[4]=s1[4].replace("AO=","");

            String a1 = "insert into dht11(temperature,humidity,smog,infrared,time) select "+"'"+s1[0]+"'"+","+"'"+s1[1]+"'"+","+"'"+s1[4]+"'"+","+"'"+s1[3]+"'"+","+"now()";
            System.out.println(a1);
            sta = connection.createStatement();
            sta.executeUpdate(a1);
            connection.close();

        }
        catch(Exception e)
        {	   e.printStackTrace(); }
    }
}
