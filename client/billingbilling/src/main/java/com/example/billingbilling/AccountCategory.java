package com.example.billingbilling;

/**
 * Created by chanjun2016 on 16/11/26.
 */
public class AccountCategory {

    private int cid;

    private String description; //名称

    private User user;

    private int flag; //标志，0为支出，非0为收入

    public AccountCategory(){

    }

    public AccountCategory(int id,String name, User user,int flag){
        description = name;
        this.user = user;
        this.flag = flag;
        cid = id;

//        Log.d("name =============" , name);
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
