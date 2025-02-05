package com.example.clothingmall.sensors

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.location.LocationManager
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import android.hardware.Sensor
import android.hardware.SensorManager
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import androidx.compose.runtime.mutableStateOf
import android.location.Location
import android.location.LocationListener
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class DevicesSensors(private val context: Context) {
    private val batteryStatus = mutableStateOf<Int>(100)
    private val temperature = mutableStateOf<Float>(0f)
    private val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    fun getBatteryPercentage(): Int {
        val batteryIntent = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        val level = batteryIntent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
        val scale = batteryIntent?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
        return ((level.toFloat() / scale.toFloat()) * 100).toInt()
    }

    fun getClothingSuggestionForTemperature(temp: Float): String {
        return when {
            temp >= 25 -> "It's warm! Wear light clothes like t-shirts, shorts and sandals"
            temp < 15 -> "It's cold! Wear warm clothes like jackets, sweaters, and boots"
            else -> "The temperature is normal. Wear casual clothes like jeans, t-shirts, and sneakers"
        }
    }

    fun checkBatteryStatus(onLowBattery: () -> Unit) {
        val percentage = getBatteryPercentage()
        if (percentage <= 20) {
            onLowBattery()
        }
    }
} 