package com.example.musicplayer

import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

class PermissionManager(private val activity: AppCompatActivity) {

    companion object {
        private const val PERMISSION_REQUEST_CODE = 123
    }

    fun requestPermissions(permissions: Array<String>) {
        val permissionsToRequest = mutableListOf<String>()
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission)
            }
        }
        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(activity, permissionsToRequest.toTypedArray(), PERMISSION_REQUEST_CODE)
        }
    }

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            for (i in permissions.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    activity.showToast("Permissions received!")
                } else {
                    activity.showToast("Permissions not received!")
                }
            }
        }
    }

    fun checkPermissions(permissions: Array<String>): Boolean {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun requestBackgroundLocationPermission() {
        val permission = Manifest.permission.ACCESS_BACKGROUND_LOCATION
        if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, arrayOf(permission), PERMISSION_REQUEST_CODE)
        }
    }

}