package com.mitnick.tannotour.easylib.cache


/**
 * Created by mitnick on 2017/11/5.
 * Description
 */
interface CacheObject {

    fun notifyObserver()
}
//abstract class CacheObject(val cacheKey: String, var autoSync: Boolean) {
//
////    abstract protected fun init(json: String)
//
//    /**
//     * 将数据从Disk取出到内存
//     */
////    fun restoreFromDisk(): String{
////        return ""
////    }
//
//    /**
//     * 将数据同步到Disk上
//     */
//    fun sync(){
//        Cache.use(CacheValueObject::class.java){
//
//        }
//    }
//}

