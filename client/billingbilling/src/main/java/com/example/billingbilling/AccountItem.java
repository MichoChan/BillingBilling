package com.example.billingbilling;

/**
 * Created by chanjun2016 on 16/11/26.
 */
public class AccountItem {

    private String description;//名称

    private int category;

    private double money;//金额

    private String date; //日期

    private User user;

    public  AccountItem(){

    }

    public AccountItem(String description, int category, double money,String date, User user){
        this.description = description;
        this.category = category;
        this.money = money;
        this.date = date;
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String  date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }
}