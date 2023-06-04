package com.example.chat4me.messaging;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.chat4me.ConversationsFragment;
import com.example.chat4me.MainActivity;

public class SmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            if (MainActivity.isAutoReply() == true) {
                Bundle bundle = intent.getExtras();
                SmsMessage[] msgs = null;
                if (bundle != null) {
                    //---retrieve the SMS message received---
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    msgs[0].getAddress();
                    ConversationsFragment.reply(msgs[0].getAddress());

                }
            }
        }
    }
}
