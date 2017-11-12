package com.mitnick.tannotour.easylib

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.mitnick.tannotour.easylib.cache.CacheKey
import com.mitnick.tannotour.easylib.cache.YY

@CacheKey(keys = arrayOf(YY::class))
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
