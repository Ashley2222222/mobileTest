package com.ashley.mobileTest

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner

class App : Application(), ViewModelStoreOwner {
    private lateinit var mAppViewModelStore: ViewModelStore
    private var mFactory: ViewModelProvider.Factory? = null

    companion object {
        private var sInstance: Application? = null

        /**
         * 获得当前app运行的Application
         */
        fun getInstance(): Application? {
            if (sInstance == null) {
                throw NullPointerException("not init Application")
            }
            return sInstance
        }
    }


    override fun onCreate() {
        super.onCreate()
        sInstance = this
        mAppViewModelStore = ViewModelStore()

    }



    override val viewModelStore: ViewModelStore
        get() {
            return mAppViewModelStore
        }

    /**
     * 获取一个全局的ViewModel
     */

    fun getAppViewModelProvider(): ViewModelProvider {
        return ViewModelProvider(this, this.getAppFactory())
    }


    private fun getAppFactory(): ViewModelProvider.Factory {
        if (mFactory == null) {
            mFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(this)
        }
        return mFactory as ViewModelProvider.Factory
    }



}

