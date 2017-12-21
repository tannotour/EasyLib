package com.mitnick.tannotour.easylib.test

import com.mitnick.tannotour.easylib.async.Funcs
import com.mitnick.tannotour.easylib.async.STATE
import com.mitnick.tannotour.easylib.async.task

/**
 * Created by mitnick on 2017/12/20.
 * Description
 */
interface TestFuncs : Funcs {
    fun testFunc(once: Boolean = false, call: TestPresenter.() -> STATE) = task(TestPresenter::class.java, once, call)
}