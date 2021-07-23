package com.hyh.springboot.Dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
public class RegNLog {
	Connection c=null;
	Statement s=null;
	String uName="ABC";
	String uPassword="BCD";
	public RegNLog()
	{
		try {
			Class.forName("com.mysql.jdbc.Driver");
			c=DriverManager.getConnection("jdbc:mysql://47.102.216.177:3306/dht11?characterEncoding=UTF-8",
					"root","admin");
			s=c.createStatement();
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	public void setUName(String a)
	{
		uName=a;
	}
	public String getUName()
	{
		return uName;
	}
	public void setUPassword(String b)
	{
		uPassword=b;
	}
	public String getUPassword()
	{
		return uPassword;
	}
	public void saveString(String str1,String str2,String str3) {
		String sql="insert into register values("+str1+","+str2+","+str3+")";
		
		try {
			boolean ifif=s.execute(sql);
			if(ifif)
			{
				System.out.println("成功插入");
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
	public boolean checkifExists(String str1,String str2,String str3)//用来看能不能注册 str1为username str2为password str3为email
	{
		String sql="select * from register where userName="+'"'+str1+'"'+" or Email="+'"'+str3+'"';
		System.out.println(sql);
		System.out.println("测试函数有没有被调用");
		try {
			ResultSet rs=s.executeQuery(sql);
//			if(rs.equals(null))
//			{
//				saveAll(str1,str2,str3);
//				System.out.println("这里是false");
//				return false;
//			}
//			while(rs.next()) {
//				String username=rs.getString(1);
//				String email=rs.getString(3);
//				if(str1.equals(username)||str3.equals(email))
//				{
//					return true;
//				}
//			}
			if(rs.next())
			{
				return true;
			}
			else
			{
				saveAll(str1,str2,str3);
				return false;
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	public void saveAll(String username,String password,String email)
	{
		try {
			String saveStr="insert into register values("+'"'+username+'"'+","+'"'+password.hashCode()+'"'+","+'"'+email+'"'+")";
			s.execute(saveStr);
			System.out.println("插入成功");
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	public boolean checkPassword(String username,String password)
	{
		try {
			String checkStr="select userPassWord from register where userName="+'"'+username+'"';
			ResultSet rs=s.executeQuery(checkStr);
			rs.next();
			String passwordGot=rs.getString(1);
			System.out.println(rs);
			System.out.println(rs.first());
			System.out.println(rs.getString(1));
			
			String code=String.valueOf(password.hashCode());
			if(passwordGot.equals(code))
			{
				return true;
			}
			else
			{
				return false;
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}
