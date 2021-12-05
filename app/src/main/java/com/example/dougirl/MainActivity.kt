package com.example.dougirl

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import com.example.dougirl.h5utils.JsMethods
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    @SuppressLint("AddJavascriptInterface", "SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        main_web.settings.javaScriptEnabled = true
        main_web.settings.userAgentString =
            "Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1"
        main_web.settings.loadWithOverviewMode = true
        //H5与Kotlin桥梁类通讯的桥梁类：第一个参数是被调用方法的对象，第二个参数是对象别名
        main_web.addJavascriptInterface(JsMethods(this), "jsInterface")
        main_web.webViewClient = MyWebViewClient()
        main_web.webChromeClient = MyWebChromeClient()
        main_web.loadUrl("https://1024shen.com")
        main_web.setDownloadListener { url, userAgent, contentDisposition, mimeType, contentLength ->
            //                // 处理下载事件
            //                val intent = Intent(Intent.ACTION_VIEW)
            //                intent.addCategory(Intent.CATEGORY_BROWSABLE)
            //                intent.data = Uri.parse(url)
            //                startActivity(intent)

            // 使用系统自带的下载任务
            if (contentDisposition != null) {
                if (url != null) {
                    if (mimeType != null) {
                        // 指定下载地址
                        val request = DownloadManager.Request(Uri.parse(url))
                        // 允许媒体扫描，根据下载的文件类型被加入相册、音乐等媒体库
                        request.allowScanningByMediaScanner()
                        // 设置通知的显示类型，下载进行时和完成后显示通知
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                        // 设置通知栏的标题，如果不设置，默认使用文件名
                        //        request.setTitle("This is title");
                        // 设置通知栏的描述
                        //        request.setDescription("This is description");
                        // 允许在计费流量下下载
                        request.setAllowedOverMetered(false)
                        // 允许该记录在下载管理界面可见
                        request.setVisibleInDownloadsUi(false)
                        // 允许漫游时下载
                        request.setAllowedOverRoaming(true)
                        // 允许下载的网路类型
                        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
                        // 设置下载文件保存的路径和文件名
                        val fileName = URLUtil.guessFileName(url, contentDisposition, mimeType)
                        request.setDestinationInExternalPublicDir(
                            Environment.DIRECTORY_DOWNLOADS,
                            fileName
                        )
                        // 另外可选一下方法，自定义下载路径
                        val downloadManager =
                            getSystemService(DOWNLOAD_SERVICE) as DownloadManager
                        // 添加一个下载任务
                        val downloadId = downloadManager.enqueue(request)
                    }
                }
            }
        }
    }

//    private fun downloadBySystem(url: String, contentDisposition: String, mimeType: String) {
//        // 指定下载地址
//        val request = DownloadManager.Request(Uri.parse(url))
//        // 允许媒体扫描，根据下载的文件类型被加入相册、音乐等媒体库
//        request.allowScanningByMediaScanner()
//        // 设置通知的显示类型，下载进行时和完成后显示通知
//        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
//        // 设置通知栏的标题，如果不设置，默认使用文件名
////        request.setTitle("This is title");
//        // 设置通知栏的描述
////        request.setDescription("This is description");
//        // 允许在计费流量下下载
//        request.setAllowedOverMetered(false)
//        // 允许该记录在下载管理界面可见
//        request.setVisibleInDownloadsUi(false)
//        // 允许漫游时下载
//        request.setAllowedOverRoaming(true)
//        // 允许下载的网路类型
//        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
//        // 设置下载文件保存的路径和文件名
//        val fileName = URLUtil.guessFileName(url, contentDisposition, mimeType)
//        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
//        //        另外可选一下方法，自定义下载路径
////        request.setDestinationUri()
////        request.setDestinationInExternalFilesDir()
//        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
//        // 添加一个下载任务
//        val downloadId = downloadManager.enqueue(request)
//    }

    fun isNetworkConnected(context: Context?): Boolean {
        if (context != null) {
            val mConnectivityManager = context
                .getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            val mNetworkInfo = mConnectivityManager.activeNetworkInfo
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable
            }
        }
        return false
    }


    inner class MyWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            return false
        }

        override fun doUpdateVisitedHistory(view: WebView?, url: String?, isReload: Boolean) {
            super.doUpdateVisitedHistory(view, url, isReload)
        }


        override fun onReceivedError(
            view: WebView?,
            request: WebResourceRequest?,
            error: WebResourceError?
        ) {
            super.onReceivedError(view, request, error)
            println("网页记载失败")
            // 让webview不显示
            // main_web.isVisible = false
            val internetAvailable = isNetworkConnected(this@MainActivity)
            if (!internetAvailable) {
                main_web.loadData(
                    "网络异常",
                    "text/html",
                    "UTF-8"
                )
            }
        }

    }


    inner class MyWebViewClient2 : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            return false
        }

        override fun doUpdateVisitedHistory(view: WebView?, url: String?, isReload: Boolean) {
            super.doUpdateVisitedHistory(view, url, isReload)
        }


        override fun onReceivedError(
            view: WebView?,
            request: WebResourceRequest?,
            error: WebResourceError?
        ) {
            super.onReceivedError(view, request, error)
            println("网页记载失败")
            // 让webview不显示
            // main_web.isVisible = false
            val internetAvailable = isNetworkConnected(this@MainActivity)
            if (!internetAvailable) {
                main_web.loadData(
                    "网络异常",
                    "text/html",
                    "UTF-8"
                )
            }
        }

    }

    // 创建一个ChromeClient
    inner class MyWebChromeClient : WebChromeClient() {

        lateinit var fullScreenView: View

        // 全屏显示
        override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
            super.onShowCustomView(view, callback)
            if (view != null) {
                fullScreenView = view
            }
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            main_content.addView(view)
        }

        // 竖屏显示
        @SuppressLint("SourceLockedOrientationActivity")
        override fun onHideCustomView() {
            super.onHideCustomView()
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            main_content.removeView(fullScreenView)
        }

        // 控制加载的进度条
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            val url = view?.url
            println("请求的URl是:$url")
            view?.loadUrl("javascript:function setTop(){document.getElementById('player').play();}setTop();")
            view?.loadUrl("javascript:function setTop(){document.getElementById('down').style.display=\"none\";}setTop();")
            super.onProgressChanged(view, newProgress)
        }

    }


}