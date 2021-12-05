package com.example.dougirl.h5utils

import android.content.Context
import android.webkit.JavascriptInterface

// Kotlin与H5通讯的桥梁类
class JsMethods(context:Context) {

    // 上下们对象
    private var mContext = context

    @JavascriptInterface // 安卓4.2之后都要加
    fun shouToast(json:String) {
        // 第一种方式弹出
//        Toast.makeText(mContext, json, Toast.LENGTH_SHORT).show()
        println("获得的网页源代码是:")
        println(json)
    }

    @JavascriptInterface // 安卓4.2之后都要加
    fun getHome() {
    }

}