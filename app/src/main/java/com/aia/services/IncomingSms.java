package com.aia.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;


/**
 * Created by User on 31-08-2016.
 */

public class IncomingSms extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {

        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null)
            {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                String format = bundle.getString("format");
                for (int i = 0; i < pdusObj.length; i++)
                {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    String senderNum = phoneNumber ;
                    String message = currentMessage.getDisplayMessageBody();
                    try
                    {
                       /* ShowMobileOtpScreen Sms = new ShowMobileOtpScreen();
                        Sms.recivedSms(message);*/
                    }
                    catch(Exception e){}
                }
            }
        } catch (Exception e) {}
    }


/*
    private String TAG = IncomingSms.class.getSimpleName();

    public IncomingSms() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // Get the data (SMS data) bound to intent
        Bundle bundle = intent.getExtras();

        SmsMessage[] msgs = null;

        String str = "";

        if (bundle != null){
            // Retrieve the Binary SMS data
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];

            // For every SMS message received (although multipart is not supported with binary)
            for (int i=0; i<msgs.length; i++) {
                byte[] data = null;

                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);

                str += "Binary SMS from " + msgs[i].getOriginatingAddress() + " :";

                str += "\nBINARY MESSAGE: ";

                // Return the User Data section minus the
                // User Data Header (UDH) (if there is any UDH at all)
                data = msgs[i].getUserData();

                // Generally you can do away with this for loop
                // You'll just need the next for loop
                for (int index=0; index < data.length; index++) {
                    str += Byte.toString(data[index]);
                }

                str += "\nTEXT MESSAGE (FROM BINARY): ";

                for (int index=0; index < data.length; index++) {
                    str += Character.toString((char) data[index]);
                }

                str += "\n";
            }

            // Dump the entire message
             Toast.makeText(context, str, Toast.LENGTH_LONG).show();
            Log.d(TAG, str);
        }
    }*/
}

