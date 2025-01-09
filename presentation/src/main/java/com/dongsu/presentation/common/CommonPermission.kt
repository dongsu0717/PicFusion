package com.dongsu.presentation.common

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import timber.log.Timber

//읽기 권한
fun checkReadImagePermission(context: Context): Boolean {
    val imagePermission = arrayOf(getGalleryPermission())
    return arePermissionsGranted(context,imagePermission)
}

fun getGalleryPermission(): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Timber.e("API 33이상")
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Timber.e("API 33미만")
        Manifest.permission.READ_EXTERNAL_STORAGE
    }
}

//쓰기 권한
fun checkWritePermission(context: Context): Boolean {
    return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
        Timber.e("API 29이상")
        true
    } else {
        val writePermission = arrayOf(getWritePermission())
        Timber.e("API 29미만")
        return arePermissionsGranted(context,writePermission)
    }
}

fun getWritePermission(): String = Manifest.permission.WRITE_EXTERNAL_STORAGE


fun arePermissionsGranted(context: Context, permissions: Array<String>): Boolean {
    return permissions.all {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }
}

fun shouldShowRequestPermissionRationale(context: Context, permissions: Array<String>): Boolean {
    return permissions.any { (context as ComponentActivity).shouldShowRequestPermissionRationale(it) }
}

fun openSettings(context: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", context.packageName, null)
    }
    context.startActivity(intent)
}

