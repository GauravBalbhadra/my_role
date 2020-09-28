package com.myrole.firebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Looper;

import android.text.TextUtils;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.myrole.R;
import com.myrole.dashboard.MainDashboardActivity;
import com.myrole.utils.AppPreferences;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by Bekground on 25-01-2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private static final int NOTIFICATION_ID = 1;
    private static final String NOTIFICATION_CHANNEL_ID = "my_notification_channel";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    // @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData().toString());
            String message = remoteMessage.getData().get("message");
            String type = remoteMessage.getData().get("type");
            String title = remoteMessage.getData().get("title");
            String type_id = remoteMessage.getData().get("type_id");
            String image = remoteMessage.getData().get("image");
//            String link = remoteMessage.getData().get("link");
            sendNotificationyo(message, type, title, type_id, "");
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());


        }
        AppPreferences.getAppPreferences(getApplicationContext()).saveBadgeCount((AppPreferences.getAppPreferences(this).fetchBadgeCount() + 1));
        // sendNotification(remoteMessage.getData());

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received FCM message.
     * <p>
     * //  * @param data FCM message Data received.
     */

    private void sendNotificationyo(final String message, String type, String title, String type_id, final String image) {

        final Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        PendingIntent pInetent = null;

        // final int NOTIFY_ID = 1002;
        int NOTIFY_ID = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);

        String name = "my_package_channel";
        String id = "my_package_channel_1"; // The user-visible name of the channel.
        String description = "my_package_first_channel"; // The user-visible description of the channel.
        NotificationCompat.Builder builder;


        if (!TextUtils.isEmpty(type)) {
//
            if (type.equals("post")) {
                final Intent intent = new Intent(this, MainDashboardActivity.class);
                intent.putExtra("type", type);
                intent.putExtra("type_id", type_id);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                pInetent = PendingIntent.getActivity(this, NOTIFY_ID /* Request code */, intent,
                        PendingIntent.FLAG_ONE_SHOT);

            } else if (type.equals("Post")) {
                final Intent intent = new Intent(this, MainDashboardActivity.class);
                intent.putExtra("type", type);
                intent.putExtra("type_id", type_id);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                pInetent = PendingIntent.getActivity(this, NOTIFY_ID /* Request code */, intent,
                        PendingIntent.FLAG_ONE_SHOT);

            } else if (type.equals("Category")) {
                final Intent intent = new Intent(this, MainDashboardActivity.class);
                intent.putExtra("type", type);
                intent.putExtra("type_id", type_id);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                pInetent = PendingIntent.getActivity(this, NOTIFY_ID /* Request code */, intent,
                        PendingIntent.FLAG_ONE_SHOT);

            } else if (type.equals("Activity")) {
                final Intent intent = new Intent(this, MainDashboardActivity.class);
                intent.putExtra("type", type);
                intent.putExtra("type_id", "");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                pInetent = PendingIntent.getActivity(this, NOTIFY_ID /* Request code */, intent,
                        PendingIntent.FLAG_ONE_SHOT);
            } else if (type.equals("follower_request")) {
                final Intent intent = new Intent(this, MainDashboardActivity.class);
                intent.putExtra("type", type);
                intent.putExtra("type_id", type_id);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                pInetent = PendingIntent.getActivity(this, NOTIFY_ID /* Request code */, intent,
                        PendingIntent.FLAG_ONE_SHOT);
            } else if (type.equals("user")) {
                final Intent intent = new Intent(this, MainDashboardActivity.class);
                intent.putExtra("type", type);
                intent.putExtra("type_id", type_id);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                pInetent = PendingIntent.getActivity(this, NOTIFY_ID /* Request code */, intent,
                        PendingIntent.FLAG_ONE_SHOT);
            } else if (type.equals("MyRoleUpdates")) {
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
//                try {
//                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
//                } catch (android.content.ActivityNotFoundException anfe) {
//                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
//                }
                final Intent intent = new Intent(Intent.ACTION_VIEW);
                try {
//                intent.setData(Uri.parse("https://goo.gl/HDW5Yb"));
                intent.setData(Uri.parse("market://details?id=" + appPackageName));
                } catch (android.content.ActivityNotFoundException anfe) {
                    intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName));
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                pInetent = PendingIntent.getActivity(this, NOTIFY_ID /* Request code */, intent,
                        PendingIntent.FLAG_ONE_SHOT);
            }
            else {
                final Intent intent = new Intent(this, MainDashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                pInetent = PendingIntent.getActivity(this, NOTIFY_ID /* Request code */, intent,
                        PendingIntent.FLAG_ONE_SHOT);
            }

        } else {

            final Intent intent = new Intent(this, MainDashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pInetent = PendingIntent.getActivity(this, NOTIFY_ID /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);
        }


        final PendingIntent pendingIntent = pInetent;


//        if (!TextUtils.isEmpty(image)) {
//
//
//            new AsyncTask<Void, Void, Bitmap>() {
//                @Override
//                protected Bitmap doInBackground(Void... params) {
//                    Bitmap bitmap = null;
//                    // avoid java.lang.RuntimeException: Only one Looper may be created per thread creash
//                    if (Looper.myLooper() == null) {
//                        Looper.prepare();
//                    }//end
//                    try {
//                        bitmap = Glide.
//                                with(MyFirebaseMessagingService.this).
//                                load(image).
//                                asBitmap().
//                                into(-1, -1).
//                                get();
//                    } catch (final ExecutionException e) {
//                        Log.e(TAG, e.getMessage());
//                    } catch (final InterruptedException e) {
//                        Log.e(TAG, e.getMessage());
//                    }
//                    return bitmap;
//                }
//
//                @Override
//                protected void onPostExecute(Bitmap bitmap) {
//                    if (null != bitmap) {
//                        // The full bitmap should be available here
//                        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(MyFirebaseMessagingService.this)
//                                .setSmallIcon(R.drawable.ch)
//                                .setLargeIcon(bitmap)
//                                .setContentTitle(getString(R.string.app_name))
//
//                                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
//                                .setAutoCancel(true)
//                                .setSound(defaultSoundUri)
//                                .setContentIntent(pendingIntent);
//                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//                        notificationManager.notify(0, notificationBuilder.build());
//                    }
//                    ;
//                }
//            }.execute();
//        } else {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_DEFAULT);
//
//            // Configure the notification channel.
//            notificationChannel.setDescription("Channel description");
//            notificationChannel.enableLights(true);
//            notificationChannel.setLightColor(Color.RED);
//            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
//            notificationChannel.enableVibration(true);
//            notificationManager.createNotificationChannel(notificationChannel);
//        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = notificationManager.getNotificationChannel(id);
            if (mChannel == null) {
                mChannel = new NotificationChannel(id, name, importance);
                mChannel.setDescription(description);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                notificationManager.createNotificationChannel(mChannel);
            }
            builder = new NotificationCompat.Builder(this, id);

//            intent = new Intent(this, MainActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            if (title != null && !title.isEmpty()) {
//            if (title==null && title.isEmpty()) {
                try {
                    builder.setContentTitle(title)                        // required
                            .setSmallIcon(R.drawable.myrole_logo_1) // required
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo))
                            .setContentText(StringEscapeUtils.unescapeJava(message))  // required
                            .setStyle(new NotificationCompat.BigTextStyle().bigText(StringEscapeUtils.unescapeJava(message)))
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setAutoCancel(true)
                            .setContentIntent(pendingIntent)
                            .setTicker(getString(R.string.app_name))
                            .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                } catch (Exception e) {
                }


            } else {
                try {
                    builder.setContentTitle(getString(R.string.app_name))                        // required
                            .setSmallIcon(R.drawable.myrole_logo_1) // required
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo))
                            .setContentText(StringEscapeUtils.unescapeJava(message))  // required\
                            .setStyle(new NotificationCompat.BigTextStyle().bigText(StringEscapeUtils.unescapeJava(message)))
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setAutoCancel(true)
                            .setContentIntent(pendingIntent)
                            .setTicker(getString(R.string.app_name))
                            .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                } catch (Exception e) {
                }
            }


