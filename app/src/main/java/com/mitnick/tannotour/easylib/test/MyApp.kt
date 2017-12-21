package com.mitnick.tannotour.easylib.test

import android.app.Application
import com.mitnick.tannotour.easylib.LibInit

/**
 * Created by mitnick on 2017/12/15.
 * Description
 */
class MyApp: Application() {
    override fun onCreate() {
        super.onCreate()
        LibInit.appInit(this)
    }
}