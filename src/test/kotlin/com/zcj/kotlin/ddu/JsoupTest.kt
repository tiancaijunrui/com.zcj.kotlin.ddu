package com.zcj.kotlin.ddu

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

/**
 * @Since2017/11/15 ZhaCongJie@HF
 */
fun main(args: Array<String>) {
    val html: Document = Jsoup.connect("http://www.baidu.com").get()
    println(html)
}