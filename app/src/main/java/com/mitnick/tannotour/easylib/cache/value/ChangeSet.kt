package com.mitnick.tannotour.easylib.cache.value

/**
 * Created by mitnick on 2017/11/11.
 * Description
 */
class ChangeSet(val sign: Int = -1){
    companion object {
        val NULL = -2
        val SIGN_CHANGE = 0
        val SIGN_INSERT = 1
        val SIGN_REMOVE = 2
        val SIGN_CLEAR_INSERT = 3
    }
    var changeIndex: Int = NULL
    var removeIndex: Int = NULL
    var removeLens: Int = 1
    var insertIndex: Int = NULL
    var insertLens: Int = 1

    override fun toString(): String {
        return "ChangeSet(sign=$sign, changeIndex=$changeIndex, removeIndex=$removeIndex, removeLens=$removeLens, insertIndex=$insertIndex, insertLens=$insertLens)"
    }
}