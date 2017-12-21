package com.mitnick.tannotour.easylib.cache.disk

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.mitnick.tannotour.easylib.cache.disk.lru.DiskLruCache
import com.mitnick.tannotour.easylib.cache.disk.lru.Util
import java.io.BufferedOutputStream
import java.io.ByteArrayOutputStream
import java.io.IOException

/**
 * Created by mitnick on 2017/11/13.
 * Description
 */
class Disk(var uniqueName: String = "cache", var maxSize: Long = (10 * 1024 * 1024).toLong()): DiskCache {

    private val TAG = "Disk"

    var mDiskLruCache: DiskLruCache? = null

    override fun init(context: Context){
        try {
            val cacheDir = Util.getDiskCacheDir(context, uniqueName)
            if (!cacheDir.exists()) {
                cacheDir.mkdirs()
            }
            mDiskLruCache = DiskLruCache.open(cacheDir, Util.getAppVersion(context), 1, maxSize)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        Log.e(TAG, "硬盘缓存初始化完毕。")
    }

    private fun getStreamValue(snapShot: DiskLruCache.Snapshot, index: Int): String{
        val value: String
        val inStream = snapShot.getInputStream(index)
        val baos = ByteArrayOutputStream()
        var i = inStream.read()
        while (i != -1){
            baos.write(i)
            i = inStream.read()
        }
        if(baos.size() == 0){
            value = ""
        }else{
            value = baos.toString()
        }
        baos.close()
        inStream.close()
        return value
    }

    private fun writeStreamValue(value: String, editor: DiskLruCache.Editor, index: Int){
        val byteArray: ByteArray = value.toByteArray()
        val outputStream = editor.newOutputStream(index)
        val out: BufferedOutputStream = BufferedOutputStream(outputStream, byteArray.size)
        out.write(byteArray)
        editor.commit()
        out.close()
    }

    fun preDealKey(key: String): String{
        return key.replace(".", "")
    }

    override fun readFromDisk(key: String, call: ((ok: Boolean, key: String, json: String) -> Unit)?): String? {
        var value: String = ""
        val snapShot = mDiskLruCache?.get(preDealKey(key))
        if (snapShot != null) {
            value = getStreamValue(snapShot, 0)
        }
        Log.e(TAG, "在硬盘中读取到key=$key,value=$value 的数据。")
        return value
    }

    override fun writeToDisk(key: String, obj: Any?, call: ((ok: Boolean, key: String, obj: Any?) -> Unit)?): Boolean {
        val editor = mDiskLruCache?.edit(preDealKey(key))
        if(editor != null){
            val value = Gson().toJson(obj)
            writeStreamValue(value, editor, 0)
            Log.e(TAG, "向硬盘写入key=${preDealKey(key)},value=$value 的数据。")
            return true
        }else{
            Log.e(TAG, "向硬盘写入数据失败，editor为null。")
            return false
        }
    }

    override fun remove(key: String?): String {
        val size = totalSize()
        if(key == null){
            mDiskLruCache?.delete()
            Log.e(TAG, "清空全部硬盘缓存数据。")
        }else{
            mDiskLruCache?.remove(preDealKey(key))
            Log.e(TAG, "删除key=${preDealKey(key)} size=$size 的硬盘缓存数据。")
        }
        return size
    }

    override fun totalSize(): String {
        val size = formetFileSize(mDiskLruCache?.size())
        Log.e(TAG, "硬盘缓存数据总大小为$size。")
        return size
    }

    override fun flush() {
        mDiskLruCache?.flush()
    }

}