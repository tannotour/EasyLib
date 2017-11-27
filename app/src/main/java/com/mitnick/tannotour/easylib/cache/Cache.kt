package com.mitnick.tannotour.easylib.cache

import android.util.Log
import com.google.gson.Gson
import com.mitnick.tannotour.easylib.cache.disk.Disk
import com.mitnick.tannotour.easylib.cache.disk.DiskCache
import com.mitnick.tannotour.easylib.cache.value.CacheListValueObserver
import com.mitnick.tannotour.easylib.cache.value.CacheValueObserver
import com.mitnick.tannotour.easylib.cache.value.ChangeSet
import org.jetbrains.anko.doAsync
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Created by mitnick on 2017/11/9.
 * Description
 */
object Cache {
    private val TAG = "Cache"

    private val caches: ConcurrentHashMap<String, Any> = ConcurrentHashMap()
    private val observers: ConcurrentHashMap<String, ArrayList<CacheObserver>> = ConcurrentHashMap()
    private var disk: DiskCache = Disk()

    fun setDisk(diskCache: DiskCache){
        disk = diskCache
    }

    fun flush(){
        disk.flush()
    }

    fun addObserver(observer: CacheObserver): Boolean{
        if(!observer.javaClass.isAnnotationPresent(CacheKey::class.java)){
            Log.e(TAG, "数据源观察者${observer::javaClass.name}未使用CacheKey注解")
            return false
        }
        doAsync {
            observer.javaClass.getAnnotation(CacheKey::class.java).keys.forEach {
                val key = it::class.java.name
                if(!observers.contains(key)){
                    observers.put(key, ArrayList<CacheObserver>())
                }
                observers[key]?.add(observer)

                val cache: Any
                if(!caches.contains(key)){
                    val json = disk.readFromDisk(key)
                    if(json == null || json.isEmpty()){
                        cache = it::class.java.newInstance()
                    }else{
                        cache = Gson().fromJson(json, it::class.java)
                    }
                    caches.put(key, cache)
                }else{
                    cache = caches[key]!!
                }
                val anno = cache::class.java.getAnnotation(CacheBean::class.java)
                if(anno == null){
                    Log.e(TAG, "无法获取初始化Cache(${cache::class.java.name})数据，绑定时初始化失败，请添加CacheKey注解。")
                }else{
                    notifyObserver(key, anno.isList, cache, observer)
                }
            }
        }
        return true
    }

    fun removeObserver(observer: CacheObserver): Boolean{
        if(!observer.javaClass.isAnnotationPresent(CacheKey::class.java)){
            return false
        }
        observer.javaClass.getAnnotation(CacheKey::class.java).keys.forEach {
            observers[it::class.java.name]?.remove(observer)
            if(observers[it::class.java.name]?.isEmpty() as Boolean){
                observers.remove(it::class.java.name)
            }
        }
        return true
    }

    fun notifyObserver(key: String, isList: Boolean, cache: Any){
//        if(isList){
//            observers[key]?.forEach {
//                val changeSet: ChangeSet = ChangeSet()
//                (it as CacheListValueObserver).onUpdate(key, cache as CopyOnWriteArrayList<*>, changeSet)
//            }
//        }else{
//            observers[key]?.forEach {
//                (it as CacheValueObserver).onUpdate(key, cache)
//            }
//        }
        observers[key]?.forEach {
            notifyObserver(key, isList, cache, it)
        }
    }

    fun notifyObserver(key: String, isList: Boolean, cache: Any, observer: CacheObserver){
        if(isList){
            val changeSet: ChangeSet = ChangeSet(ChangeSet.SIGN_CLEAR_INSERT)
            val cacheTrue = (cache as CopyOnWriteArrayList<*>)
            changeSet.insertIndex = 0
            changeSet.insertLens = cacheTrue.size
            (observer as CacheListValueObserver).onUpdate(key, cacheTrue, changeSet)
        }else{
            (observer as CacheValueObserver).onUpdate(key, cache)
        }
    }

    fun sync(cache: Any){
        if(!cache.javaClass.isAnnotationPresent(CacheBean::class.java)){
            throw Exception(cache.javaClass.name + " 同步缓存失败,缓存数据类必须使用CacheBean注解")
        }
//        val key = cache.javaClass.getAnnotation(CacheBean::class.java).key
        val key = cache.javaClass.name
        disk.writeToDisk(key, cache)
    }

    fun <T: Any> use(clazz: Class<out T>, call: T.() -> Unit): T{
        if(!clazz.isAnnotationPresent(CacheBean::class.java)){
            throw Exception(clazz.name + " 缓存数据类必须使用CacheBean注解")
        }
        val cache: T
        val key = clazz.name
        if(key.isEmpty()){
            throw Exception(clazz.name + " CacheBean(key)不可为空")
        }
        if(!caches.contains(key)){
            val json = disk.readFromDisk(key)
            if(json == null || json.isEmpty()){
                cache = clazz.newInstance()
            }else{
                cache = Gson().fromJson(json, clazz)
            }
            caches.put(key, cache)
        }else{
            cache = caches[key] as T
        }
        cache.call()
        val annotation = clazz.getAnnotation(CacheBean::class.java)
        notifyObserver(key, annotation.isList, cache)
        if(annotation.autoSync){
            sync(cache)
        }
        return cache
    }
}