package com.aia.utility;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.aia.interfaces.SmsListener;


public class SMSReceiver extends BroadcastReceiver
{

    public static final String TAG = SMSReceiver.class.getSimpleName();

    /*@Override
    public void onReceive(Context context, Intent intent)
    {

        StringTokenizer tokenizer = new StringTokenizer("AD-CTHPLA",",");
        Set<String> phoneEnrties = new HashSet<String>();
        while (tokenizer.hasMoreTokens())
        {
            phoneEnrties.add(tokenizer.nextToken().trim());
        }

        Bundle bundle = intent.getExtras();
        Object[] pdus = (Object[]) bundle.get("pdus");
        SmsMessage[] messages = new SmsMessage[pdus.length];
        for (int i = 0; i < messages.length; i++)
        {
            messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
            String address = messages[i].getOriginatingAddress();
            if (phoneEnrties.contains(address))
            {
//                Intent newintent = new Intent(context, SignUpActivity.class);
//                newintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Log.d(TAG,"Address="+address+" Message="+messages[i].getDisplayMessageBody());
                ShowMobileOtpScreen.OTP = messages[i].getDisplayMessageBody();

                Intent mIntent = new Intent(context, SelectLocationActivity.class);
                mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
//                newintent.putExtra("address", address);
//                newintent.putExtra("message",messages[i].getDisplayMessageBody());
//                context.startActivity(newintent);
            }
        }
    }*/

    private static SmsListener mListener = new SmsListener() {
        @Override
        public void messageReceived(String sender, String messageText) {

        }
    };

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle data  = intent.getExtras();

        Object[] pdus = (Object[]) data.get("pdus");

        for(int i=0; i < pdus.length; i++){
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);

            String sender = smsMessage.getDisplayOriginatingAddress();
            //You must check here if the sender is your provider and not another one with same text.

            String messageBody = smsMessage.getMessageBody();

            Log.d("Sender"+sender, "MessageBody"+messageBody);
            //Pass on the text to our listener.
            mListener.messageReceived(sender, messageBody);
        }

    }

    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }
}
