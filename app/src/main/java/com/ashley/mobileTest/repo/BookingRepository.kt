package com.ashley.mobileTest.repo

import android.content.Context
import com.ashley.mobileTest.model.Booking
import com.ashley.mobileTest.util.JsonFileUtil

/**
 * @description: 统一管理数据来源（本地文件/网络/数据库）
 * @author: liangxy
 */

class BookingRepository(private val context: Context) {
    /**
     * 获取本地booking数据
     */
    fun getBookingData(): Booking? {
        return JsonFileUtil.parseBookingData(context)
    }


}