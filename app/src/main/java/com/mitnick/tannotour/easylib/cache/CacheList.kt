package com.mitnick.tannotour.easylib.cache

import com.mitnick.tannotour.easylib.cache.value.ChangeSet
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Created by mitnick on 2017/12/16.
 * Description
 */
abstract class CacheList<T>: CopyOnWriteArrayList<T>() {

    /* 更新记录 */
    val changes: LinkedList<ChangeSet> = LinkedList()

    override fun add(element: T): Boolean {
        changes.add(ChangeSet(ChangeSet.TYPE.ADD, size))
        return super.add(element)
    }

    override fun add(index: Int, element: T) {
        changes.add(ChangeSet(ChangeSet.TYPE.ADD, index))
        super.add(index, element)
    }

    override fun set(index: Int, element: T): T {
        changes.add(ChangeSet(ChangeSet.TYPE.SET, index))
        return super.set(index, element)
    }

    override fun removeAt(index: Int): T {
        changes.add(ChangeSet(ChangeSet.TYPE.REMOVE, index))
        return super.removeAt(index)
    }

    override fun remove(element: T): Boolean {
        val index = indexOf(element)
        changes.add(ChangeSet(ChangeSet.TYPE.REMOVE, index))
        return super.remove(element)
    }

    fun clearRecord(){
        changes.clear()
    }
}