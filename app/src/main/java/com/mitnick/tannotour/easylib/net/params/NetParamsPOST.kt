package com.mitnick.tannotour.easylib.net.params

import java.util.HashMap

/**
 * Created by mitnick on 2017/12/15.
 * Description
 */
class NetParamsPOST: NetParams() {
    /* 提交的文本数据 */
    val data: HashMap<String, String> = HashMap()
    /* 提交的对象的键 */
    var dataKey: String = "data"
    /* 提交的对象的值 */
    var dataObj: Any? = null
    /* 提交的文件路径列表 */
    val files: Map<FileType, String> = HashMap()
}