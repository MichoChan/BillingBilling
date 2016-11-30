package com.example.billingbilling;

/**
 * Created by chanjun2016 on 16/11/27.
 */
public class ShareModule {

    private static int connectFlag;

    public static int getConnectFlag() {
        return connectFlag;
    }

    public static void setConnectFlag(int connectFlag) {
        ShareModule.connectFlag = connectFlag;
    }
}
