package com.zzang.chongdae.common.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.util.Log
import androidx.core.app.NotificationCompat
import com.zzang.chongdae.R
import com.zzang.chongdae.presentation.view.MainActivity

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        
//        // 메시지 처리
//        Log.d(TAG, "From: ${remoteMessage.from}")
//

        // 메시지가 알림 페이로드를 포함하는 경우
        remoteMessage.notification?.apply {
            Log.d(TAG, "Message Notification Body: ${body}")
            sendNotification(title, body)
        }

        Log.d(TAG, "onMessageReceived: $remoteMessage")
    }

    private fun sendNotification(title: String?, messageBody: String?) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 1001, intent, PendingIntent.FLAG_IMMUTABLE)

        val channelId = "default_channel"
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_check) // 알림 아이콘
            .setContentTitle(title)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Default Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(1001, notificationBuilder.build())
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "new token: $token")
        // 서버로 새 토큰 전송
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String?) {
        // 서버에 FCM 토큰을 전송하는 로직
    }

    companion object {
        private const val TAG = "MyFirebaseMsgService"
    }
}