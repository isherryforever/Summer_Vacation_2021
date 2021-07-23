/*
 * MainFrame.java
 *
 * Created on 2016.8.19
 */

package com.atgeretg.serialport.ui;

import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

import java.awt.Container;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;

import com.atgeretg.serialport.exception.NoSuchPort;
import com.atgeretg.serialport.exception.NotASerialPort;
import com.atgeretg.serialport.exception.PortInUse;
import com.atgeretg.serialport.exception.SendDataToSerialPortFailure;
import com.atgeretg.serialport.exception.SerialPortOutputStreamCloseFailure;
import com.atgeretg.serialport.exception.SerialPortParameterFailure;
import com.atgeretg.serialport.exception.TooManyListeners;
import com.atgeretg.serialport.manage.SerialPortManager;
import com.atgeretg.serialport.mq.Test;
import com.atgeretg.serialport.mq.send;
import com.atgeretg.serialport.utils.DialogShowUtils;
import com.atgeretg.serialport.utils.GUIUtil;
import com.atgeretg.serialport.utils.MyUtils;

import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

/**
 * 主界面
 * 
 * @author atgeretg
 */
public class MainFrame extends JFrame implements ActionListener {

	/**
	 * 程序界面宽度
	 */
	public static final int WIDTH = 800;

	/**
	 * 程序界面高度
	 */
	
	public static final int HEIGHT = 600;

	private  JTextPane dataView = new JTextPane();
	private JScrollPane scrollDataView = new JScrollPane(dataView);

	// 串口设置面板
	//private JPanel serialPortPanel = new JPanel();
	private JLabel serialPortLabel = new JLabel("串口"); 
	private JLabel baudrateLabel = new JLabel("波特率");
	private JLabel SendLabel = new JLabel("未发送命令");
	private JComboBox commChoice = new JComboBox();
	private JComboBox baudrateChoice = new JComboBox();
	

	// 操作面板
	//private JPanel operatePanel = new JPanel();
	//private JTextArea dataInput = new JTextArea();
	private JButton serialPortOpenBtn = new JButton("打开串口");

	/**
	 * 正常的风格
	 */
	private final static String STYLE_NORMAL = "normal";
	/**
	 * 字体为红色
	 */
	private final static String STYLE_RED = "red";

	private List<String> commList = null;
	private static SerialPort serialport;

	public MainFrame() {
		Point p = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
		MyUtils.useLNF();
		initView();
		initComponents();
		initData();
		
	}

	private void initView() {
		// 关闭程序
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		// 禁止窗口最大化
		setResizable(false);
		// 设置程序窗口居中显示
		Point p = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
		setBounds(p.x - WIDTH / 2, p.y - HEIGHT / 2, 200, 400);
		getContentPane().setLayout(null);
		setTitle("串口通讯");
	}

