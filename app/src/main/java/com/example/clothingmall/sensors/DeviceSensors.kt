package com.example.clothingmall.sensors

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.LocationManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class DeviceSensors(private val context: Context) {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    
    private var temperatureSensorListener: SensorEventListener? = null

    var batteryLevel by mutableStateOf(100)
        private set
    
    var temperature by mutableStateOf(0f)
        private set

    init {
        updateBatteryLevel()
        setupTemperatureSensor()
    }

    private fun setupTemperatureSensor() {
        val temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
        temperatureSensorListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                temperature = event.values[0]
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
        
        temperatureSensor?.let { sensor ->
            sensorManager.registerListener(
                temperatureSensorListener,
                sensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    private fun updateBatteryLevel() {
        val batteryIntent = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        val level = batteryIntent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
        val scale = batteryIntent?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
        batteryLevel = ((level.toFloat() / scale.toFloat()) * 100).toInt()
    }

    fun getClothingSuggestionForTemperature(temp: Float): String {
        return when {
            temp >= 25 -> "It's warm! Wear light clothes like t-shirts, shorts and sandals"
            temp < 15 -> "It's cold! Wear warm clothes like jackets, sweaters, and boots"
            else -> "The temperature is normal. Wear casual clothes like jeans, t-shirts, and sneakers"
        }
    }

    fun checkBatteryStatus(): String {
        updateBatteryLevel()
        return if (batteryLevel <= 20) {
            "Warning: Battery level is low (${batteryLevel}%)"
        } else {
            "Battery level: ${batteryLevel}%"
        }
    }

    fun cleanup() {
        temperatureSensorListener?.let { listener ->
            sensorManager.unregisterListener(listener)
        }
    }
} 