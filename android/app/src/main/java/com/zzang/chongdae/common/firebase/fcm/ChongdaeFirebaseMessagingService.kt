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
import com.zzang.chongdae.presentation.view.main.MainActivity
import com.zzang.chongdae.presentation.view.main.MainActivity.Companion.NOTIFICATION_FLAG_KEY
import com.zzang.chongdae.presentation.view.main.MainActivity.Companion.NOTIFICATION_OFFERING_ID_KEY
import com.zzang.chongdae.presentation.view.commentdetail.CommentDetailActivity
import com.zzang.chongdae.presentation.view.commentdetail.CommentDetailActivity.Companion.EXTRA_OFFERING_ID_KEY
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ChongdaeFirebaseMessagingService : FirebaseMessagingService() {
    @Inject
    lateinit var dataStore: UserPreferencesDataStore

    private lateinit var notificationImportance: NotificationImportance

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        CoroutineScope(Dispatchers.IO).launch {
            if (isLoggedOut()) return@launch
            if (isNotificationInactivate()) return@launch
            setNotificationImportance()
            notifyFromRemoteMessage(remoteMessage)
        }
    }

    private suspend fun isLoggedOut(): Boolean {
        return dataStore.accessTokenFlow.first() == null
    }

    private suspend fun isNotificationInactivate(): Boolean {
        return !dataStore.notificationActivateFlow.first()
    }

    private suspend fun setNotificationImportance() {
        when (dataStore.notificationImportanceFlow.first()) {
            NotificationManager.IMPORTANCE_DEFAULT -> notificationImportance = NotificationImportance.Default
            NotificationManager.IMPORTANCE_HIGH -> notificationImportance = NotificationImportance.High
        }
    }

    private fun notifyFromRemoteMessage(remoteMessage: RemoteMessage) {
        if (remoteMessage.data.isNotEmpty()) {
            val title = remoteMessage.data[TITLE_KEY]
            val messageBody = remoteMessage.data[BODY_KEY]
            val notificationType = remoteMessage.data[NOTIFICATION_TYPE_KEY]
            val offeringId = remoteMessage.data[OFFERING_ID_KEY]
            displayNotification(title, messageBody, notificationType, offeringId)
        }
    }

    private fun displayNotification(
        title: String?,
        body: String?,
        notificationType: String?,
        offeringId: String?,
    ) {
        val pendingIntent = generatePendingIntent(notificationType, offeringId)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel(notificationManager)
        val uniqueNotificationId = System.currentTimeMillis().toInt()
        val notificationBuilder = buildNotification(title, body, pendingIntent)
        notificationManager.notify(uniqueNotificationId, notificationBuilder.build())
    }

    private fun generatePendingIntent(
        type: String?,
        offeringId: String?,
    ): PendingIntent? {
        val notificationType = NotificationType.of(type)
        val parsedOfferingId = offeringId?.toLong() ?: error("알림 데이터에 offeringId가 없음")
        val intent = intentOf(notificationType, parsedOfferingId)
        val uniqueRequestCode = System.currentTimeMillis().toInt()
        return PendingIntent.getActivity(
            this,
            uniqueRequestCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE,
        )
    }

    private fun intentOf(
        notificationType: NotificationType,
        parsedOfferingId: Long,
    ): Intent {
        val intent: Intent
        when (notificationType) {
            NotificationType.COMMENT_DETAIL -> {
                intent = Intent(this, CommentDetailActivity::class.java)
                intent.putExtra(EXTRA_OFFERING_ID_KEY, parsedOfferingId)
            }

            NotificationType.OFFERING_DETAIL -> {
                intent = Intent(this, MainActivity::class.java)
                intent.putExtra(NOTIFICATION_OFFERING_ID_KEY, parsedOfferingId)
                intent.putExtra(NOTIFICATION_FLAG_KEY, true)
            }
        }
        return intent
    }

    private fun createNotificationChannel(notificationManager: NotificationManager) {
        notificationImportance.apply {
            val channel = NotificationChannel(channelId, channelName, importance)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun buildNotification(
        title: String?,
        messageBody: String?,
        pendingIntent: PendingIntent?,
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(this, notificationImportance.channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.i("FCM", "FCM토큰 갱신됨")
    }

    companion object {
        private const val TITLE_KEY = "title"
        private const val BODY_KEY = "body"
        private const val NOTIFICATION_TYPE_KEY = "type"
        private const val OFFERING_ID_KEY = "offering_id"
    }
}
