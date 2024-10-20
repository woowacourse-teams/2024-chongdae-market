package com.zzang.chongdae.common.firebase.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.zzang.chongdae.R
import com.zzang.chongdae.common.datastore.UserPreferencesDataStore
import com.zzang.chongdae.presentation.view.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ChongdaeFirebaseMessagingService : FirebaseMessagingService() {
    @Inject
    lateinit var userPreferencesDataStore: UserPreferencesDataStore

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        sendNotificationAtBackground(remoteMessage)
        sendNotificationAtForeground(remoteMessage)
    }

    private fun sendNotificationAtBackground(remoteMessage: RemoteMessage) {
        if (remoteMessage.data.isNotEmpty()) {
            val title = remoteMessage.data[TITLE_KEY]
            val messageBody = remoteMessage.data[BODY_KEY]
            sendNotification(title, messageBody)
        }
    }

    private fun sendNotificationAtForeground(remoteMessage: RemoteMessage) {
        remoteMessage.notification?.apply {
            sendNotification(title, body)
        }
    }

    private fun sendNotification(
        title: String?,
        body: String?,
    ) {
        val pendingIntent = generatePendingIntent()
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel(notificationManager)
        val uniqueNotificationId = System.currentTimeMillis().toInt()
        val notificationBuilder = buildNotification(title, body, pendingIntent)
        notificationManager.notify(uniqueNotificationId, notificationBuilder.build())
    }

    private fun generatePendingIntent(): PendingIntent? {
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
        var channel: NotificationChannel
        CoroutineScope(Dispatchers.IO).launch {
            val notificationImportance =
                NotificationImportance.of(
                    userPreferencesDataStore.notificationImportanceFlow.first(),
                )
            Log.d("alsong", "${notificationImportance}")
            channel = when (notificationImportance) {
                NotificationImportance.DEFAULT -> setImportance(NotificationManager.IMPORTANCE_DEFAULT)
                NotificationImportance.HIGH -> setImportance(NotificationManager.IMPORTANCE_HIGH)
            }
            Log.d("alsong", "${channel.importance}")
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun setImportance(importance: Int): NotificationChannel {
        return NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance)
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
    }
}
