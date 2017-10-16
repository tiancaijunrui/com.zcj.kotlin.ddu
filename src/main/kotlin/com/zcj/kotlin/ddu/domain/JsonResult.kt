package com.zcj.kotlin.ddu.domain

/**
 * ajax请求返回结果的json对象
 */
class JsonResult {
    /**
     * 状态，默认为SUCCESS
     */
    var status = "SUCCESS"

    /**
     * 消息
     */
    var message: String? = null

    /**
     * 返回的数据
     */
    var data: Any? = null
}