//            builder.setContentTitle(aMessage)  // required
//                    .setSmallIcon(android.R.drawable.ic_popup_reminder) // required
//                    .setContentText(this.getString(R.string.app_name))  // required
//                    .setDefaults(Notification.DEFAULT_ALL)
//                    .setAutoCancel(true)
//                    .setContentIntent(pendingIntent)
//                    .setTicker(aMessage)
//                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        } else {

            builder = new NotificationCompat.Builder(this);

//            intent = new Intent(this, MainActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            if (title != null && !title.isEmpty()) {
                try{
                builder.setContentTitle(title)                        // required
                        .setSmallIcon(R.drawable.myrole_logo_1) // required
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo))
                        .setContentText(StringEscapeUtils.unescapeJava(message))  // required
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(StringEscapeUtils.unescapeJava(message)))
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .setTicker(getString(R.string.app_name))
                        .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                        .setPriority(Notification.PRIORITY_HIGH);}catch (Exception e){}
                //if ( title==null && title.isEmpty()) {

            } else {
                try{
                builder.setContentTitle(getString(R.string.app_name))                        // required
                        .setSmallIcon(R.drawable.myrole_logo_1) // required
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo))
                        .setContentText(StringEscapeUtils.unescapeJava(message))  // required
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(StringEscapeUtils.unescapeJava(message)))
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .setTicker(getString(R.string.app_name))
                        .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                        .setPriority(Notification.PRIORITY_HIGH);}catch (Exception e){}
            }

        } // else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

        Notification notification = builder.build();
        notificationManager.notify(NOTIFY_ID, notification);

//
//            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
////                    .setSmallIcon(R.drawable.ch)
//                    .setSmallIcon(R.drawable.myrole_logo_1)
//                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo))
//                    .setContentTitle(getString(R.string.app_name))
//                    .setContentText(message)
//                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
//                    .setAutoCancel(true)
//                    .setSound(defaultSoundUri)
//                    .setContentIntent(pendingIntent);
//
//
//            notificationManager.notify(0, notificationBuilder.build());

