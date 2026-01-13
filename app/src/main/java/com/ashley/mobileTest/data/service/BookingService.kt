package com.ashley.mobileTest.data.service

import android.content.Context
import com.ashley.mobileTest.model.Booking
import com.ashley.mobileTest.util.JsonFileUtil
import kotlinx.coroutines.delay

/**
 * @description: 服务层接口
 * @author: liangxy
 */
interface BookingService {
    suspend fun getBookingData(): Result<Booking>
}
/**
 * @description: 服务层实现，使用本地JSON文件作为mock数据
 * @author: liangxy
 */
class MobileTestServiceImpl(private val context: Context) : BookingService {
    override suspend fun getBookingData(): Result<Booking> {
        // 模拟网络延迟
        delay(1500)
        
        return try {
            val bookingData = JsonFileUtil.parseBookingData(context)
            if (bookingData != null) {
                Result.success(bookingData)


            } else {
                Result.failure(Exception("Failed to parse mobileTest data"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
