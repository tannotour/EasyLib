package com.mitnick.tannotour.easylib.net

import com.google.gson.Gson
import okhttp3.Response

/**
 * Created by mitnick on 2017/12/13.
 * Description
 */

inline fun <reified T> Response.toObj(): Obj<T>{
    val body = body()
    if (body == null) {
        return Obj(
                code(),
                isSuccessful,
                isRedirect,
                null
        )
    }else{
        return Obj(
                code(),
                isSuccessful,
                isRedirect,
                Gson().fromJson(body.string(), T::class.java)
        )
    }
}

data class Obj<T>(
        var code: Int,
        var isSuccessful: Boolean,
        var isRedirect: Boolean,
        var body: T?
)

class CODE{
    companion object {
        val CODE_SUCCESSFUL = 200
    }
}