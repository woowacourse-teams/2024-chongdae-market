package com.zzang.chongdae.presentation.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.zzang.chongdae.R
import com.zzang.chongdae.databinding.ActivityMainBinding
import com.zzang.chongdae.presentation.view.offeringdetail.OfferingDetailFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController

    private val offeringIdFromNotification by lazy {
        intent.getLongExtra(NOTIFICATION_OFFERING_ID_KEY, OFFERING_ID_ERROR)
    }
    private val isNotificationTriggered by lazy {
        intent.getBooleanExtra(NOTIFICATION_FLAG_KEY, DEFAULT_NOTIFICATION_FLAG)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestNotificationPermission()
        initBinding()
        initNavController()
        setupBottomNavigation()
        handleDeepLink(intent)
        handleNotificationTrigger()
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.POST_NOTIFICATIONS,
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    PENDING_INTENT_REQUEST_CODE,
                )
            }
        }
    }

    private fun initBinding() {
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun initNavController() {
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        navController = navHostFragment.findNavController()
    }

    private fun setupBottomNavigation() {
        binding.mainBottomNavigation.setupWithNavController(navController)
    }

    fun hideBottomNavigation() {
        binding.mainBottomNavigation.visibility = View.GONE
    }

    fun showBottomNavigation() {
        binding.mainBottomNavigation.visibility = View.VISIBLE
    }

    override fun dispatchTouchEvent(motionEvent: MotionEvent): Boolean {
        (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).apply {
            this.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }
        return super.dispatchTouchEvent(motionEvent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleDeepLink(intent)
    }

    private fun handleDeepLink(intent: Intent) {
        val data: Uri? = intent.data
        data?.let { uri ->
            if (uri.scheme == SCHEME && uri.host == HOST) {
                val offeringIdStr = uri.lastPathSegment

                val offeringId = offeringIdStr?.toLongOrNull()
                if (offeringId != null) {
                    openOfferingDetailFragment(offeringId)
                } else {
                    Toast.makeText(this, "공모 ID가 올바르지 않습니다.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Deeplink가 올바르지 않습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openOfferingDetailFragment(offeringId: Long) {
        val navController = navHostFragment.navController
        val bundle = bundleOf(OfferingDetailFragment.OFFERING_ID_KEY to offeringId)

        navController.navigate(R.id.action_home_fragment_to_offering_detail_fragment, bundle)
    }

    private fun handleNotificationTrigger() {
        if (isNotificationTriggered) {
            openOfferingDetailFragment(offeringIdFromNotification)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val SCHEME = "chongdaeapp"
        private const val HOST = "offerings"
        const val NOTIFICATION_OFFERING_ID_KEY = "notification_offering_id_key"
        const val NOTIFICATION_FLAG_KEY = "notification_flag_key"
        private const val OFFERING_ID_ERROR = -1L
        private const val DEFAULT_NOTIFICATION_FLAG = false
        private const val PENDING_INTENT_REQUEST_CODE = 1001

        fun startActivity(context: Context) =
            Intent(context, MainActivity::class.java).run {
                context.startActivity(this)
            }
    }
}
