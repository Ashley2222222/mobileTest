package com.ashley.mobileTest.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ashley.mobileTest.model.Booking
import com.ashley.mobileTest.repo.BookingRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BookingViewModel(context: Context): ViewModel() {
    private val repository = BookingRepository(context)
    private val _mobileTestData = MutableLiveData<Booking?>()

    val mobileTestData : LiveData<Booking?> = _mobileTestData
        get() = field

    /**
     * @description: 加载mobileTest数据 (模拟网络请求)

     * @author: liangxy
     * @date:  16:55
     */
    fun loadBookingData() {
        viewModelScope.launch {
            // 模拟网络延迟
            delay(2000)
            val data = repository.getBookingData()
            _mobileTestData.value = data
        }
    }

}