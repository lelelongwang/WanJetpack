package com.longjunhao.wanjetpack

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * .WanJetpackApp
 *
 * @author Admitor
 * @date 2021/05/24
 */
@HiltAndroidApp
class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: Application
    }
}