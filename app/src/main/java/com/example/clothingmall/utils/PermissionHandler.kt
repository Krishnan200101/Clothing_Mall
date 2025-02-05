package com.example.clothingmall.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.content.ContextCompat

class PermissionHandler(private val context: Context) {
    fun hasPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }
}

@Composable
fun RequestPermissions(
    onPermissionsGranted: () -> Unit
) {
    val permissionsGranted = remember { mutableStateOf(false) }
    
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        permissionsGranted.value = permissions.values.all { it }
        if (permissionsGranted.value) {
            onPermissionsGranted()
        }
    }

    LaunchedEffect(Unit) {
        launcher.launch(PermissionHandler.REQUIRED_PERMISSIONS)
    }
} 