//        }

        sendBroadcast(new Intent().setAction("com.myrole.updateBadges"));


//        if (type.equals("post")) {
//            Intent intent = new Intent(this, MainDashboardActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.putExtra("type", type);
//            intent.putExtra("type_id", type_id);
////
//            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);
//            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//
//            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo))/*Notification icon image*/
//                    .setSmallIcon(R.drawable.ch)
//                    .setContentTitle(getString(R.string.app_name))
//                    // .setContentText(Body)
//                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
//                    .setAutoCancel(true)
//                    .setSound(defaultSoundUri)
//                    .setContentIntent(pendingIntent);
//
//            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
//
//
////            notification.setLatestEventInfo(contexta, contentTitle, contentText, contentIntent);
////            mNotificationManager.notify(970970, notification);
//
//            // PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
//        } else if (type.equals("categeory")) {
//            Log.e(TAG, "Data  " + type);
//            Intent intent = new Intent(this, MainDashboardActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.putExtra("type", type);
//            intent.putExtra("type_id", type_id);
//            // startActivity(intent);
//            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);
//
//            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//
//            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo))/*Notification icon image*/
//                    .setSmallIcon(R.drawable.ch)
//                    // .setContentTitle(messageBody)
//                    .setContentTitle(getString(R.string.app_name))
//                    // .setContentText(Body)
//                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
////
//                    .setAutoCancel(true)
//                    .setSound(defaultSoundUri)
//                    .setContentIntent(pendingIntent);
//
//            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
//
//            // PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
//        } else if (type.equals("activity")) {
//            Intent intent = new Intent(this, MainDashboardActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.putExtra("type", type);
//            intent.putExtra("type_id", type_id);
//            //  startActivity(intent);
//            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//                    PendingIntent.FLAG_ONE_SHOT);
//            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//
//            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo))/*Notification icon image*/
//                    .setSmallIcon(R.drawable.ch)
//                    // .setContentTitle(messageBody)
//                    .setContentTitle(getString(R.string.app_name))
//                    // .setContentText(Body)
//                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
////
//                    .setAutoCancel(true)
//                    .setSound(defaultSoundUri)
//                    .setContentIntent(pendingIntent);
//
//            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
//            // PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
//        }
//        sendBroadcast(new Intent().setAction("com.myrole.updateBadges"));
    }

    public void sendNotification(final Map<String, String> data) {

        final Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        PendingIntent pInetent = null;

        if (!TextUtils.isEmpty(data.get("type"))) {

            if (data.get("type").equalsIgnoreCase("post")) {
                final Intent intent = new Intent(this, MainDashboardActivity.class);
                intent.putExtra("callPostDetailsFragment", true);
                intent.putExtra("id", data.get("type_id"));
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                pInetent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                        PendingIntent.FLAG_ONE_SHOT);


            } else if (data.get("type").equalsIgnoreCase("user")) {
                final Intent intent = new Intent(this, MainDashboardActivity.class);
                intent.putExtra("callOtherUserFragment", true);
                intent.putExtra("id", data.get("type_id"));
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                pInetent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                        PendingIntent.FLAG_ONE_SHOT);


            } else {
                final Intent intent = new Intent(this, MainDashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                pInetent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                        PendingIntent.FLAG_ONE_SHOT);
            }

        } else {

            final Intent intent = new Intent(this, MainDashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pInetent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);
        }
        final PendingIntent pendingIntent = pInetent;


        if (!TextUtils.isEmpty(data.get("image"))) {


            new AsyncTask<Void, Void, Bitmap>() {
                @Override
                protected Bitmap doInBackground(Void... params) {
                    Bitmap bitmap = null;
                    // avoid java.lang.RuntimeException: Only one Looper may be created per thread creash
                    if (Looper.myLooper() == null) {
                        Looper.prepare();
                    }//end
                    try {
                        bitmap = Glide.
                                with(MyFirebaseMessagingService.this).
                                asBitmap().
                                load(data.get("image")).

                                into(-1, -1).
                                get();
                    } catch (final ExecutionException e) {
                        Log.e(TAG, e.getMessage());
                    } catch (final InterruptedException e) {
                        Log.e(TAG, e.getMessage());
                    }
                    return bitmap;
                }

                @Override
                protected void onPostExecute(Bitmap bitmap) {
                    if (null != bitmap) {
                        // The full bitmap should be available here
                        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(MyFirebaseMessagingService.this)
                                .setSmallIcon(R.drawable.ch)
                                .setLargeIcon(bitmap)
                                .setContentTitle(getString(R.string.app_name))

                                .setStyle(new NotificationCompat.BigTextStyle().bigText(data.get("message")))
                                .setAutoCancel(true)
                                .setSound(defaultSoundUri)
                                .setContentIntent(pendingIntent);
                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                        notificationManager.notify(0, notificationBuilder.build());
                    }
                }
            }.execute();
        } else {

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ch)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo))
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText(data.get("message"))
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0, notificationBuilder.build());
        }

        sendBroadcast(new Intent().setAction("com.myrole.updateBadges"));
    }


}
