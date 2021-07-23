package com.upc.smartfamily;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static cn.leancloud.AVOSCloud.getContext;

public class MainActivity extends AppCompatActivity {
TextView camera,weather,wendu,water,smoke,motor,people,quit,shujv,web,time;
String kaiguan="OFF",tem="100",hum="100",mq="100",shijian="0000-00-00";
boolean flag=false;
public Context myContext;
static OkHttpClient client1 = new OkHttpClient();
static OkHttpClient client2 = new OkHttpClient();
private final static String Url = "http://34339k72u0.qicp.vip/mychart";
//在子线程刷新获取数据时，检查风扇开关，然后下达命令
//由于风扇会自动开关，所以也要根据传入的烟感和温湿度的数值进行修改
//环境变化触发的风扇转动无法在APP关掉
/*
public static ObjectAnimator coverAnimator( iv){
    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(iv, "rotation",
            0f, 360.0f);
    objectAnimator.setDuration(10000); // 10 秒钟转一圈
    objectAnimator.setInterpolator(new LinearInterpolator()); // 匀速转运
    objectAnimator.setRepeatCount(-1); // 无限循环
    return objectAnimator;
}*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getWindow().setBackgroundDrawableResource(R.drawable.a);
        camera = findViewById(R.id.btn1);
        weather = findViewById(R.id.btn2);
        wendu = findViewById(R.id.btn3);
        water = findViewById(R.id.btn4);
        smoke = findViewById(R.id.btn5);
        motor = findViewById(R.id.btn6);
        people = findViewById(R.id.btn7);
        quit = findViewById(R.id.btn8);
        shujv = findViewById(R.id.btn11);
        web = findViewById(R.id.btn12);
        time = findViewById(R.id.time);
        motor.setText("风扇："+kaiguan);
        wendu.setText("温度："+tem+" ℃");
        water.setText("湿度："+hum+" %");
        smoke.setText("烟雾："+mq+" ppm");
        time.setText("更新时间："+shijian);
        motor.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                MediaPlayer.create(MyApplication.getAppContext(),R.raw.water).start();
                if(kaiguan=="OFF")
                {
                    kaiguan="ON";
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Request request1 = new Request.Builder()
                                    .url("http://www.glp666.ltd/on")
                                    .get()
                                    .build();
                            Call call = client1.newCall(request1);
                            call.enqueue(new Callback() {
                                @Override
                                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                }
                                @Override
                                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                                }
                            });
                        }
                    }).start();
                }
                else
                {
                    kaiguan="OFF";
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Request request2 = new Request.Builder()
                                    .url("http://www.glp666.ltd/off")
                                    .get()
                                    .build();
                            Call call = client2.newCall(request2);
                            call.enqueue(new Callback() {
                                @Override
                                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                }
                                @Override
                                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                                }
                            });
                        }
                    }).start();
                }
                motor.setText("风扇："+kaiguan);
            }
        });
        weather.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                MediaPlayer.create(MyApplication.getAppContext(),R.raw.water).start();
                Intent intent = new Intent(MainActivity.this,Weather.class);
                startActivity(intent);
            }
        });
        camera.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                MediaPlayer.create(MyApplication.getAppContext(),R.raw.water).start();
                Intent intent = new Intent(MainActivity.this,WebViewActivity.class);
                startActivity(intent);
            }
        });
        people.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                MediaPlayer.create(MyApplication.getAppContext(),R.raw.water).start();
                Intent intent = new Intent(MainActivity.this,People.class);
                startActivity(intent);
            }
        });
        quit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                MediaPlayer.create(MyApplication.getAppContext(),R.raw.water).start();
                SharedPreferences shared;
                shared = MyApplication.getAppContext().getSharedPreferences("spfRecord",0);
                int version = shared.getInt("version", 0);
                SharedPreferences.Editor edit = shared.edit();
                edit.putString("account",null);
                edit.putString("password",null);
                edit.putBoolean("isRemember",true);
                edit.putBoolean("isLogin",false);
                edit.apply();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        web.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MediaPlayer.create(MyApplication.getAppContext(),R.raw.water).start();
                final Uri uri = Uri.parse("http://www.glp666.ltd/");
                final Intent it = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(it);
            }
        });
        @SuppressLint("HandlerLeak")
        Handler mHandler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case 0:
                        wendu.setText("温度："+tem+" ℃");
                        water.setText("湿度："+hum+" %");
                        //red.setText("红外状态："+redvalue);
                        smoke.setText("烟雾浓度："+mq+" PPm");
                        time.setText("更新时间："+shijian);
                        break;
                    default:
                        break;
                }
                super.handleMessage(msg);
            }
        };
        shujv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Api.postRequest(new myCallback(){
                            @Override
                            public void onSuccess(final String res) {
                                mHandler.sendEmptyMessage(0);
                                //showToastSync(res==null?"123":res);
                                Log.e("PostSuccess", res==null?"":res);
                            }
                            @Override
                            public void onFailure(Exception e) {
                                showToastSync("网络连接失败");
                            }
                        });
                    }
                }).start();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Api.getRequest(new myCallback() {
                            @Override
                            public void onSuccess(final String res) {
                                Log.e("GetSuccess", res);
                                Sensor data = new Gson().fromJson(res, Sensor.class);
                                hum=data.getHumidity();
                                tem=data.getTemperature();
                                //redvalue=data.getInfrared();
                                mq=data.getSmog();
                                shijian=data.getTime();
                                // if(redvalue.equals("1")) redvalue=redvalue+" ——有人";
                                //else redvalue=redvalue+" ——无人";
                                mHandler.sendEmptyMessage(0);
                            }
                            @Override
                            public void onFailure(Exception e) {
                                showToastSync("网络连接失败");
                            }
                        });
                    }
                }, 500);//0.5秒后执行Runnable中的run方法
                if(!flag)
                {
                    flag=true;
                    shujv.setText("数据：AUTO");
                }
                else
                {
                    flag=false;
                    shujv.setText("数据：OFF");
                }

            }
        });
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if(flag)
                {
                    Api.getRequest(new myCallback() {
                        @Override
                        public void onSuccess(final String res) {
                            Log.e("GetSuccess", res);
                            Sensor data = new Gson().fromJson(res, Sensor.class);
                            hum=data.getHumidity();
                            tem=data.getTemperature();
                            //redvalue=data.getInfrared();
                            mq=data.getSmog();
                            shijian=data.getTime();
                            //此处加上判断条件
                            int warn=Integer.parseInt(tem);
                            if(warn>=20)
                            {
                                MediaPlayer.create(MyApplication.getAppContext(),R.raw.warn).start();
                                long[] pattern = {0, 800, 800, 800,800,800}; // OFF/ON/OFF/ON......
                                Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                                vibrator.vibrate(pattern, -1);
                            }
                            //  if(redvalue.equals("1")) redvalue=redvalue+" ——有人";
                            //else redvalue=redvalue+" ——无人";

                            mHandler.sendEmptyMessage(0);
                        }
                        @Override
                        public void onFailure(Exception e) {
                            showToastSync("网络连接失败");
                        }
                    });
                }

                System.out.println(flag);
            }
        };
        timer.schedule(timerTask,2000,5000);
        myContext=this;
    }

    public void showToast(String msg)
    {
        Toast.makeText(myContext,msg,Toast.LENGTH_SHORT).show();
    }
    public void showToastSync(String msg) {
        Looper.prepare();
        Toast.makeText(myContext, msg, Toast.LENGTH_SHORT).show();
        Looper.loop();
    }
}