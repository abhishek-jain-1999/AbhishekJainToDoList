package com.example.abhishek.mytodolist;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        if (intent.getAction() != null) {
            if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
                Bundle bundle = intent.getExtras();
                if (MainActivity.updateWithMessage) {
                    if (bundle != null) {

                        final Object[] pdusObj = (Object[]) bundle.get("pdus");
                        if (pdusObj == null) {

                        }


                        for (int i = 0; i < pdusObj.length; i++) {

                            SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                            String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                            String senderNum = phoneNumber;
                            String message = currentMessage.getDisplayMessageBody();

                            Log.i("SmsReceiver", "senderNum: " + senderNum + "; message: " + message);


                            // Show alert
                            // int duration = Toast.LENGTH_LONG;
                            //Toast.makeText(context, "senderNum: "+ senderNum + ", message: " + message, duration).show();

                            Calendar cal = Calendar.getInstance();

                            int hour = cal.get(Calendar.HOUR_OF_DAY);
                            int min = cal.get(Calendar.MINUTE);
                            String dateS = "" + cal.get(Calendar.DAY_OF_MONTH) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR);
                            String timeS = "" + hour + ":" + min;
                            MainActivity.addTask(senderNum, message, dateS, timeS, context);


                        } // end for loop
                    }
                }
            }
        } else {
            if (intent.getStringExtra("Action") != null) {
                if (intent.getStringExtra("Action").equals("CustomAction")) {

                    NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        NotificationChannel notificationChannel = new NotificationChannel("DisplayOnComplete", "Todo", NotificationManager.IMPORTANCE_HIGH);
                        manager.createNotificationChannel(notificationChannel);

                    }
                    Intent intent1 = new Intent(context, ActivityForDisplay.class);
                    intent1.putExtra("data", intent.getStringExtra("data"));
                    intent1.putExtra("date", intent.getStringExtra("date"));
                    intent1.putExtra("name", intent.getStringExtra("name"));
                    intent1.putExtra("time", intent.getStringExtra("time"));
                    intent1.putExtra("Action", "CustomAction");
                    intent1.putExtra("idRow", intent.getLongExtra("idRow", 0));
                    intent1.putExtra("position", intent.getIntExtra("position", -1));

                    //Toast.makeText(context,""+intent.getIntExtra("id",5)+intent.getStringExtra("data"),Toast.LENGTH_LONG).show();

                    PendingIntent pendingIntent = PendingIntent.getActivity(context, intent.getIntExtra("id", 5), intent1, PendingIntent.FLAG_UPDATE_CURRENT);


                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "DisplayOnComplete");
                    builder.setContentText(intent.getStringExtra("data"));
                    builder.setContentTitle(intent.getStringExtra("name"));
                    builder.setSmallIcon(R.drawable.ic_launcher_foreground);
                    builder.setContentIntent(pendingIntent);
                    builder.setAutoCancel(true);

                    Notification notification = builder.build();

                    manager.notify(intent.getIntExtra("id", 2), notification);


                }
            }
        }


        //throw new UnsupportedOperationException("Not yet implemented");
    }
}
