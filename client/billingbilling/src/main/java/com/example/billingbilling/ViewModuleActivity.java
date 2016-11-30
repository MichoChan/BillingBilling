package com.example.billingbilling;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by chanjun2016 on 16/11/28.
 */
public class ViewModuleActivity extends Activity {

    private Button btn_back;
    private TextView textTitle;
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_viewmodule);

        Intent intent = getIntent();
        int flag = intent.getIntExtra("flag",-1);

        List<String> list = new ArrayList<String>();

        Map<Integer,AccountCategory> mapCategory = User.getInstance().getCategoryMap();

        for(Integer key : mapCategory.keySet()){
            if (mapCategory.get(key).getFlag() == flag){
                list.add(mapCategory.get(key).getDescription());
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list);

        listView = (ListView)findViewById(R.id.list_module);

        listView.setAdapter(adapter);

        btn_back = (Button)findViewById(R.id.btn_back);
        textTitle = (TextView)findViewById(R.id.textview_title);

        textTitle.setText(flag == 0 ? "支出模块" :"收入模块");

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent1 = new Intent(ViewModuleActivity.this,TotalActivity.class);
//                intent1.putExtra("tabid",2);
//                startActivity(intent1);
                finish();
            }
        });
    }
}
