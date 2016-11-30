package com.example.billingbilling;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

/**
 * Created by chanjun2016 on 16/11/28.
 */
public class AddMoudleActivity extends Activity {

    private Button btn_commit;
    private Button btn_back;

    private EditText edit_name;

    private RadioGroup radioGroup;

    private String moduleName;
    private int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_addmodule);

        btn_commit = (Button)findViewById(R.id.btn_commit2);

        btn_back = (Button)findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        edit_name = (EditText)findViewById(R.id.edit_modulename);

        radioGroup = (RadioGroup)findViewById(R.id.radio2);

        btn_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                User user = User.getInstance();

                moduleName = edit_name.getText().toString().trim();

                if (moduleName.isEmpty()){
                    Toast.makeText(AddMoudleActivity.this,"模块名不能为空",Toast.LENGTH_LONG).show();
                    return;
                }

                if (moduleName.length() > 20){
                    Toast.makeText(AddMoudleActivity.this,"模块名过长",Toast.LENGTH_LONG).show();
                    return;
                }

                flag = radioGroup.getCheckedRadioButtonId() == R.id.rbtn_i ? 1 : 0;

                Bundle data = new Bundle();
                data.putString("json","{\"uid\":\"" + User.getInstance().getId() + "\"," + "\"category_name\":\"" + moduleName
                        + "\"," + "\"category_flag\":\"" + flag + "\"}"
                        );

                Log.d("^^^^^^^^^^^^^^^",data.getString("json"));

                new MyHttpClient().connection("/billingbilling/api/addModule",1,data,myHandler);

            }
        });

    }
    final Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case 0x1:
                    int id = msg.getData().getInt("id");
                    AccountCategory category = new AccountCategory(id,moduleName,User.getInstance(),flag);
                    User.getInstance().categoryMap.put(id,category);
                    Log.d("ADDMODULE cid =",id+"");

                    Toast.makeText(AddMoudleActivity.this,"添加成功",Toast.LENGTH_LONG).show();
                    break;
                case 0x2:
                    Toast.makeText(AddMoudleActivity.this,"添加失败，已存在同名模块",Toast.LENGTH_LONG).show();
                    break;
                case 0x3:
                    Toast.makeText(AddMoudleActivity.this,"添加失败，未知错误",Toast.LENGTH_LONG).show();
                    break;
                default:
                    Toast.makeText(AddMoudleActivity.this,"添加失败，未知错误",Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };
}
