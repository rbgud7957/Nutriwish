package com.example.nutriwish;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    public void scheduleNotification(Context context, String title, String message, long timeInMillis) {
        NotificationHelper notificationHelper = new NotificationHelper(context); // NotificationHelper 생성
        notificationHelper.scheduleNotification(context, title, message, timeInMillis); // 알림 예약
    }
}
