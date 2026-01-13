package com.ashley.mobileTest.data.cache

import android.content.Context
import android.content.SharedPreferences
import com.ashley.mobileTest.model.Booking
import com.google.gson.Gson
import androidx.core.content.edit

/**
 * @description: 缓存层接口
 * @author: liangxy
 */
interface BookingCache {
    fun saveBookingData(mobileTest: Booking)
    fun getBookingData(): Booking?
    fun isDataExpired(): Boolean
    fun clearCache()
}

/**
 * @description: 缓存层实现，使用SharedPreferences存储数据
 * @author: liangxy
 */
class BookingCacheImpl(context: Context) : BookingCache {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("mobileTest_cache", Context.MODE_PRIVATE)
    private val gson = Gson()
    
    // 数据过期时间（毫秒），这里设置为1分钟
    private val EXPIRY_TIME_MS = 1 * 60 * 1000L
    
    override fun saveBookingData(mobileTest: Booking) {
        sharedPreferences.edit {
            val mobileTestJson = gson.toJson(mobileTest)
            putString("booking_data", mobileTestJson)
            putLong("timestamp", System.currentTimeMillis())
        }
    }
    
    override fun getBookingData(): Booking? {
        val mobileTestJson = sharedPreferences.getString("booking_data", null)
        return if (mobileTestJson != null) {
            try {
                gson.fromJson(mobileTestJson, Booking::class.java)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        } else {
            null
        }
    }
    
    override fun isDataExpired(): Boolean {
        val timestamp = sharedPreferences.getLong("timestamp", 0)
        return System.currentTimeMillis() - timestamp > EXPIRY_TIME_MS
    }
    
    override fun clearCache() {
        sharedPreferences.edit { clear() }
    }
}
