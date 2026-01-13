package com.ashley.mobileTest.util

import android.content.Context
import com.ashley.mobileTest.model.Booking
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.IOException

/**
 * 本地JSON文件读取+解析工具类
 */
object JsonFileUtil {
    /**
     * @description: 读取assets下的JSON文件内容
     * @author: liangxy
     */
    fun Context.getJsonFromAssets(fileName: String): String? {
        return try {
            val inputStream = assets.open(fileName)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer, Charsets.UTF_8)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    /**
     * @description: 解析mobileTest.json为mobileTest对象
     * @author: liangxy
     */
    fun parseBookingData(context: Context): Booking? {
        // 1. 读取mobileTest.json内容
        val jsonString = context.getJsonFromAssets("booking.json")
        if (jsonString.isNullOrEmpty()) {
            return null
        }
        // 2. 使用Moshi解析
        return try {
            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
            val adapter = moshi.adapter(Booking::class.java)
            adapter.fromJson(jsonString)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}