	protected void initComponents() {
		// 数据显示
		dataView.setFocusable(false);
		//scrollDataView.setBounds(10, 10, 175, 200);
		scrollDataView.setBounds(30, 30, 340, 500);
		/* 数据区域的风格 */
		Style def = dataView.getStyledDocument().addStyle(null, null);
		StyleConstants.setFontFamily(def, "verdana");
		StyleConstants.setFontSize(def, 12);
		Style normal = dataView.addStyle(STYLE_NORMAL, def);
		Style s = dataView.addStyle(STYLE_RED, normal);
		StyleConstants.setForeground(s, Color.RED);
		dataView.setParagraphAttributes(normal, true);
		
		//背景图片label
		ImageIcon img = new ImageIcon("背景.jpg");
		JLabel imgLabel = new JLabel(img);
		this.getLayeredPane().add(imgLabel, new Integer(Integer.MIN_VALUE));
		imgLabel.setBounds(0, 0, img.getIconWidth(), img.getIconHeight());
		Container contain = this.getContentPane();
		((JPanel) contain).setOpaque(false); 
		
		
		getContentPane().add(scrollDataView);
		// 串口设置
		//serialPortPanel.setBorder(BorderFactory.createTitledBorder("操作设置"));
		//serialPortPanel.setBounds(10, 220, 170, 140);
		//serialPortPanel.setBounds(440, 20, 180, 200);
		//serialPortPanel.setLayout(null);
		//getContentPane().add(serialPortPanel);

		serialPortLabel.setForeground(Color.gray);
		//serialPortLabel.setBounds(10, 25, 40, 20);
		serialPortLabel.setBounds(450, 45, 40, 20);
		this.add(serialPortLabel);

		commChoice.setFocusable(false);
		//commChoice.setBounds(60, 25, 100, 20);
		commChoice.setBounds(500, 45, 100, 20);
		this.add(commChoice);

		baudrateLabel.setForeground(Color.gray);
		//baudrateLabel.setBounds(10, 60, 40, 20);
		baudrateLabel.setBounds(450, 80, 40, 20);
		this.add(baudrateLabel);

		baudrateChoice.setFocusable(false);
		//baudrateChoice.setBounds(60, 60, 100, 20);
		baudrateChoice.setBounds(500, 80, 100, 20);
		this.add(baudrateChoice);

		serialPortOpenBtn.setFocusable(false);
		//serialPortOpenBtn.setBounds(40, 95, 80, 80); 
		serialPortOpenBtn.setBounds(400, 115, 300, 300); 
		GUIUtil.setImageIcon(serialPortOpenBtn, "好运熊1.png", null);
		serialPortOpenBtn.addActionListener(this);
		serialPortOpenBtn.setFocusPainted(false);
		serialPortOpenBtn.setBorderPainted(false);
		serialPortOpenBtn.setContentAreaFilled(false);
		this.add(serialPortOpenBtn);
		
		SendLabel.setBounds(400, 500, 100, 30);
		SendLabel.setForeground(Color.red);
		SendLabel.setFont(new Font("宋体",Font.BOLD,16));
		this.add(SendLabel);

	}

