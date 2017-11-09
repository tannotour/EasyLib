package com.mitnick.tannotour.easylib.cache.list

import android.os.Looper
import com.mitnick.tannotour.easylib.cache.CacheObserver
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Created by mitnick on 2017/11/5.
 * Description
 */
interface CacheListValueObserver: CacheObserver {

    fun <T> onUpdate(key: String, cache: CopyOnWriteArrayList<T>?, changeSet: ChangeSet){
        if(Looper.myLooper() == Looper.getMainLooper()){
            onNotify(key, cache, changeSet)
        }else{
            doAsync {
                uiThread {
                    onNotify(key, cache, changeSet)
                }
            }
        }
    }

    fun <T> onNotify(key: String, cache: CopyOnWriteArrayList<T>?, changeSet: ChangeSet)
}