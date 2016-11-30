package com.example.billingbilling;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chanjun2016 on 16/11/26.
 */
public class User {

    private int id; //用户id
    private String name; //用户昵称
    private String pwd;  //用户密码
    private double totalMoney;//用户余额

    private double incomeYesterday;//昨日收入
    private double payYesterday;   //昨日支出

    Map<Integer,AccountItem> acntMap;
    Map<Integer,AccountCategory> categoryMap;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(double totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    private static User _user = null;

    private User(){
        acntMap = new HashMap<Integer, AccountItem>();
        categoryMap = new HashMap<Integer, AccountCategory>();
    }

    public static User getInstance() {
        if (_user == null) {
            return _user = new User();
        }
        return _user;
    }

    public Map<Integer, AccountItem> getAcntMap() {
        return acntMap;
    }

    public void setAcntMap(Map<Integer, AccountItem> acntMap) {
        this.acntMap = acntMap;
    }

    public Map<Integer, AccountCategory> getCategoryMap() {
        return categoryMap;
    }

    public void setCategoryMap(Map<Integer, AccountCategory> categoryMap) {
        this.categoryMap = categoryMap;
    }

    public void calIncomeYesterday(){

        double sum = 0;

        for(Integer key : acntMap.keySet()){
            if (categoryMap.get(acntMap.get(key).getCategory()).getFlag() == 1 ){
                if (isYesterDay(acntMap.get(key).getDate()))
                    sum += acntMap.get(key).getMoney();
            }
        }

        incomeYesterday = sum;
    }

    private boolean isYesterDay(String sdate){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = null;
        try {
            date1 = sdf.parse(sdate);
        }catch (ParseException e){
            e.printStackTrace();
        }

        if (date1 == null){
            return  false;
        }

        Date date2 = Calendar.getInstance().getTime();

        Calendar aCalendar = Calendar.getInstance();

        aCalendar.setTime(date2);

        int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);

        aCalendar.setTime(date1);

        int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);

        return day2-day1 == 1;
    }

    public void calPayYesterday() {
        double sum = 0;

        for(Integer key : acntMap.keySet()){
            if (categoryMap.get(acntMap.get(key).getCategory()).getFlag() == 0){
                if (isYesterDay(acntMap.get(key).getDate()))
                    sum += acntMap.get(key).getMoney();
                Log.d("date------------>",acntMap.get(key).getDate());
            }
        }

        payYesterday = sum;
    }

    public void calTotalMoney(){

        double sum = 0;

        for(Integer key : acntMap.keySet()){
            if (categoryMap.get(acntMap.get(key).getCategory()).getFlag() == 0){
                sum -= acntMap.get(key).getMoney();
            }else{
                sum += acntMap.get(key).getMoney();
            }
        }

        totalMoney = sum;
    }

    public double getIncomeYesterday() {
        return incomeYesterday;
    }

    public void setIncomeYesterday(double incomeYesterday) {
        this.incomeYesterday = incomeYesterday;
    }

    public double getPayYesterday() {
        return payYesterday;
    }

    public void setPayYesterday(double payYesterday) {
        this.payYesterday = payYesterday;
    }

    public void reset(){
        acntMap.clear();
        categoryMap.clear();
    }
}