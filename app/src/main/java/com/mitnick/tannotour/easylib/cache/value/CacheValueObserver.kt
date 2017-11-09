package com.mitnick.tannotour.easylib.cache.value

import android.os.Looper
import com.mitnick.tannotour.easylib.cache.CacheObserver
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * Created by mitnick on 2017/11/5.
 * Description
 */
interface CacheValueObserver: CacheObserver {

    fun <T> onUpdate(key: String, newValue: T?){
        if(Looper.getMainLooper() == Looper.myLooper()){
            onNotify(key, newValue)
        }else{
            doAsync {
                uiThread {
                    onNotify(key, newValue)
                }
            }
        }
    }

    fun <T> onNotify(key: String, newValue: T?)
}