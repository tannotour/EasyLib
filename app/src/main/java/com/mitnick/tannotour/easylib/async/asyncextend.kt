package com.mitnick.tannotour.easylib.async

import kotlinx.coroutines.experimental.*
//import kotlinx.coroutines.experimental.android.UI
//import org.jetbrains.anko.doAsync

/**
 * Created by mitnick on 2017/11/14.
 * Description
 */

object Async{
    val presenters: HashMap<String, Presenter> = HashMap()
    val errors: HashMap<String, String> = HashMap()
}

class Presenter(var presenter: Any){
    var useing: Boolean = false
}

enum class STATE{
    SUCCESS, FAILED
}

fun wait(presenter: Presenter): Deferred<Any> = async(CommonPool) {
    while (presenter.useing){
        delay(200)
    }
    return@async presenter.presenter
}

fun execute(call: ()->STATE): Deferred<STATE> = async(CommonPool) {
    return@async call.invoke()
}

fun <T: Any> Funcs.task(clazz: Class<T>, once: Boolean = false, call: T.() -> STATE): STATE{
    return runBlocking {
        val presenterf: Presenter
        if(Async.presenters.containsKey(clazz.name)){
            presenterf = Async.presenters[clazz.name]!!
            if(once){
                Async.presenters.remove(clazz.name)
            }
        }else{
            presenterf = Presenter(clazz.newInstance())
            if(!once){
                Async.presenters.put(clazz.name, presenterf)
            }
        }
        var presenter: Any? = wait(presenterf).await()
        presenterf.useing = true
        val result = execute { (presenter as T).call() }.await()
        presenterf.useing = false
        if(once){
            presenter = null
        }
        return@runBlocking result
    }
}

fun <T: Any> Funcs.writeError(clazz: Class<T>, errorMsg: String){
    Async.errors.put(clazz.name, errorMsg)
}

fun <T: Any> Any.error(clazz: Class<T>, autoClear: Boolean = true): String{
    if(Async.errors.containsKey(clazz.name)){
        val error = Async.errors[clazz.name]!!
        if(autoClear){
            Async.errors.remove(clazz.name)
        }
        return error
    }else{
        return ""
    }
}
