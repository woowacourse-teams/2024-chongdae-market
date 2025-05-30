package com.zzang.chongdae.presentation.view.nickname

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result
import com.zzang.chongdae.domain.usecase.nickname.PatchNicknameUseCase
import com.zzang.chongdae.domain.usecase.nickname.SaveNicknameUseCase
import com.zzang.chongdae.presentation.view.nickname.event.NicknameEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditNicknameViewModel
    @Inject
    constructor(
        private val patchNicknameUseCase: PatchNicknameUseCase,
        private val saveNicknameUseCase: SaveNicknameUseCase,
    ) : ViewModel() {
        var nickname by mutableStateOf("")
            private set

        val isNicknameValid: Boolean
            get() = nickname.isNotBlank() && nickname.length <= 10

        val shouldShowHelper: Boolean
            get() = nickname.length > 10

        private val _event = MutableSharedFlow<NicknameEvent>()
        val event = _event.asSharedFlow()

        fun onNicknameChange(input: String) {
            nickname = input
        }

        fun confirmNickname() {
            viewModelScope.launch {
                if (!isNicknameValid) {
                    _event.emit(NicknameEvent.InvalidNickname)
                    return@launch
                }

                when (val result = patchNicknameUseCase(nickname)) {
                    is Result.Error ->
                        when (result.error) {
                            DataError.Network.BAD_REQUEST -> _event.emit(NicknameEvent.ShowToast("닉네임은 10자 이하로 입력해주세요."))
                            DataError.Network.CONFLICT -> _event.emit(NicknameEvent.ShowToast("이미 사용중인 닉네임입니다."))
                            else -> _event.emit(NicknameEvent.ShowToast("닉네임 변경에 실패했습니다."))
                        }
                    is Result.Success -> {
                        saveNicknameUseCase(nickname)
                        _event.emit(NicknameEvent.NicknameChanged)
                    }
                }
            }
        }
    }
