package com.example.billingbilling;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chanjun2016 on 16/11/27.
 */
public class AddAccountActivity extends Activity {

    private Button btn_commit;
    private Button btn_back;

    private EditText edit_description;
    private EditText edit_money;

    private Spinner spinner;

    private RadioGroup radioGroup;

    private String description;
    private double money;
    private int flag;
    private int cid;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_addaccount);

        btn_commit = (Button)findViewById(R.id.btn_commit);

        edit_description = (EditText)findViewById(R.id.edit_descriptipn);
        edit_money = (EditText)findViewById(R.id.edit_money);

        spinner = (Spinner)findViewById(R.id.spinner);

        radioGroup = (RadioGroup)findViewById(R.id.radio);

        final List<String> list1 = new ArrayList<String>();
        final List<String> list2 = new ArrayList<String>();

        final Map<String , Integer> map= new HashMap<String,Integer>();

        Map<Integer,AccountCategory> mapCategory = User.getInstance().getCategoryMap();

        for(Integer key : mapCategory.keySet()){
            if (mapCategory.get(key).getFlag() == 0){
                list1.add(mapCategory.get(key).getDescription());
            }else{
                list2.add(mapCategory.get(key).getDescription());
            }
            map.put(mapCategory.get(key).getDescription(),mapCategory.get(key).getCid());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list1);

        spinner.setAdapter(adapter);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbtn_pay){
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddAccountActivity.this,android.R.layout.simple_list_item_1,list1);

                    spinner.setAdapter(adapter);
                }else if (checkedId == R.id.rbtn_income){

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddAccountActivity.this,android.R.layout.simple_list_item_1,list2);

                    spinner.setAdapter(adapter);
                }
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("************",position+"");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btn_back = (Button)findViewById(R.id.btn_back);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(AddAccountActivity.this,TotalActivity.class);
//                intent.putExtra("tabId",1);
//
//                startActivity(intent);
                finish();
            }
        });

        btn_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                description = edit_description.getText().toString().trim();
                if (description.isEmpty()){
                    Toast.makeText(AddAccountActivity.this,"名称不能为空",Toast.LENGTH_LONG).show();
                    return;
                }

                if (description.length() > 20){
                    Toast.makeText(AddAccountActivity.this,"名称过长",Toast.LENGTH_LONG).show();
                    return;
                }

                String smoney = edit_money.getText().toString();
                if (smoney.length() == 0){
                    Toast.makeText(AddAccountActivity.this,"金额不能为空",Toast.LENGTH_LONG).show();
                    return;
                }

                money = Double.valueOf(smoney);

                if (money > 1000000 || money < 0){
                    Toast.makeText(AddAccountActivity.this,"金额过小或过大",Toast.LENGTH_LONG).show();
                    return;
                }

                flag = radioGroup.getCheckedRadioButtonId() == R.id.rbtn_income ? 1 : 0;

                try{
                    cid = map.get(spinner.getSelectedItem().toString());

                    date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());

                    Bundle data = new Bundle();

                    data.putString("json","{\"uid\":\"" + User.getInstance().getId()  +"\"," + "\"describe\":\"" + description + "\","
                            + "\"flag\":\"" + flag + "\"," + "\"money\":\"" + money + "\"," + "\"category\":\"" + cid + "\"}"
                    );
                    new MyHttpClient().connection("/billingbilling/api/addAccount",1,data,myHandler);
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(AddAccountActivity.this,"请选择正确的模块",Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    final Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case 0x1:
                    int id = msg.getData().getInt("id");
                    AccountItem item = new AccountItem(description,cid,money,date,User.getInstance());
                    User.getInstance().getAcntMap().put(id,item);
                    User.getInstance().calTotalMoney();
                    Toast.makeText(AddAccountActivity.this,"添加成功",Toast.LENGTH_LONG).show();
                    break;
                case 0x2:
                    Toast.makeText(AddAccountActivity.this,"添加失败",Toast.LENGTH_LONG).show();
                    break;
                default:
                    Toast.makeText(AddAccountActivity.this,"未知错误",Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };
}
