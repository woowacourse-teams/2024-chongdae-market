package com.zzang.chongdae.presentation.view.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.messaging.FirebaseMessaging
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.zzang.chongdae.common.firebase.FirebaseAnalyticsManager
import com.zzang.chongdae.databinding.ActivityLoginBinding
import com.zzang.chongdae.presentation.view.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity(), OnAuthClickListener {
    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()

    private val firebaseAnalytics: FirebaseAnalytics by lazy {
        FirebaseAnalytics.getInstance(this)
    }

    private val firebaseAnalyticsManager: FirebaseAnalyticsManager by lazy {
        FirebaseAnalyticsManager(firebaseAnalytics)
    }

    private val kakaoAuthResultHandler: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e("error", "카카오계정으로 로그인 실패", error)
        } else if (token != null) {
            loadUserInformation(token.accessToken)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        setupObserve()
    }

    private fun setupObserve() {
        observeAlreadyLoggedInEvent()
        observeLoginSuccessEvent()
    }

    private fun observeAlreadyLoggedInEvent() {
        viewModel.alreadyLoggedInEvent.observe(this) {
            navigateToNextActivity()
        }
    }

    private fun observeLoginSuccessEvent() {
        viewModel.loginSuccessEvent.observe(this) {
            firebaseAnalyticsManager.logSelectContentEvent(
                id = "login_event",
                name = "login_event",
                contentType = "button",
            )
            navigateToNextActivity()
        }
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
            loginWithKakaoTalk()
        } else {
            UserApiClient.instance.loginWithKakaoAccount(this, callback = kakaoAuthResultHandler)
        }
    }

    private fun loginWithKakaoTalk() {
        UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
            if (error != null) {
                Log.e("error", "카카오톡으로 로그인 실패", error)
                loginWithKakaoAcountIfKakaoTalkLoginFailed(error)
            } else if (token != null) {
                loadUserInformation(token.accessToken)
            }
        }
    }

    private fun loginWithKakaoAcountIfKakaoTalkLoginFailed(error: Throwable?) {
        if (isKakaoTalkLoginCanceled(error)) {
            return
        }
        UserApiClient.instance.loginWithKakaoAccount(this, callback = kakaoAuthResultHandler)
    }

    private fun isKakaoTalkLoginCanceled(error: Throwable?): Boolean {
        return error is ClientError && error.reason == ClientErrorCause.Cancelled
    }

    private fun loadUserInformation(accessToken: String) {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.d("error", "사용자 정보 요청 실패 $error")
            } else if (user != null) {
                loadFcmToken { fcmToken ->
                    viewModel.postLogin(accessToken, fcmToken)
                }
            }
        }
    }

    private fun loadFcmToken(onFcmTokenReceived: (String) -> Unit) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.e("error", "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }
            val fcmToken = task.result
            onFcmTokenReceived(fcmToken)
        }
    }

    private fun navigateToNextActivity() {
        MainActivity.startActivity(this)
        finish()
    }

    companion object {
        fun startActivity(context: Context) =
            Intent(context, LoginActivity::class.java).run {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                context.startActivity(this)
            }
    }
}
