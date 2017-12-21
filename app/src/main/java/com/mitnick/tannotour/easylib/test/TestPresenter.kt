package com.mitnick.tannotour.easylib.test

import com.mitnick.tannotour.easylib.async.STATE
import com.mitnick.tannotour.easylib.cache.Cache
import com.mitnick.tannotour.easylib.net.CODE
import com.mitnick.tannotour.easylib.net.INet

/**
 * Created by mitnick on 2017/11/27.
 * Description
 */
class TestPresenter: INet {
    fun doPost(): STATE {
        /* 请求网络 */
        val response = get {
            url = "http://www.baidu.com/"
        }
        println("网络请求完成：\n${response.body()?.string()}")
        /* 写缓存 */
        Cache.use(YY::class.java){
            for (i in arrayOf(1, 2, 3, 4, 5)){
                add(TestCacheBean())
                println("添加了一个数据：$i")
            }
        }
        /* 返回执行结果 */
        if(response.code() == CODE.CODE_SUCCESSFUL){
            return STATE.SUCCESS
        }else{
            return STATE.FAILED
        }
    }
}