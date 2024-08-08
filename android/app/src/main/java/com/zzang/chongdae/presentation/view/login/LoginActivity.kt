package com.zzang.chongdae.presentation.view.login

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.zzang.chongdae.ChongdaeApp
import com.zzang.chongdae.databinding.ActivityLoginBinding
import com.zzang.chongdae.presentation.view.MainActivity

class LoginActivity : AppCompatActivity(), OnAuthClickListener {
    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels {
        LoginViewModel.getFactory(
            authRepository = (application as ChongdaeApp).authRepository,
            context = applicationContext,
        )
    }

    val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e(TAG, "카카오계정으로 로그인 실패", error)
        } else if (token != null) {
            Log.i(TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
            loadUserInformation()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        observeNavigateEvent()
    }

    private fun initBinding() {
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.vm = viewModel
        binding.onAuthClickListener = this
        binding.lifecycleOwner = this
    }

    override fun onLoginButtonClick() {
        loginWithKakao()
    }

    private fun loginWithKakao() {
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
            UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                if (error != null) {
                    Log.e(TAG, "카카오톡으로 로그인 실패", error)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }
                    UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                } else if (token != null) {
                    Log.i(TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")
                    loadUserInformation()
                }
            }
        } else {
            Log.d(TAG, "카톡 설치 안되어있음. 계정으로 로그인 시도")
            UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
        }
    }

    private fun loadUserInformation() {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.d(TAG, "사용자 정보 요청 실패 $error")
            } else if (user != null) {
                Log.d(TAG, "사용자 정보 요청 성공 : $user")
                val email = user.kakaoAccount?.email ?: return@me
                viewModel.postLogin(email)
            }
        }
    }

    private fun observeNavigateEvent() {
        viewModel.navigateEvent.observe(this) {
            MainActivity.startActivity(this)
        }
    }

    companion object {
        const val TAG = "alsong"
    }
}
