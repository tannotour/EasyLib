package com.mitnick.tannotour.easylib.cache.value

import android.os.Looper
import com.mitnick.tannotour.easylib.cache.CacheList
import com.mitnick.tannotour.easylib.cache.CacheObserver
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * Created by mitnick on 2017/11/5.
 * Description
 */
interface CacheListValueObserver: CacheObserver {

    fun <T> onUpdate(key: String, cache: CacheList<T>?){
        if(Looper.myLooper() == Looper.getMainLooper()){
            onNotify(key, cache)
        }else{
            doAsync {
                uiThread {
                    onNotify(key, cache)
                }
            }
        }
    }

    fun <T> onNotify(key: String, cache: CacheList<T>?)
}