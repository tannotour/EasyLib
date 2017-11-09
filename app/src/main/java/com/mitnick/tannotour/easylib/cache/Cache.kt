package com.mitnick.tannotour.easylib.cache

import com.google.gson.Gson
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by mitnick on 2017/11/9.
 * Description
 */
object Cache {

    private val caches: ConcurrentHashMap<String, CacheObject> = ConcurrentHashMap()
    private val observers: ConcurrentHashMap<String, ArrayList<CacheObserver>> = ConcurrentHashMap()

    private fun <T: CacheObject> restoreFromDisk(key: String, clazz: Class<out T>): String{

    }

    fun getObservers(key: String): ArrayList<CacheObserver>?{
        return observers[key]
    }

    fun addObserver(key: String, observer: CacheObserver){
        observers[key]?.add(observer)
    }



    fun <T: CacheObject> use(clazz: Class<out T>, call: T.() -> Unit): T{
        var cache: T
        val key = clazz.getAnnotation(CacheBean::class.java).key
//        val isList = clazz.getAnnotation(CacheBean::class.java).isList
//        val autoSync = clazz.getAnnotation(CacheBean::class.java).autoSync
        if(key.isEmpty()){
            throw Exception(clazz.name + " CacheBean(key)不可为空")
        }
        if(!caches.contains(key)){
            val json = restoreFromDisk(key, clazz)
//            val isList = clazz.getAnnotation(CacheBean::class.java).isList
            if(json.isEmpty()){
                cache = clazz.newInstance()
            }else{
                cache = Gson().fromJson(json, clazz)
            }
//            if(isList){
//                caches.put(key, )
//            }else{
//                caches.put(key, cache)
//            }
            caches.put(key, cache)
        }else{
            cache = caches[key] as T
        }
        cache.call()
        cache.notifyObserver()
        val autoSync = clazz.getAnnotation(CacheBean::class.java).autoSync
//        if(autoSync){
//            cache.sync()
//        }
        return cache
    }

}