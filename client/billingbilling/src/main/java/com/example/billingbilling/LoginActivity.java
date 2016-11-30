package com.example.billingbilling;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by chanjun2016 on 16/11/27.
 */
public class LoginActivity extends Activity {

    private EditText edittextUser;
    private EditText editTextPwd;

    private Button btnLogin;

    private TextView textRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        //检测网络状态
        if (isNetworkConnected(this)){
            Log.d("NetWorkState--------->","OK");

            Bundle data = new Bundle();
            data.putString("json","{}");
            new MyHttpClient().connection("/",1,data,myHandler2);
        }else{
            Log.d("NetWorkState--------->","Disconnect");
            Toast.makeText(this,"NetWork are unavailable!",Toast.LENGTH_LONG).show();
        }

        btnLogin = (Button)findViewById(R.id.btn_login);
        edittextUser = (EditText)findViewById(R.id.edit_user);
        editTextPwd = (EditText)findViewById(R.id.edit_passwd);
        textRegister = (TextView)findViewById(R.id.hint_register);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = edittextUser.getText().toString().trim();
                String pwd = editTextPwd.getText().toString().trim();
                if (name.isEmpty() || pwd.isEmpty()){
                    Toast.makeText(LoginActivity.this,"名字或者密码不能为空",Toast.LENGTH_LONG).show();
                }else{

                    Bundle data = new Bundle();
                    data.putString("json","{\"uname\":\"" + name + "\"," + "\"pwd\":\"" + pwd + "\"}");
                    data.putString("pwd",pwd);

                    new MyHttpClient().connection("/billingbilling/api/login"
                            ,1,data,myHandler);
                }
            }
        });

        textRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    final Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0x1:

                    SharedPreferences preferences;
                    SharedPreferences.Editor editor;

                    preferences = getSharedPreferences("billingbilling",MODE_PRIVATE);
                    editor = preferences.edit();

                    editor.putString("uname",User.getInstance().getName());
                    editor.putString("upwd",User.getInstance().getPwd());
                    editor.commit();

                    Intent intent = new Intent(LoginActivity.this,TotalActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case 0x2:
                    Toast.makeText(LoginActivity.this,"登录失败，账户不存在",Toast.LENGTH_LONG).show();
                    break;
                case 0x3:
                    Toast.makeText(LoginActivity.this,"登录失败，密码错误",Toast.LENGTH_LONG).show();
                    break;
                case 0x4:
                    Toast.makeText(LoginActivity.this,"登录失败，未知错误",Toast.LENGTH_LONG).show();
                    break;
                default:
                    Toast.makeText(LoginActivity.this,"登录失败，未知错误",Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return true;
            }
        }
        return false;
    }

    final Handler myHandler2 = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //super.handleMessage(msg);
            if (msg.what == 0x1){
            }else{
                Log.d("NetWorkState--------->","Error");
                Toast.makeText(LoginActivity.this,"Can't connect to server!",Toast.LENGTH_LONG).show();
            }
        }
    };
}
