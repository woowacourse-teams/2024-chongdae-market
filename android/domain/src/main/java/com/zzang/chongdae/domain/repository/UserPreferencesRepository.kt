package com.zzang.chongdae.domain.repository

import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val accessTokenFlow: Flow<String?>
    val refreshTokenFlow: Flow<String?>
    val notificationActivateFlow: Flow<Boolean>
    val notificationImportanceFlow: Flow<Int>
    val nickNameFlow: Flow<String?>

    suspend fun saveMember(memberId: Long, nickname: String)
    suspend fun saveFcmToken(fcmToken: String)
    suspend fun saveNickname(nickname: String)
    suspend fun setNotificationActivate(isActivate: Boolean)
    suspend fun setNotificationImportance(importance: Int)
    suspend fun removeAllData()
    suspend fun saveAccountTokens(accessToken: String, refreshToken: String)
}
