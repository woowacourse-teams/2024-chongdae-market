package com.zzang.chongdae.presentation.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class PermissionManager(
    private val fragment: Fragment,
    private val onPermissionGranted: () -> Unit,
    private val onPermissionDenied: () -> Unit,
) {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private val permission33 =
        arrayOf(
            Manifest.permission.READ_MEDIA_IMAGES,
        )

    private val permission =
        arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
        )

    private val requestPermissionLauncher =
        fragment.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(),
        ) { permissions ->
            if (permissions.values.all { it }) {
                onPermissionGranted()
            } else {
                onPermissionDenied()
            }
        }

    fun requestPermissions() {
        val permissionsToRequest =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                permission33
            } else {
                permission
            }

        if (!hasPermissions(fragment.requireContext(), *permissionsToRequest)) {
            requestPermissionLauncher.launch(permissionsToRequest)
        } else {
            onPermissionGranted()
        }
    }

    private fun hasPermissions(
        context: Context,
        vararg permissions: String,
    ): Boolean {
        return permissions.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }
}
