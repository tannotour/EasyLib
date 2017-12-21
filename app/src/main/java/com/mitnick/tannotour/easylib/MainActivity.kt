package com.mitnick.tannotour.easylib

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.mitnick.tannotour.easylib.async.STATE
import com.mitnick.tannotour.easylib.cache.CacheKey
import com.mitnick.tannotour.easylib.cache.CacheList
import com.mitnick.tannotour.easylib.test.YY
import com.mitnick.tannotour.easylib.cache.value.CacheListValueObserver
import com.mitnick.tannotour.easylib.test.TestFuncs
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast

@CacheKey(keys = arrayOf(YY::class))
class MainActivity : AppCompatActivity(), CacheListValueObserver, TestFuncs {

    override fun <T> onNotify(key: String, cache: CacheList<T>?) {
        toast("收到了缓存通知${cache?.size}")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainActivityTextView.setOnClickListener {
            toast("开始执行耗时任务")
            if(testFunc{
                doPost()
            } == STATE.SUCCESS){
                toast("网络请求成功")
            }else{
                toast("网络请求失败")
            }
        }
    }
}
