package com.example.myapp;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;
import android.os.Handler;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import java.util.Date;

import static android.content.Context.NOTIFICATION_SERVICE;


public class callReciver extends PhonecallReceiver {
    Context context;

    @Override
    protected void onIncomingCallStarted(final Context ctx, String number, Date start) {
        Toast.makeText(ctx, "Incoming Call " + number, Toast.LENGTH_LONG).show();

        context = ctx;

        final Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        //intent.setAction("android.intent.action.PHONE_STATE");

        intent.putExtra("phone_no", number);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                context.startActivity(intent);
            }

        }, 2000);

//        MyCus/*tomDialog dialog   =   new MyCustomDialog(context);
//        dialog.*/show();
    }

    @Override
    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end) {
        Toast.makeText(ctx, "Bye " + number, Toast.LENGTH_LONG).show();
        System.exit(0);
    }
}