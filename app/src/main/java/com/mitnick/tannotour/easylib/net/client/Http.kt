package com.mitnick.tannotour.easylib.net.client

import com.google.gson.Gson
import com.mitnick.tannotour.easylib.net.params.FileType
import okhttp3.*
import okhttp3.MultipartBody
import java.io.File
import java.util.*


/**
 * Created by mitnick on 2017/12/9.
 * Description http请求功能类
 */
class Http private constructor(
        var url: String = "",
        var cacheKey: String = "httpCache",
        val headers: HashMap<String, String> = HashMap()
){

    private var okHttpClient: OkHttpClient = OkHttpClient()

    /* 私有化构造方法 */
    private constructor(builder: Builder) : this(url = builder.url, headers = builder.headers)
    /* 伴生对象，对外提供静态的build方法 */
    companion object {
        fun build(init: Builder.() -> Unit) = Builder(init).build()
    }

    /**
     * GET方式请求数据
     */
    @Synchronized
    fun get(): Response{
        val builder = Request.Builder().url(url)
        headers.forEach {
            builder.addHeader(it.key, it.value)
        }
        val request = builder.build()
        val call = okHttpClient.newCall(request)
        return call.execute()
    }

    /**
     * POST方式请求数据（提交json）
     */
    @Synchronized
    fun post(
            data: HashMap<String, String> = HashMap(),
            dataKey: String = "data",
            dataObj: Any? = null,
//            fileKey: String = "file",
            files: Map<FileType, String> = HashMap()
    ): Response{
        val builder = Request.Builder().url(url)
        headers.forEach {
            builder.addHeader(it.key, it.value)
        }
//        val body = FormBody.Builder()
        val body = MultipartBody.Builder().setType(MultipartBody.FORM)
        data.forEach {
            body.addFormDataPart(it.key, it.value)
        }
        if(dataObj == null){
            body.addFormDataPart(dataKey, Gson().toJson(dataObj))
        }
        if(files.isNotEmpty()){
            files.forEach {
                val file = File(it.value)
                when(it.key){
                    FileType.TYPE_FILE -> {
                        body.addPart(
                                Headers.of("Content-Disposition", "form-data; name=\"file\";filename=\"file.${file.extension}\""),
                                RequestBody.create(
                                        MediaType.parse("text/plain"),
                                        file
                                )
                        )
                    }
                    FileType.TYPE_IMAGE -> {
                        body.addPart(
                                Headers.of("Content-Disposition", "form-data; name=\"file\";filename=\"file.${file.extension}\""),
                                RequestBody.create(
                                        MediaType.parse("image/png"),
                                        file
                                )
                        )
                    }
                }
            }
        }
        val request = builder.post(body.build()).build()
        val call = okHttpClient.newCall(request)
        return call.execute()
    }

    /**
     * 建造者
     */
    class Builder private constructor(){

        constructor(init: Builder.() -> Unit) : this() {
            init()
        }

        fun build() = Http(this)

        /* 请求地址 */
        var url: String = ""
        fun url(init: Builder.() -> String) = apply {
            url = init()
        }
        /* 缓存key */
        var cacheKey: String = "httpCache"
        fun cacheKey(init: Builder.() -> String) = apply {
            cacheKey = init()
        }
        /* 请求头 */
        val headers: HashMap<String, String> = HashMap()
        fun addHeaders(init: Builder.() -> HashMap<String, String>) = apply {
            init().forEach {
                headers.put(it.key, it.value)
            }
        }
        /* POST提交的数据 */
//        val data: HashMap<String, String> = HashMap()
//        fun setData(init: Builder.() -> HashMap<String, String>) = apply {
//            init().forEach{
//                data.put(it.key, it.value)
//            }
//        }
    }
}