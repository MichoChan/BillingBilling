package com.example.billingbilling;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by chanjun2016 on 16/11/28.
 */
public class StatisticFragment extends Fragment {

    private TextView textViewTotal;
    private TextView textViewIncome;
    private TextView textViewPay;
    private LineChartView lineChartView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_statistics,container,false);

        textViewIncome = (TextView)rootView.findViewById(R.id.textview_income);

        textViewPay = (TextView)rootView.findViewById(R.id.textview_pay);

        textViewTotal = (TextView)rootView.findViewById(R.id.textview_totalmoney);

        String sign = User.getInstance().getTotalMoney() > 0 ? "+": "";
        textViewTotal.setText(sign+User.getInstance().getTotalMoney());

        textViewPay.setText("-"+User.getInstance().getPayYesterday());
        textViewIncome.setText("+"+User.getInstance().getIncomeYesterday());

        lineChartView = (LineChartView)rootView.findViewById(R.id.linechart);

        List<Double> payList = new ArrayList<Double>();

        List<String> dateList = new ArrayList<String>();

        String dateNow =  new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());

        for (int i = 0; i < 7; ++i){
            String tmp = getSpecifiedDayBefore(dateNow);
            dateList.add(0,getMMDD(tmp));
            dateNow = tmp;
        }

        double value[] = new double[7];
        final Map<Integer,AccountItem> map = User.getInstance().getAcntMap();
        for (Integer key : map.keySet()) {
            if (User.getInstance().getCategoryMap().get(map.get(key).getCategory()).getFlag() == 0) {
                Log.d("^^^^^^^^^^^^^^",map.get(key).getMoney()+"");
                int deleta = distanceOfTwoDate(map.get(key).getDate());
                Log.d("--------------",deleta+"");
                if (deleta > 0 && deleta <= 7) {
                    value[deleta-1] += map.get(key).getMoney();
                    Log.d("+++++++++++++",map.get(key).getMoney()+"");
                }
            }
        }

        for(int i = 6; i >= 0; --i){
            payList.add(value[i]);
            Log.d("--------------",value[i]+"");
        }

        lineChartView.setData(payList,dateList,false);

        return rootView;
    }

    private int distanceOfTwoDate(String sdate){

        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd");
        Date date1 = null;
        try {
            date1 = sdf.parse(sdate);
        }catch (ParseException e){
            e.printStackTrace();
        }

        if (date1 == null){
            return -1;
        }

        Date date2 = Calendar.getInstance().getTime();

        Calendar aCalendar = Calendar.getInstance();

        aCalendar.setTime(date1);

        int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);

        aCalendar.setTime(date2);

        int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);

        return day2-day1;
    }

    private String getSpecifiedDayBefore(String specifiedDay) {
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day - 1);

        String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c
                .getTime());
        return dayBefore;
    }

    private String getMMDD(String date){

        int j = -1;
        for (int i = 0; i < date.length(); ++i){
            if (date.charAt(i) == '-'){
                j = i;
                break;
            }
        }

        String rlt = "";
        if (j > -1){
            rlt += date.substring(j+1);
        }

        return rlt;
    }

}