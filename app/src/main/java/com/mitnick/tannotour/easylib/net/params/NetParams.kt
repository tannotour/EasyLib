package com.mitnick.tannotour.easylib.net.params


/**
 * Created by mitnick on 2017/12/13.
 * Description
 */
open class NetParams {
    var url: String = ""
    var headers: HashMap<String, String> = HashMap()
    var queryCache: Boolean = false
    /* GET提交的数据 */
    var params: HashMap<String, String> = HashMap()
}