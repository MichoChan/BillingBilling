package com.example.billingbilling;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

        SharedPreferences preferences = getSharedPreferences("billingbilling",MODE_PRIVATE);
        if (preferences.getString("uname","").equals("")){
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
            finish();
        }else{
            String name = preferences.getString("uname","");
            String pwd = preferences.getString("upwd","");

            Bundle data = new Bundle();
            data.putString("json","{\"uname\":\"" + name + "\"," + "\"pwd\":\"" + pwd + "\"}");
            data.putString("pwd",pwd);

            new MyHttpClient().connection("/billingbilling/api/login"
                    ,1,data,myHandler);
        }
    }

    final Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Intent intent;
            switch (msg.what){
                case 0x1:
                    intent = new Intent(MainActivity.this,TotalActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case 0x2:
                    intent = new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case 0x3:
                    intent = new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case 0x4:
                    intent = new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }
        }
    };
}
