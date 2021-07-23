package com.upc.smartfamily;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import cn.leancloud.AVUser;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class LoginActivity extends Activity {

    public static final int REQUEST_CODE_REGISTER = 1;
    private Button btnLogin;
    private EditText etAccount,etPassword;
    private CheckBox cbRemember,cbAutoLogin;

    private  String userName = "linkexin";
    private  String pass = "1808040209";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //getSupportActionBar().setTitle("登录");
        setContentView(R.layout.activity_login); //实现跳转
        this.getWindow().setBackgroundDrawableResource(R.drawable.c);
        initView();
        inteData();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String account = etAccount.getText().toString();
                String password = etPassword.getText().toString();
                //Toast.makeText(LoginActivity.this,account,Toast.LENGTH_LONG).show();
                //Toast.makeText(LoginActivity.this,password,Toast.LENGTH_LONG).show();
                if(TextUtils.isEmpty(etAccount.getText())){
                    Toast.makeText(LoginActivity.this,"用户名不能为空",Toast.LENGTH_LONG).show();
                    return;
                }
                if(TextUtils.isEmpty(etPassword.getText())){
                    Toast.makeText(LoginActivity.this,"密码不能为空",Toast.LENGTH_LONG).show();
                    return;
                }
                else {
                    AVUser.logIn(account, password).subscribe(new Observer<AVUser>() {
                        public void onSubscribe(Disposable disposable) {
                        }

                        public void onNext(AVUser user) {
                            if(cbRemember.isChecked()){
                                SharedPreferences spf = getSharedPreferences("spfRecord",MODE_PRIVATE);
                                SharedPreferences.Editor edit = spf.edit();
                                edit.putString("account",account);
                                edit.putString("password",password);
                                edit.putBoolean("isRemember",true);
                                if(cbAutoLogin.isChecked()){
                                    edit.putBoolean("isLogin",true);
                                }else{
                                    edit.putBoolean("isLogin",false);
                                }
                                edit.apply();
                            }else{
                                SharedPreferences spf = getSharedPreferences("spfRecord",MODE_PRIVATE);
                                SharedPreferences.Editor edit = spf.edit();
                                edit.putBoolean("isRemember",false);
                                edit.apply();
                            }
                            MediaPlayer.create(MyApplication.getAppContext(),R.raw.win11).start();
                            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_LONG).show();
                            // 登录成功
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        }

                        public void onError(Throwable throwable) {
                            Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_LONG).show();
                            // 登录失败（可能是密码错误）
                        }

                        public void onComplete() {
                        }
                    });
                }
            }

        });
        cbAutoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    cbRemember.setChecked(true);
                }
            }
        });
        cbRemember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    cbAutoLogin.setChecked(false);
                }
            }
        });
    }
    private void inteData() {
        SharedPreferences spf = getSharedPreferences("spfRecord",MODE_PRIVATE);
        boolean isRemember = spf.getBoolean("isRemember",false);
        boolean isLogin = spf.getBoolean("isLogin",false);
        String account = spf.getString("account","");
        String password = spf.getString("password","");
        if(isLogin){
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            intent.putExtra("account",account);
            startActivity(intent);
            LoginActivity.this.finish();
        }
        userName = account;
        pass = password;


        if(isRemember){
            etAccount.setText(account);
            etPassword.setText(password);
            cbRemember.setChecked(true);
        }
    }
    private void initView(){
        btnLogin = findViewById(R.id.btn_login);
        etAccount = findViewById((R.id.et_account));
        etPassword = findViewById(R.id.et_password);
        cbRemember = findViewById(R.id.cb_remember);
        cbAutoLogin = findViewById(R.id.cb_auto_login);

    }
    public void toRegister(View view) {
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivityForResult(intent,REQUEST_CODE_REGISTER);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_REGISTER && resultCode == RegisterActivity.RESULT_CODE_REGISTER && data != null){
            Bundle extras = data.getExtras();
            String account = extras.getString("account", "");
            String password = extras.getString("password", "");
            etPassword.setText(account);
            etPassword.setText(password);
            userName = account;
            pass = password;
        }
    }
}