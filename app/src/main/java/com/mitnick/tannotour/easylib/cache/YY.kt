package com.mitnick.tannotour.easylib.cache

import com.mitnick.tannotour.easylib.TestCacheBean
import com.mitnick.tannotour.easylib.cache.value.CacheValueObject
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Created by mitnick on 2017/11/9.
 * Description
 */
@CacheBean(key = "user", isList = true, autoSync = true)
class YY: CopyOnWriteArrayList<TestCacheBean>(), CacheValueObject {

}