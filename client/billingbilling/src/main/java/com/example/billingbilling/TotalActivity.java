package com.example.billingbilling;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

/**
 * Created by chanjun2016 on 16/11/27.
 */
public class TotalActivity extends FragmentActivity {

    private FragmentTabHost mTabHost;

    private Class fragmentArray[] ={StatisticFragment.class,AccountListFragment.class,MyselfFragment.class};

    private LayoutInflater layoutInflater;

    private int mImageViewArray[] ={
            R.drawable.statistic,
            R.drawable.bill,
            R.drawable.me,
            R.drawable.statistic_b,
            R.drawable.bill_b,
            R.drawable.me_b,
    };

    private String mTextviewArray[] = {"统计", "账目", "我的"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total);

        layoutInflater = LayoutInflater.from(this);

        mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);

        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        int count = fragmentArray.length;

        for(int i = 0; i < count; i++){
            //为每一个Tab按钮设置图标、文字和内容
            TabSpec tabSpec = mTabHost.newTabSpec(mTextviewArray[i]).setIndicator(getTabItemView(i));

            //将Tab按钮添加进Tab选项卡中
            mTabHost.addTab(tabSpec, fragmentArray[i], null);
            //设置Tab按钮的背景
            mTabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.rgb(0xe6, 0xe6, 0xe6));
        }
//        mTabHost.setBackgroundColor(Color.rgb(0xdb, 0xdb, 0xdb));

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (tabId.equals(mTextviewArray[0])){
                    ImageView imageView = (ImageView) mTabHost.getTabWidget().getChildAt(0).findViewById(R.id.imageview);
                    imageView.setImageResource(mImageViewArray[3]);

                    imageView = (ImageView) mTabHost.getTabWidget().getChildAt(1).findViewById(R.id.imageview);
                    imageView.setImageResource(mImageViewArray[1]);

                    imageView = (ImageView) mTabHost.getTabWidget().getChildAt(2).findViewById(R.id.imageview);
                    imageView.setImageResource(mImageViewArray[2]);

                }else if (tabId.equals(mTextviewArray[1])){

                    ImageView imageView = (ImageView) mTabHost.getTabWidget().getChildAt(1).findViewById(R.id.imageview);
                    imageView.setImageResource(mImageViewArray[4]);

                    imageView = (ImageView) mTabHost.getTabWidget().getChildAt(0).findViewById(R.id.imageview);
                    imageView.setImageResource(mImageViewArray[0]);

                    imageView = (ImageView) mTabHost.getTabWidget().getChildAt(2).findViewById(R.id.imageview);
                    imageView.setImageResource(mImageViewArray[2]);


                }else if (tabId.equals(mTextviewArray[2])){

                    ImageView imageView = (ImageView) mTabHost.getTabWidget().getChildAt(2).findViewById(R.id.imageview);
                    imageView.setImageResource(mImageViewArray[5]);

                    imageView = (ImageView) mTabHost.getTabWidget().getChildAt(1).findViewById(R.id.imageview);
                    imageView.setImageResource(mImageViewArray[1]);

                    imageView = (ImageView) mTabHost.getTabWidget().getChildAt(0).findViewById(R.id.imageview);
                    imageView.setImageResource(mImageViewArray[0]);

                }
            }
        });

        //mTabHost.setOnTabChangedListener();
    }

    private View getTabItemView(int index) {
        //LayoutInflater是用来找res/layout/下的xml布局文件
        View view = layoutInflater.inflate(R.layout.tab_item_view, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.imageview);

        if (index == 0){
            imageView.setImageResource(mImageViewArray[3]);
        }else {
            imageView.setImageResource(mImageViewArray[index]);
        }
        TextView textView = (TextView) view.findViewById(R.id.textview);
        textView.setText(mTextviewArray[index]);

        return view;
    }

    @Override
    protected void onResume() {
        super.onResume();




//        Intent intent = getIntent();
//        mTabHost.setCurrentTab(intent.getIntExtra("tabid",0));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0){
            if (resultCode == 0){
//                View view = mTabHost.getCurrentView();
//
//                ListView listView = (ListView)view.findViewById(R.id.list_account);
//                listView.getSelectedItem();
            }
        }

    }
}