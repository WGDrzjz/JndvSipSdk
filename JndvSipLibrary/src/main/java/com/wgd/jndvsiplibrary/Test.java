package com.wgd.jndvsiplibrary;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.ehome.manager.utils.JNLogUtil;
import com.ehome.manager.utils.JNPjSipConstants;
import com.ehome.manager.utils.JNSpUtils;
import com.ehome.sipservice.SipAccountData;
import com.ehome.sipservice.SipServiceCommand;

/**
 * Author: wangguodong
 * Date: 2022/10/7
 * QQ: 1772889689@qq.com
 * WX: gdihh8180
 * Description:
 */
public class Test extends Activity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sipRegister();
    }

    //注册
    public void sipRegister() {
        SipServiceCommand.setSipStackLogLevel(this, 5);
        SipAccountData mSipAccount = new SipAccountData();
        mSipAccount.setHost("")
                    .setPort(5060)
                    .setUsername("user")
                    .setPassword("psw")
                    .setRealm("sipRealm")
                    .setAuthenticationType(SipAccountData.AUTH_TYPE_DIGEST);
        SipServiceCommand.setAccount(this, mSipAccount);
    }

}
