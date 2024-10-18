package com.zzang.chongdae.common.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.zzang.chongdae.R
import com.zzang.chongdae.presentation.view.MainActivity

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        sendNotification(remoteMessage)
    }

    private fun sendNotification(remoteMessage: RemoteMessage) {
        remoteMessage.notification?.apply {
            val intent = Intent(this@MyFirebaseMessagingService, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            val pendingIntent =
                PendingIntent.getActivity(this@MyFirebaseMessagingService, REQUEST_CODE, intent, PendingIntent.FLAG_IMMUTABLE)

            val notificationBuilder = buildNotification(title, body, pendingIntent)

            val notificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            createNotificationChannel(notificationManager)
            notificationManager.notify(REQUEST_CODE, notificationBuilder.build())
        }
    }

    private fun buildNotification(
        title: String?,
        messageBody: String?,
        pendingIntent: PendingIntent?,
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_chongdae_sub)
            .setContentTitle(title)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
    }

    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel =
            NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT,
            )
        notificationManager.createNotificationChannel(channel)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.e("error", "FCM토큰 갱신됨. new token: $token")
    }

    companion object {
        private const val REQUEST_CODE = 1001
        private const val CHANNEL_ID = "default_channel"
        private const val CHANNEL_NAME = "Default Channel"
    }
}
