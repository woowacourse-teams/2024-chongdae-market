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

class ChongdaeFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        sendNotificationAtForeground(remoteMessage)
    }

    private fun sendNotificationAtForeground(remoteMessage: RemoteMessage) {
        remoteMessage.notification?.apply {
            val pendingIntent = generatePendingIntent()
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            createNotificationChannel(notificationManager)
            val uniqueNotificationId = System.currentTimeMillis().toInt()
            val notificationBuilder = buildNotification(title, body, pendingIntent)
            notificationManager.notify(uniqueNotificationId, notificationBuilder.build())
        }
    }

    private fun generatePendingIntent(): PendingIntent? {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        return PendingIntent.getActivity(
            this,
            REQUEST_CODE,
            intent,
            PendingIntent.FLAG_IMMUTABLE,
        )
    }

    private fun buildNotification(
        title: String?,
        messageBody: String?,
        pendingIntent: PendingIntent?,
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
    }

    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel =
            NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
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
