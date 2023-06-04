package com.example.chat4me.messaging;

import android.database.Cursor;

import androidx.annotation.NonNull;

import java.util.Date;

/**
 * SmsMessage represents message data loaded from content://sms/[inbox|sent]
 */
public class SmsMessage {
    // columns from content://sms/[inbox|sent]
    private int _id;
    private int thread_id;
    private String address;
    private int person;
    private long date;
    private long date_sent;
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

    private static int getIntFromCursorColumnName(String colName, Cursor cur, int fallback) {
        int index = cur.getColumnIndex(colName);
        if(index > -1)
            return cur.getInt(index);
        return 0;
    }
    private static long getLongFromCursorColumnName(String colName, Cursor cur, long fallback) {
        int index = cur.getColumnIndex(colName);
        if(index > -1)
            return cur.getLong(index);
        return 0;
    }
    private static String getStringFromCursorColumnName(String colName, Cursor cur) {
        int index = cur.getColumnIndex(colName);
        if(index > -1)
            return cur.getString(index);
        return "";
    }

    public static SmsMessage readFromCursor(Cursor cur) {
        SmsMessage msg = new SmsMessage(
                getIntFromCursorColumnName("_id", cur, -1),
                getIntFromCursorColumnName("thread_id", cur, -1)
        );

        msg.address = getStringFromCursorColumnName("address", cur);
        msg.person = getIntFromCursorColumnName("person", cur, 0);
        msg.date = getLongFromCursorColumnName("date", cur, 0);
        msg.date_sent = getLongFromCursorColumnName("date_sent", cur, 0);
        msg.protocol = getIntFromCursorColumnName("protocol", cur, 0);
        msg.read = getIntFromCursorColumnName("read", cur, 0);
        msg.status = getIntFromCursorColumnName("status", cur, 0);
        msg.type = getIntFromCursorColumnName("type", cur, 0);
        msg.reply_path_present = getStringFromCursorColumnName("reply_path_present", cur) == "1"?1:0;
        msg.subject = getStringFromCursorColumnName("subject", cur);
        msg.body = getStringFromCursorColumnName("body", cur);
        msg.service_center = getStringFromCursorColumnName("service_center", cur);
        msg.locked = getIntFromCursorColumnName("locked", cur, 0);
        msg.sub_id = getIntFromCursorColumnName("sub_id", cur, 0);
        msg.error_code = getIntFromCursorColumnName("error_code", cur, 0);
        msg.creator = getStringFromCursorColumnName("creator", cur);
        msg.seen = getIntFromCursorColumnName("seen", cur, 0);
        return msg;
    }
    public SmsMessage(int id) {
        _id = id;
        thread_id = -1;
    }
    public SmsMessage(int id, int threadID) {
        _id = id;
        thread_id = threadID;
    }

    /**
     * getAddress returns the phone number
     * @return a phone number
     */
    public String getAddress() {
        return address;
    }

    /**
     * setAddress sets the phone number
     * @param address the address of the message receiver or sender
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * getBody returns the main message body
     * @return message body
     */
    public String getBody() {
        return body;
    }

    /**
     * setBody sets the message text to body
     * @param body the new message text
     */
    public void setBody(String body) {
        this.body = body;
    }

    public Date getDate() {
        return new Date(date);
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

    public int getThread_id() {
        return thread_id;
    }

    public void setThreadId(int thread_id) {
        this.thread_id = thread_id;
    }

}
