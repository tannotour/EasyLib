package com.mitnick.tannotour.easylib.cache.disk

import android.content.Context
import java.text.DecimalFormat

/**
 * Created by mitnick on 2017/11/10.
 * Description
 */
interface DiskCache {

    fun init(context: Context)

    fun readFromDisk(key: String, call:((ok: Boolean, key: String, json: String) -> Unit)? = null): String?

    fun writeToDisk(key: String, obj: Any?, call: ((ok: Boolean, key: String, obj: Any?) -> Unit)? = null): Boolean

    fun remove(key: String? = null): String

    fun totalSize(): String

    fun flush()

    fun formetFileSize(fileS: Long?): String {
        if(fileS == null){
            return "0B"
        }
        val df = DecimalFormat("#.00")
        var fileSizeString: String
        if (fileS < 1024) {
            fileSizeString = df.format(fileS.toDouble()) + "B"
        } else if (fileS < 1048576) {
            fileSizeString = df.format(fileS.toDouble() / 1024) + "K"
        } else if (fileS < 1073741824) {
            fileSizeString = df.format(fileS.toDouble() / 1048576) + "M"
        } else {
            fileSizeString = df.format(fileS.toDouble() / 1073741824) + "G"
        }
        return fileSizeString
    }
}