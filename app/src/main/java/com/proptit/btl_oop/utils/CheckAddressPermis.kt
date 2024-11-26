package com.proptit.btl_oop.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData

object CheckAddressPermis {

    // LiveData để theo dõi trạng thái quyền
    val locationPermissionGranted = MutableLiveData(false)

    // Hàm kiểm tra quyền
    fun checkLocationPermission(
        context: Context,
        requestPermissionLauncher: ActivityResultLauncher<String>
    ) {
        when {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                // Quyền đã được cấp
                locationPermissionGranted.value = true
            }
            else -> {
                // Yêu cầu quyền
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    // Cập nhật trạng thái quyền khi người dùng phản hồi
    fun handlePermissionResult(isGranted: Boolean) {
        locationPermissionGranted.value = isGranted
    }
}
