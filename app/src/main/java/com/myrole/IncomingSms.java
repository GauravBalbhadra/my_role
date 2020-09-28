package com.myrole;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

/**
 * Created by welcome on 07-06-2017.
 */

public class IncomingSms extends BroadcastReceiver
{
//    @Override
//    public void onReceive(Context context, Intent intent)
//    {
//
//        final Bundle bundle = intent.getExtras();
//        try {
//            if (bundle != null)
//            {
//                final Object[] pdusObj = (Object[]) bundle.get("pdus");
//                for (int i = 0; i < pdusObj .length; i++)
//                {
//                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[])                                                                                                    pdusObj[i]);
//                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
//                    String senderNum = phoneNumber ;
//                    String message = currentMessage .getDisplayMessageBody();
//                    try
//                    {
//                        if (senderNum .equals("AD-VERIFY"))
//                        {
////                            Otp Sms = new Otp();
////                            Sms.recivedSms(message );
//                            OTPActivity Sms = new OTPActivity();
//                            Sms.recivedSms(message );
//                        }
//                    }
//                    catch(Exception e){}
//
//                }
//            }
//
//        } catch (Exception e)
//        {
//
//        }
//    }
   //  Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();

    public void onReceive(Context context, Intent intent) {

        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    String senderNum = phoneNumber;
                 //   String message = currentMessage.getDisplayMessageBody().split(":")[1];
                    String message = currentMessage.getDisplayMessageBody();

//                    if (senderNum .equals("AD-VERIFY"))
//                        {
//                            String otp=message.substring(0, 6);
////                            Otp Sms = new Otp();
////                            Sms.recivedSms(message );
//                            OTPActivity Sms = new OTPActivity();
//                            Sms.recivedSms(otp);
//                        }

                    message = message.substring(0, message.length()-1);
                    Log.i("SmsReceiver", "senderNum: " + senderNum + "; message: " + message);
                    String senderNumcut=senderNum.substring(3, 9);

                    Log.i("SmsReceiver", "senderNum cut: " + senderNumcut );


                    if (senderNumcut .equals("MYROLE"))
                    {
                        String otp_code=message.substring(0, 6);
                        Intent myIntent = new Intent("otp");
                        myIntent.putExtra("message",otp_code);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(myIntent);
                    }


//                    if (senderNum .equals("AD-VERIFY"))
//                        {
//                            String otp_code=message.substring(0, 6);
//                            Intent myIntent = new Intent("otp");
//                            myIntent.putExtra("message",otp_code);
//                            LocalBroadcastManager.getInstance(context).sendBroadcast(myIntent);
//                        }else  if (senderNum .equals("AM-VERIFY"))
//                    {
//                        String otp_code=message.substring(0, 6);
//                        Intent myIntent = new Intent("otp");
//                        myIntent.putExtra("message",otp_code);
//                        LocalBroadcastManager.getInstance(context).sendBroadcast(myIntent);
//                    }else  if (senderNum .equals("AK-VERIFY"))
//                    {
//                        String otp_code=message.substring(0, 6);
//                        Intent myIntent = new Intent("otp");
//                        myIntent.putExtra("message",otp_code);
//                        LocalBroadcastManager.getInstance(context).sendBroadcast(myIntent);
//                    }
//                    String otp_code=message.substring(0, 6);
//                    Intent myIntent = new Intent("otp");
//                    myIntent.putExtra("message",otp_code);
//                    LocalBroadcastManager.getInstance(context).sendBroadcast(myIntent);
                    // Show Alert

                } // end for loop
            } // bundle is null

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);

        }
    }

}
