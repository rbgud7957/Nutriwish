package com.example.nutriwish;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class NotificationHelper {
    private static final String CHANNEL_ID = "nutriwish_channel"; // 알림 채널 ID
    private Context mContext;

    // Context를 매개변수로 받는 생성자 추가
    public NotificationHelper(Context context) {
        this.mContext = context;
    }

    // 알림 채널 생성 (Android 8.0 이상)
    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Nutriwish 알림 채널";
            String description = "영양제 복용 시간 알림";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    // 알림 예약
    @SuppressLint("ScheduleExactAlarm")
    public void scheduleNotification(Context context, String title, String message, long timeInMillis) {
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra("title", title);
        intent.putExtra("message", message);

        // requestCode를 고유하게 설정 (현재 시간을 기반으로)
        int requestCode = (int) (System.currentTimeMillis() & 0xFFFFFFF);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            Log.d("test", "알림 예약 시간: " + timeInMillis + ", 현재 시간: " + System.currentTimeMillis());
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
        }
    }

}
