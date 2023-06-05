package com.example.chat4me.messaging;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;

import com.example.chat4me.ConversationViewFragment;
import com.example.chat4me.MainActivity;

public class SmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
            if (MainActivity.isAutoReply() == true) {
                Bundle bundle = intent.getExtras();
                SmsMessage[] msgs = null;
                if (bundle != null) {
                    //---retrieve the SMS message received---
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    newInstance(msgs[0].getAddress());

                    }
                }
            }
        }
    public static ConversationViewFragment newInstance(String address) {
        ConversationViewFragment f = new ConversationViewFragment();
        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putString("address", address);
        args.putBoolean("true",true);
        f.setArguments(args);
        return f;
    }
}

