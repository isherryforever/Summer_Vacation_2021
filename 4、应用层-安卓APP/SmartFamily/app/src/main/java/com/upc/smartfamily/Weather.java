package com.upc.smartfamily;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Weather extends AppCompatActivity {
    // 天气情况查询接口地址
    public static String API_URL = "http://apis.juhe.cn/simpleWeather/query";
    // 接口请求Key，这里需要输入自己的API_KEY
    public static String API_KEY = "e881bc9d7bbc309eb34cac2ebc57acd6";

    /**
     * get方式的http请求
     *
     * @param httpUrl 请求地址
     * @return 返回结果
     */
    @SuppressLint("NewApi")
    public static String doGet(String httpUrl, String queryParams) {
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;
        String result = null;// 返回结果字符串
        try {
            // 创建远程url连接对象
            URL url = new URL(new StringBuffer(httpUrl).append("?").append(queryParams).toString());
            // 通过远程url连接对象打开一个连接，强转成httpURLConnection类
            connection = (HttpURLConnection) url.openConnection();
            // 设置连接方式：get
            connection.setRequestMethod("GET");
            // 设置连接主机服务器的超时时间：15000毫秒
            connection.setConnectTimeout(5000);
            // 设置读取远程返回的数据时间：60000毫秒
            connection.setReadTimeout(6000);
            // 发送请求
            connection.connect();
            // 通过connection连接，获取输入流
            if (connection.getResponseCode() == 200) {
                inputStream = connection.getInputStream();
                // 封装输入流，并指定字符集
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                // 存放数据
                StringBuilder sbf = new StringBuilder();
                String temp;
                while ((temp = bufferedReader.readLine()) != null) {
                    sbf.append(temp);
                    sbf.append(System.getProperty("line.separator"));
                }
                result = sbf.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != bufferedReader) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                connection.disconnect();// 关闭远程连接
            }
        }
        return result;
    }
    /**
     * 根据城市名查询天气情况
     *
     * @param cityName
     */
    public static void queryWeather(String cityName) {
        Map<String, Object> params = new HashMap<>();//组合参数
        params.put("city", cityName);
        params.put("key", API_KEY);
        String queryParams = urlencode(params);

        String response = doGet(API_URL, queryParams);
        try {
            JSONObject jsonObject = JSONObject.fromObject(response);
            int error_code = jsonObject.getInt("error_code");
            if (error_code == 0) {
                System.out.println("调用接口成功");

                JSONObject result = jsonObject.getJSONObject("result");
                JSONObject realtime = result.getJSONObject("realtime");

                rcvString="";
                rcvString+=("\n城市："+ result.getString("city"));
                rcvString+=("\n天气："+ realtime.getString("info"));
                rcvString+=("\n温度：" +realtime.getString("temperature"));
                rcvString+=("\n湿度："+ realtime.getString("humidity"));
                rcvString+=("\n风向："+ realtime.getString("direct"));
                rcvString+=("\n风力："+ realtime.getString("power"));
                rcvString+=("\n空气质量："+ realtime.getString("aqi"));
                CityN=result.getString("city");
            } else {
                Log.d("调用接口失败：" , jsonObject.getString("reason"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 将map型转为请求参数型
     *
     * @param data
     * @return
     */
    public static String urlencode(Map<String, ?> data) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, ?> i : data.entrySet()) {
            try {
                sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue() + "", "UTF-8")).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        String result = sb.toString();
        result = result.substring(0, result.lastIndexOf("&"));
        return result;
    }
    TextView city,inputText,text_weather;
    Button msgBtn;
    public static String rcvString = "";
    public static String CityN = "青岛";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        city = findViewById(R.id.city);
        msgBtn = findViewById(R.id.btn_weather);
        inputText = findViewById(R.id.edit_weather);
        text_weather=findViewById(R.id.text_weather);
        this.getWindow().setBackgroundDrawableResource(R.drawable.d);
        msgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer.create(MyApplication.getAppContext(),R.raw.water).start();
                String cityName = inputText.getText().toString();
                //queryWeather("温州");
                //if(inputText.getText()==null)
                    //cityName="青岛";
                queryWeather(cityName);
                //textView.setText(rcvString);
                text_weather.setText(rcvString);
                city.setText(CityN);
                Toast.makeText(Weather.this,cityName+" 天气查询成功",Toast.LENGTH_LONG).show();
            }
        });
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        /*Uri uri = Uri.parse("http://www.baidu.com");
        Intent it = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(it);*/
    }

}