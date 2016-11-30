package com.example.billingbilling;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by chanjun2016 on 16/11/27.
 */
public class RegisterActivity extends Activity {


    private EditText edittextUser;
    private EditText editTextPwd;

    private Button btnRegister;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        editTextPwd = (EditText)findViewById(R.id.edit_rpasswd);
        edittextUser = (EditText)findViewById(R.id.edit_ruser);
        btnRegister = (Button)findViewById(R.id.btn_register);
        btnBack = (Button)findViewById(R.id.btn_back);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edittextUser.getText().toString().trim();
                String pwd = editTextPwd.getText().toString().trim();

                if (name.isEmpty() || pwd.isEmpty()){
                    Toast.makeText(RegisterActivity.this,"名称和密码不能为空",Toast.LENGTH_LONG).show();
                }else{
                    if (name.length() > 12 || pwd.length() > 12){
                        Toast.makeText(RegisterActivity.this,"名称和密码不能超过12个字符",Toast.LENGTH_LONG).show();
                    } else if (name.length() < 6 || pwd.length() < 6) {
                        Toast.makeText(RegisterActivity.this,"名称和密码不能小于6个字符",Toast.LENGTH_LONG).show();
                    } else {
                        Bundle data = new Bundle();
                        data.putString("json", "{\"uname\":\"" + name + "\"," + "\"pwd\":\"" + pwd + "\"}");
                        data.putString("pwd", pwd);
                        new MyHttpClient().connection("/billingbilling/api/register", 0, data, myHandler
                        );
                    }
                }
            }
        });
    }

    final Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0x1:
                    Intent intent = new Intent(RegisterActivity.this,TotalActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case 0x2:
                    Toast.makeText(RegisterActivity.this,"注册失败，已存在账户名",Toast.LENGTH_LONG).show();
                    break;
                case 0x3:
                    Toast.makeText(RegisterActivity.this,"注册失败，未知错误",Toast.LENGTH_LONG).show();
                    break;
                default:
                    Toast.makeText(RegisterActivity.this,"注册失败，未知错误",Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };
}