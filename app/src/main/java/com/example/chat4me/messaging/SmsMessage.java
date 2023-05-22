package com.example.chat4me.messaging;

import android.database.Cursor;
import android.telephony.PhoneNumberUtils;

import androidx.annotation.NonNull;

/**
 * SmsMessage represents message data loaded from content://sms/[inbox|sent]
 */
public class SmsMessage {
    // columns from content://sms/[inbox|sent]
    private int _id;
    private int thread_id;
    private String address;
    private int person;
    private String date;
    private String date_sent;
    private int protocol;
    private int read;
    private int status;
    private int type;
    private int reply_path_present;
    private String subject;
    private String body;
    private String service_center;
    private int locked;
    private int sub_id;
    private int error_code;
    private String creator;
    private int seen;

    private static String getStringFromCursorColumnName(String colName, Cursor cur) {
        int index = cur.getColumnIndex(colName);
        return cur.getString(index);
    }

    public static SmsMessage readFromCursor(Cursor cur) {
        SmsMessage msg = new SmsMessage();

        msg._id = Integer.parseInt(getStringFromCursorColumnName("_id", cur));
        msg.thread_id = Integer.parseInt(getStringFromCursorColumnName("thread_id", cur));
        msg.address = getStringFromCursorColumnName("address", cur);
        String tmp = getStringFromCursorColumnName("person", cur);
        try {
            msg.person = Integer.parseInt(tmp);
        } catch (NumberFormatException e) {
            msg.person = 0;
        }
        msg.date = getStringFromCursorColumnName("date", cur);
        msg.date_sent = getStringFromCursorColumnName("date_sent", cur);
        try {
            msg.protocol = Integer.parseInt(getStringFromCursorColumnName("protocol", cur));
        } catch (NumberFormatException e) {
            msg.protocol = 0;
        }
        try {
            msg.read = Integer.parseInt(getStringFromCursorColumnName("read", cur));
        } catch (NumberFormatException e) {
            msg.read = 0;
        }
        msg.status = Integer.parseInt(getStringFromCursorColumnName("status", cur));
        msg.type = Integer.parseInt(getStringFromCursorColumnName("type", cur));
        msg.reply_path_present = getStringFromCursorColumnName("reply_path_present", cur) == "1"?1:0;
        msg.subject = getStringFromCursorColumnName("subject", cur);
        msg.body = getStringFromCursorColumnName("body", cur);
        msg.service_center = getStringFromCursorColumnName("service_center", cur);
        try {
            msg.locked = Integer.parseInt(getStringFromCursorColumnName("locked", cur));
        } catch (NumberFormatException e) {
            msg.locked = 0;
        }
        msg.sub_id = Integer.parseInt(getStringFromCursorColumnName("sub_id", cur));
        msg.error_code = Integer.parseInt(getStringFromCursorColumnName("error_code", cur));
        msg.creator = getStringFromCursorColumnName("creator", cur);
        try {
            msg.seen = Integer.parseInt(getStringFromCursorColumnName("seen", cur));
        } catch (NumberFormatException e) {
            msg.seen = 0;
        }
        return msg;
    }

    /**
     * getAddress returns the phone number
     * @return a phone number
     */
    public String getAddress() {
        return address;
    }

    /**
     * getBody returns the main message body
     * @return message body
     */
    public String getBody() {
        return body;
    }

    /**
     * isRead returns true if the message has been marked as read, otherwise false
     * @return the read status
     */
    public boolean isRead() {
        return read == 1;
    }

    @NonNull
    @Override
    public String toString() {
        return address + ":" + ((body != null)?body:"");
    }
}
