package com.mitnick.tannotour.easylib.cache

import com.mitnick.tannotour.easylib.TestCacheBean
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Created by mitnick on 2017/11/9.
 * Description key = "user",
 */
@CacheBean(isList = true, autoSync = true)
class YY: CopyOnWriteArrayList<TestCacheBean>() {

}