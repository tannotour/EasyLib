package com.mitnick.tannotour.easylib.async

import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.UI
import org.jetbrains.anko.doAsync

/**
 * Created by mitnick on 2017/11/14.
 * Description
 */

object Async{
    val presenters: HashMap<String, Presenter> = HashMap()
}

class Presenter(var presenter: Any){
    var useing: Boolean = false
}

enum class STATE{
    SUCCESS, FAILED
}

//val Any.SUCCESS: String
//    get() = "0"
//
//val Any.FAILED: String
//    get() = "-1"

//@Synchronized fun <T: Any> Any.task(clazz: Class<T>, call: T.() -> STATE): STATE{
//    val presenter: Presenter
//    if(Async.presenters.containsKey(clazz.name)){
//        presenter = Async.presenters[clazz.name]!!
//    }else{
//        presenter = Presenter(clazz.newInstance())
//        Async.presenters.put(clazz.name, presenter)
//    }
//    var ret: STATE = STATE.FAILED
//    doAsync {
//        if(!presenter.useing){
//            presenter.useing = true
//            ret = (presenter.presenter as T).call()
//            presenter.useing = false
//        }else{
//            ret = clazz.newInstance().call()
//        }
//    }
//    return ret
//}

//suspend fun wait(presenter: Presenter): Boolean{
//    while (presenter.useing){
//        delay(200)
//    }
//    return true
//}
//
//suspend fun execute(call: ()->STATE): STATE{
//    return call.invoke()
//}

fun wait(presenter: Presenter): Deferred<Any> = async(CommonPool) {
    while (presenter.useing){
        delay(200)
    }
    return@async presenter.presenter
}

fun execute(call: ()->STATE): Deferred<STATE> = async(CommonPool) {
    return@async call.invoke()
}

fun <T: Any> Any.task(clazz: Class<T>, call: T.() -> STATE): STATE{
    return runBlocking {
        val presenterf: Presenter
        if(Async.presenters.containsKey(clazz.name)){
            presenterf = Async.presenters[clazz.name]!!
        }else{
            presenterf = Presenter(clazz.newInstance())
            Async.presenters.put(clazz.name, presenterf)
        }
        val presenter = wait(presenterf).await()
        return@runBlocking execute { (presenter as T).call() }.await()
    }
}

//fun <T: Any> Any.task(clazz: Class<T>, call: T.() -> STATE): STATE{
//    val presenter: Presenter
//    if(Async.presenters.containsKey(clazz.name)){
//        presenter = Async.presenters[clazz.name]!!
//    }else{
//        presenter = Presenter(clazz.newInstance())
//        Async.presenters.put(clazz.name, presenter)
//    }
//    var ret: STATE = STATE.FAILED
//    doAsync {
//        if(!presenter.useing){
//            presenter.useing = true
//            ret = (presenter.presenter as T).call()
//            presenter.useing = false
//        }else{
//            ret = clazz.newInstance().call()
//        }
//    }
//    return ret
//}
