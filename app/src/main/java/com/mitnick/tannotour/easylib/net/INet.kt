package com.mitnick.tannotour.easylib.net

import com.mitnick.tannotour.easylib.net.client.Http
import com.mitnick.tannotour.easylib.net.params.NetParams
import com.mitnick.tannotour.easylib.net.params.NetParamsPOST
import okhttp3.Response

/**
 * Created by mitnick on 2017/12/9.
 * Description
 */
interface INet {

    /**
     * GET方式请求服务器
     * @param call 参数
     */
    fun get(
            call: NetParams.() -> Unit
    ): Response {
        val netParams = NetParams()
        netParams.call()
        return Http.build {
            if(netParams.url.isNotEmpty()){
                url = netParams.url
            }
            if(netParams.headers.isNotEmpty()){
                netParams.headers.forEach {
                    headers.put(it.key, it.value)
                }
            }
            if(netParams.params.isNotEmpty()){
                url += "?"
                netParams.params.forEach {
                    url = url + it.key + "=" + it.value + "&"
                }
            }
        }.get()
    }

    /**
     * POST方式请求服务器
     * @param call 参数
     */
    fun post(
            call: NetParamsPOST.() -> Unit
    ): Response {
        val netParams = NetParamsPOST()
        netParams.call()
        return Http.build {
            if(netParams.url.isNotEmpty()){
                url = netParams.url
            }
            if(netParams.headers.isNotEmpty()){
                netParams.headers.forEach {
                    headers.put(it.key, it.value)
                }
            }
            if(netParams.params.isNotEmpty()){
                url += "?"
                netParams.params.forEach {
                    url = url + it.key + "=" + it.value + "&"
                }
            }
        }.post(
                netParams.data,
                netParams.dataKey,
                netParams.dataObj,
                netParams.files
        )
    }
}