package com.zzang.chongdae.common.firebase

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.util.Log

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        
        // 메시지 처리
        Log.d(TAG, "From: ${remoteMessage.from}")

        // 메시지가 데이터 페이로드를 포함하는 경우
        remoteMessage.data.isNotEmpty().let {
            Log.d(TAG, "Message data payload: ${remoteMessage.data}")
        }

        // 메시지가 알림 페이로드를 포함하는 경우
        remoteMessage.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "Refreshed token: $token")
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