	@SuppressWarnings("unchecked")
	public void initData() {
		commList = SerialPortManager.findPort();
		// 检查是否有可用串口，有则加入选项中
		if (commList == null || commList.size() < 1) {
			DialogShowUtils.warningMessage("没有搜索到有效串口！");
		} else {
			for (String s : commList) {
				commChoice.addItem(s);
			}
		}
		baudrateChoice.addItem("9600");
		baudrateChoice.addItem("19200");
		baudrateChoice.addItem("38400");
		baudrateChoice.addItem("57600");
		baudrateChoice.addItem("115200");
	}
	/**
	 * 打开串口
	 * 
	 */
	public void openSerialPort() {


		// 获取串口名称
		String commName = (String) commChoice.getSelectedItem();
		// 获取波特率
		int baudrate = 9600;
		String bps = (String) baudrateChoice.getSelectedItem();
		baudrate = Integer.parseInt(bps);

		// 检查串口名称是否获取正确
		if (commName == null || commName.equals("")) {
			DialogShowUtils.warningMessage("没有搜索到有效串口！");
		} else {
			try {
				serialport = SerialPortManager.openPort(commName, baudrate);
				if (serialport != null) {
					dataShow("串口已打开",STYLE_RED);
					GUIUtil.setImageIcon(serialPortOpenBtn, "好运熊2.png", null);
				}
			} catch (SerialPortParameterFailure e) {
				e.printStackTrace();
			} catch (NotASerialPort e) {
				e.printStackTrace();
			} catch (NoSuchPort e) {
				e.printStackTrace();
			} catch (PortInUse e) {
				e.printStackTrace();
				DialogShowUtils.warningMessage("串口已被占用！");
			}
			
		}

		try {
			SerialPortManager.addListener(serialport, new SerialListener());
		} catch (TooManyListeners e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 关闭串口
	 * 
	 */
	public void closeSerialPort() {
		SerialPortManager.closePort(serialport);
		dataShow("已经关闭串口",STYLE_RED);
		serialPortOpenBtn.setText("打开串口");
		GUIUtil.setImageIcon(serialPortOpenBtn, "好运熊1.png", null);
		serialport = null;
	}
	/**
	 * 打印数据到面板上
	 * 
	 */
	public  void dataShow(String text,String style) {
		StringBuilder builderData = new StringBuilder();
		builderData.setLength(0);
		builderData.append(MyUtils.formatDateStr_ss()).append("\r\n").append(text).append("\r\n");
		try {
			Document document = dataView.getDocument();
			if(STYLE_RED.equals(style))
			dataView.getDocument().insertString(document.getLength(), builderData.toString(),
					dataView.getStyle(style));
			else
				dataView.getDocument().insertString(document.getLength(), builderData.toString(),
						dataView.getStyle(STYLE_NORMAL));
			dataView.setCaretPosition(document.getLength());
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		if(Test.a==true) {		
	        this.sendData("00 01 00");
	        this.SendLabel.setText("电机转起来");
	        System.out.println("111");
		}
		else {
	        this.sendData("00 00 00");
	        this.SendLabel.setText("电机别转了");
	        System.out.println("000");
		}
	}


	/**
	 * 发送指定数据
	 * 
	 */
	public  static void sendData(String data) {
		try {
			SerialPortManager.sendToPort(serialport, MyUtils.HexString2Bytes(data));
		} catch (SendDataToSerialPortFailure e) {
			e.printStackTrace();
		} catch (SerialPortOutputStreamCloseFailure e) {
			e.printStackTrace();
		} 
		
	}

	public class SerialListener implements SerialPortEventListener {
		/**
		 * 处理监控到的串口事件
		 */
		public void serialEvent(SerialPortEvent serialPortEvent) {

			switch (serialPortEvent.getEventType()) {

			case SerialPortEvent.BI: // 10 通讯中断
				DialogShowUtils.errorMessage("与串口设备通讯中断");
				break;

			case SerialPortEvent.OE: // 7 溢位（溢出）错误

			case SerialPortEvent.FE: // 9 帧错误

			case SerialPortEvent.PE: // 8 奇偶校验错误

			case SerialPortEvent.CD: // 6 载波检测

			case SerialPortEvent.CTS: // 3 清除待发送数据

			case SerialPortEvent.DSR: // 4 待发送数据准备好了

			case SerialPortEvent.RI: // 5 振铃指示

			case SerialPortEvent.OUTPUT_BUFFER_EMPTY: // 2 输出缓冲区已清空
				break;

			case SerialPortEvent.DATA_AVAILABLE: // 1 串口存在可用数据
				byte[] data = null;
				try {
					if (serialport == null) {
						DialogShowUtils.errorMessage("串口对象为空！监听失败！");
					} else {
						// 读取串口数据
						data = SerialPortManager.readFromPort(serialport);							
						String s=MyUtils.byteArray2HexString(data, data.length, true);
						send s1 = new send();
	                    s1.sendmqtt(MyUtils.hexStringToString(s)); 
						dataShow(MyUtils.hexStringToString(s), STYLE_NORMAL);
						
					}
				} catch (Exception e) {
					DialogShowUtils.errorMessage(e.toString());
					// 发生读取错误时显示错误信息后退出系统
					System.exit(0);
				}
				break;
			}
		}
	}

	public static void main(String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				JFrame f=new MainFrame();
				f.setSize(700,600);
				f.setVisible(true);
				try {
					new Test().rec();
				} catch (IOException | TimeoutException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				/*receive r=new receive();							
	            try {
		     r.rec();
	         } catch (IOException | TimeoutException e1) {
		  // TODO Auto-generated catch block
		    e1.printStackTrace();
	        }		
			*/	
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == serialPortOpenBtn) {
			if (serialport == null) {
				openSerialPort();
			} else {
				closeSerialPort();
			}
		} 
	}
}