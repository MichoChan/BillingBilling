package com.example.billingbilling;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by chanjun2016 on 16/11/27.
 */
public class MyHttpClient {

    private String addressTarget = "http://10.82.82.107:8000";
    private String addressDebug = "http://10.0.2.2:5000";

    public MyHttpClient(){
          //get address from config file.
    }

    public MyHttpClient(String address) {
        addressTarget = address;
    }

    public void connection(String para, int method, Bundle data,Handler handler){
        HttpConnectThread connectThread = new HttpConnectThread(para,method,data,handler);
        connectThread.start();
    }

    private class HttpConnectThread extends Thread{
        private String urlparaString;
        private int method;
        Bundle data;
        private Handler handler;

        public HttpConnectThread(){
        }

        public HttpConnectThread(String para, int method, Handler handler){
            this.urlparaString = para;
            this.method = method;
            this.handler = handler;
        }

        public HttpConnectThread(String para, int method, Bundle data,Handler handler){
            this.urlparaString = para;
            this.method = method;
            this.data = data;
            this.handler = handler;
        }

        @Override
        public void run() {
            HttpURLConnection urlConnection = null;
            int status = -1;
            try {
                URL url = new URL(addressTarget + urlparaString);

                urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setDoOutput(true);   //需要输出
                urlConnection.setDoInput(true);   //需要输入
                urlConnection.setUseCaches(false);  //不允许缓存
                urlConnection.setRequestMethod("POST");   //设置POST方式连接
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
                urlConnection.setRequestProperty("Charset", "UTF-8");

                DataOutputStream dos = new DataOutputStream(urlConnection.getOutputStream());

                dos.writeBytes(data.getString("json"));
                dos.flush();
                dos.close();

                if (urlConnection.getResponseCode() == 200){

                    BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                    String ss = "", lines = "";

                    while ((lines = in.readLine()) != null) {
                        ss += lines;
                    }
                    Log.d("ResponseContent---->",ss);

                    JSONTokener jsonTokener = new JSONTokener(ss);
                    JSONObject result = (JSONObject)jsonTokener.nextValue();
                    status = result.getInt("status");

                    String action = result.getString("action");

                    if (status == 0x1){

                        if (action.equals("login")){
                            User.getInstance().setId(result.getInt("uid"));
                            User.getInstance().setName(result.getString("uname"));
                            User.getInstance().setPwd(data.getString("pwd"));

                            JSONArray jsonArray = (JSONArray) result.getJSONArray("data");
                            JSONArray jsonArrayCategory = (JSONArray)jsonArray.get(2);
                            Log.d("lengthCategory=========",jsonArrayCategory.length()+"");

                            for (int i = 0; i < jsonArrayCategory.length(); ++i){
                                JSONArray jarray = (JSONArray) jsonArrayCategory.get(i);
                                AccountCategory accountCategory = new AccountCategory(jarray.getInt(0),jarray.getString(1),
                                        User.getInstance(),jarray.getInt(2)
                                );
                                User.getInstance().getCategoryMap().put(jarray.getInt(0),accountCategory);
                            }

                            JSONArray jsonArrayPay = (JSONArray)jsonArray.get(0);
                            Log.d("lengthPay=========",jsonArrayPay.length()+"");
                            for (int i = 0; i < jsonArrayPay.length(); ++i){
                                JSONArray jarray = (JSONArray)jsonArrayPay.get(i);

                                AccountItem accountItem = new AccountItem(jarray.getString(1),jarray.getInt(5),
                                        jarray.getDouble(2),jarray.getString(3),User.getInstance()
                                        );

                                User.getInstance().getAcntMap().put(jarray.getInt(0),accountItem);
                            }

                            JSONArray jsonArrayIncome = (JSONArray)jsonArray.get(1);
                            Log.d("lengthIncome=========",jsonArrayIncome.length()+"");
                            for (int i = 0; i < jsonArrayIncome.length(); ++i){
                                JSONArray jarray = (JSONArray)jsonArrayIncome.get(i);
                                AccountItem accountItem = new AccountItem(jarray.getString(1),jarray.getInt(5),
                                        jarray.getDouble(2),jarray.getString(3),User.getInstance()
                                );

                                User.getInstance().getAcntMap().put(jarray.getInt(0),accountItem);
                            }

                            User.getInstance().calIncomeYesterday();
                            User.getInstance().calPayYesterday();
                            User.getInstance().calTotalMoney();

                        }else if (action.equals("register")){
                            User.getInstance().setId(result.getInt("uid"));
                            User.getInstance().setName(result.getString("uname"));
                            User.getInstance().setTotalMoney(0.00);

                        }else if (action.equals("addModule")){
                            int id = result.getInt("cid");
                            Bundle data = new Bundle();
                            data.putInt("id",id);
                            Message message = new Message();
                            message.what = status;
                            message.setData(data);
                            handler.sendMessage(message);
                            return ;
                        }else if (action.equals("addAccount")){
                            int id = result.getInt("id");
                            Log.d("addAccount ID =======",id+"");
                            Bundle data = new Bundle();
                            data.putInt("id",id);
                            Message message = new Message();
                            message.what = status;
                            message.setData(data);
                            handler.sendMessage(message);
                            return ;
                        }
                    }
                    handler.sendEmptyMessage(status);
                }else{
                    Log.d("HttpConnection--->",urlConnection.getResponseMessage());
                    handler.sendEmptyMessage(status);
                }
            }
            catch (Exception e){
                Log.d("Exception----->",e.toString());
                handler.sendEmptyMessage(status);
            }
            finally {
                if (urlConnection != null){
                    urlConnection.disconnect();
                }
            }
        }

        public void setData(Bundle data) {
            this.data = data;
        }

        public void setMethod(int method) {
            this.method = method;
        }

        public void setUrlparaString(String urlparaString) {
            this.urlparaString = urlparaString;
        }
    }
}