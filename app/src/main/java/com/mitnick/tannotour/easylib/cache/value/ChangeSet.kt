package com.mitnick.tannotour.easylib.cache.value

/**
 * Created by mitnick on 2017/11/11.
 * Description
 */
class ChangeSet(var type: TYPE, var positionStart: Int = -1, var positionEnd: Int = -1){
    enum class TYPE{
        ADD,
        SET,
        REMOVE,
        CLEAR
    }
}