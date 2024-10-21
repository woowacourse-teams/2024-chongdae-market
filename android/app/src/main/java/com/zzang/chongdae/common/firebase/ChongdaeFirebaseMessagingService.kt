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
        notifyFromRemoteMessage(remoteMessage)
    }

    private fun notifyFromRemoteMessage(remoteMessage: RemoteMessage) {
        if (remoteMessage.data.isNotEmpty()) {
            val title = remoteMessage.data[TITLE_KEY]
            val messageBody = remoteMessage.data[BODY_KEY]
            val offeringId = remoteMessage.data[OFFERING_ID_KEY]
            displayNotification(title, messageBody, offeringId)
        }
    }

//    private fun sendNotificationAtForeground(remoteMessage: RemoteMessage) {
//        Log.d("alsong", "Foreground")
//        remoteMessage.notification?.apply {
//            sendNotification(title, body)
//        }
//    }

    private fun displayNotification(
        title: String?,
        body: String?,
        offeringId: String?
    ) {
        val pendingIntent = generatePendingIntent(offeringId)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel(notificationManager)
        val uniqueNotificationId = System.currentTimeMillis().toInt()
        val notificationBuilder = buildNotification(title, body, pendingIntent)
        notificationManager.notify(uniqueNotificationId, notificationBuilder.build())
    }

    private fun generatePendingIntent(offeringId: String?): PendingIntent? {
        val intent = Intent(this, MainActivity::class.java)
        // TODO(댓글방으로 가야 할 경우 offeringID를 넘겨주어야 함)
        return PendingIntent.getActivity(
            this,
            MainActivity.PENDING_INTENT_REQUEST_CODE,
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
        Log.i("fcm", "FCM토큰 갱신됨. new token: $token")
    }

    companion object {
        private const val CHANNEL_ID = "default_channel"
        private const val CHANNEL_NAME = "Default Channel"
        private const val TITLE_KEY = "title"
        private const val BODY_KEY = "body"
        private const val OFFERING_ID_KEY = "offering_id"
    }
}
