package com.ashley.mobileTest.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ashley.mobileTest.viewmodel.BookingViewModel

class BookingActivity: AppCompatActivity() {
    private lateinit var  viewModel: BookingViewModel


    private class BookingViewModelFactory(private val context: android.content.Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(BookingViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return BookingViewModel(context) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 初始化ViewModel
        viewModel = ViewModelProvider(this, BookingViewModelFactory(this))[BookingViewModel::class.java]
        // 加载数据
        viewModel.loadBookingData()

        setContent {
            MaterialTheme {
                BookingDetailScreen(viewModel = viewModel)
            }
        }
    }

}