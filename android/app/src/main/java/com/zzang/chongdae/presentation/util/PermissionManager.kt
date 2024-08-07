package com.zzang.chongdae.presentation.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class PermissionManager(
    private val fragment: Fragment,
    private val onPermissionGranted: () -> Unit,
    private val onPermissionDenied: () -> Unit,
) {
    private val storagePermissions =
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
        if (isAndroid13OrAbove() || hasPermissions(fragment.requireContext(), storagePermissions)) {
            onPermissionGranted()
        } else {
            requestPermissionLauncher.launch(storagePermissions)
        }
    }

    fun isAndroid13OrAbove(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

    private fun hasPermissions(
        context: Context,
        permissions: Array<String>,
    ): Boolean {
        return permissions.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }
}
