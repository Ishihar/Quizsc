package com.example.a171y005.quizsc;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.NotificationCompat;

/**
 * Created by 171y005 on 2018/10/05.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int value = intent.getIntExtra("KEY",0);

        Resources res = context.getResources();

        NotificationCompat.Builder n = (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.baseline_public_black_48dp)
                .setContentTitle("英単語学習")
                .setContentText("最後の学習から1日経ちました。")
                .setAutoCancel(true);


        Intent i = new Intent(context, TitleActivity.class);

        PendingIntent pi = PendingIntent.getActivity(context,0,i,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager mNotifyM = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        mNotifyM.notify(1,n.build());


    }
}
