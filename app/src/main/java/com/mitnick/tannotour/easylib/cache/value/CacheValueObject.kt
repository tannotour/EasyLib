package com.mitnick.tannotour.easylib.cache.value

import com.mitnick.tannotour.easylib.cache.Cache
import com.mitnick.tannotour.easylib.cache.CacheBean
import com.mitnick.tannotour.easylib.cache.CacheObject
import com.mitnick.tannotour.easylib.cache.list.CacheListValueObserver
import com.mitnick.tannotour.easylib.cache.list.ChangeSet
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Created by mitnick on 2017/11/5.
 * Description <T>(cacheKey: String, autoSync: Boolean = true)
 */
interface CacheValueObject: CacheObject {

    private fun getNewValue(): Any{
        return this
    }

    private fun getKey(): String{
        return this.javaClass.getAnnotation(CacheBean::class.java).key
    }

    private fun isList(): Boolean{
        return this.javaClass.getAnnotation(CacheBean::class.java).isList
    }

    override fun notifyObserver() {
        val observers = Cache.getObservers(getKey())
        if(isList()){
            observers?.forEach {
                val changeSet: ChangeSet = ChangeSet()
                (it as CacheListValueObserver).onUpdate(getKey(), getNewValue() as CopyOnWriteArrayList<*>, changeSet)
            }
        }else{
            observers?.forEach {
                (it as CacheValueObserver).onUpdate(getKey(), getNewValue())
            }
        }
    }

    override fun sync() {

    }
}