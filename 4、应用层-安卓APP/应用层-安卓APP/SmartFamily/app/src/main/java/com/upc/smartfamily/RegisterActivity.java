package com.upc.smartfamily;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.leancloud.AVUser;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class RegisterActivity extends Activity implements View.OnClickListener {

    public static final int RESULT_CODE_REGISTER = 0;
    private Button btnRegister;
    private EditText etAccount,etPass,etPasswordConfirm;
    private CheckBox cbAgree;
    TextView xieyi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        etAccount = findViewById(R.id.et_account);
        etPass = findViewById(R.id.et_password);
        etPasswordConfirm = findViewById(R.id.et_password_confirm);
        cbAgree = findViewById(R.id.cb_agree);
        btnRegister = findViewById(R.id.btn_registor);
        xieyi = findViewById(R.id.xieyi);
        btnRegister.setOnClickListener(this);
        SpannableString spannableString1=new SpannableString(xieyi.getText());
        spannableString1.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Uri uri2 = Uri.parse("https://yuyang.cyou/xieyi.html");
                Intent intent2 = new Intent(Intent.ACTION_VIEW, uri2);
                startActivity(intent2);
            }
        }, 0, xieyi.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString1.setSpan(new ForegroundColorSpan(Color.rgb(0, 51, 255)), 0, 8, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        xieyi.setText(spannableString1);
        xieyi.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void onClick(View v) {
        String name = etAccount.getText().toString();
        String pass = etPass.getText().toString();
        String passConfirm = etPasswordConfirm.getText().toString();

        if(TextUtils.isEmpty(name)){
            Toast.makeText(RegisterActivity.this,"用户名不能为空",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(pass)){
            Toast.makeText(RegisterActivity.this,"密码不能为空",Toast.LENGTH_LONG).show();
            return;
        }
        if(!TextUtils.equals(pass,passConfirm)){
            Toast.makeText(RegisterActivity.this,"密码不一致",Toast.LENGTH_LONG).show();
            return;
        }
        if(!cbAgree.isChecked()){
            Toast.makeText(RegisterActivity.this,"请注意同意协议",Toast.LENGTH_LONG).show();
            return;
        }
        AVUser user = new AVUser();
        user.setUsername(name);
        user.setPassword(pass);
        user.put("gender", "secret");
        user.signUpInBackground().subscribe(new Observer<AVUser>() {
            public void onSubscribe(Disposable disposable) {}
            public void onNext(AVUser user) {
                MediaPlayer.create(MyApplication.getAppContext(),R.raw.win11).start();
                // 注册成功
                Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_LONG).show();
            }
            public void onError(Throwable throwable) {
                MediaPlayer.create(MyApplication.getAppContext(),R.raw.win11).start();
                // 注册失败（通常是因为用户名已被使用）
                Toast.makeText(RegisterActivity.this,"用户名已经存在",Toast.LENGTH_LONG).show();
            }
            public void onComplete() {}
        });
        finish();
    }
}