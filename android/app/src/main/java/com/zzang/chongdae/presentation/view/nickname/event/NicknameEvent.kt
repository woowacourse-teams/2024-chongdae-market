package com.zzang.chongdae.presentation.view.nickname.event

sealed class NicknameEvent {
    data object NicknameChanged : NicknameEvent()

    data object InvalidNickname : NicknameEvent()

    data class ShowToast(val message: String) : NicknameEvent()
}
