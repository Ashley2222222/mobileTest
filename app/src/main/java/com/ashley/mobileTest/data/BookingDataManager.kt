package com.ashley.mobileTest.data

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ashley.mobileTest.data.cache.BookingCache
import com.ashley.mobileTest.data.cache.BookingCacheImpl
import com.ashley.mobileTest.data.service.BookingService
import com.ashley.mobileTest.data.service.MobileTestServiceImpl
import com.ashley.mobileTest.model.Booking
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @description: 数据管理器，整合Service层和缓存层
 * @author: liangxy
 */
class BookingDataManager private constructor(context: Context) {
    private val TAG = "BookingDataManager"
    private val service: BookingService = MobileTestServiceImpl(context)
    private val cache: BookingCache = BookingCacheImpl(context)
    
    private val _mobileTestData = MutableLiveData<Booking?>()
    val mobileTestData: LiveData<Booking?> = _mobileTestData
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    /**
     * @description:  获取mobileTest数据
     *    1. 首先检查缓存是否有效
     *    2. 如果缓存有效，直接使用缓存数据
     *    3. 如果缓存无效或不存在，从Service获取新数据

     * @author: liangxy
     */
    fun fetchMobileTestData() {
        _isLoading.value = true
        _error.value = null
        
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // 检查缓存是否有效
                if (!cache.isDataExpired()) {
                    val cachedData = cache.getBookingData()
                    if (cachedData != null) {
                        withContext(Dispatchers.Main) {
                            _mobileTestData.value = cachedData
                            _isLoading.value = false
                            // 打印数据到控制台
                             Log.e(TAG,"Loaded mobileTest data from cache: $cachedData")
                        }
                        return@launch
                    }
                }
                
                // 缓存无效或不存在，从Service获取新数据
                val result = service.getBookingData()
                withContext(Dispatchers.Main) {
                    if (result.isSuccess) {
                        val mobileTest = result.getOrThrow()
                        _mobileTestData.value = mobileTest
                        // 保存到缓存
                        cache.saveBookingData(mobileTest)
                        // 打印数据到控制台
                         Log.e(TAG,"Loaded mobileTest data from service: $mobileTest")
                    } else {
                        val exception = result.exceptionOrNull()
                        _error.value = exception?.message ?: "Failed to load mobileTest data"
                        // 尝试使用缓存数据作为后备
                        val cachedData = cache.getBookingData()
                        if (cachedData != null) {
                            _mobileTestData.value = cachedData
                             Log.e(TAG,"Using cached data as fallback")
                        }
                    }
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _error.value = e.message ?: "Unknown error"
                    // 尝试使用缓存数据作为后备
                    val cachedData = cache.getBookingData()
                    if (cachedData != null) {
                        _mobileTestData.value = cachedData
                         Log.e(TAG,"Using cached data as fallback after error")
                    }
                    _isLoading.value = false
                }
            }
        }
    }

    /**
     * @description:  强制刷新数据，忽略缓存
     * @author: liangxy
     */
    fun refreshMobileTestData() {
        _isLoading.value = true
        _error.value = null
        
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = service.getBookingData()
                withContext(Dispatchers.Main) {
                    if (result.isSuccess) {
                        val mobileTest = result.getOrThrow()
                        _mobileTestData.value = mobileTest
                        // 保存到缓存
                        cache.saveBookingData(mobileTest)
                        // 打印数据到控制台
                        Log.e(TAG,"Refreshed mobileTest data: $mobileTest")
                    } else {
                        val exception = result.exceptionOrNull()
                        _error.value = exception?.message ?: "Failed to refresh mobileTest data"
                    }
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _error.value = e.message ?: "Unknown error"
                    _isLoading.value = false
                }
            }
        }
    }

    /**
     * @description:  清除缓存
     * @author: liangxy
     */
    fun clearCache() {
        cache.clearCache()
        Log.e(TAG,"Cache cleared")
    }
    
    companion object {
        @Volatile
        private var instance: BookingDataManager? = null

        /**
         * @description:获取单例实例
         * @author: liangxy
         */
        fun getInstance(context: Context): BookingDataManager {
            return instance ?: synchronized(this) {
                instance ?: BookingDataManager(context.applicationContext).also {
                    instance = it
                }
            }
        }
    }
}
