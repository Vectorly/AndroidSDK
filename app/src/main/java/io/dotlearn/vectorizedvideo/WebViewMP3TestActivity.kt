package io.dotlearn.vectorizedvideo

import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.ConsoleMessage
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_web_view_mp3_test.*

class WebViewMP3TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view_mp3_test)

        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            webSettings.mediaPlaybackRequiresUserGesture = false
        }

        webView.webChromeClient = object : WebChromeClient() {

            override fun onConsoleMessage(consoleMessage: ConsoleMessage): Boolean {
                Log.d("Boom", "Line no: ${consoleMessage.lineNumber()}," +
                        " Source id: ${consoleMessage.sourceId()}," +
                        " Message: ${consoleMessage.message()}")
                return true
            }

        }

        webView.webViewClient = object : WebViewClient() {

            override fun onPageFinished(view: WebView, url: String) {

            }

        }

        webView.settings.allowFileAccess = true
        webView.settings.allowUniversalAccessFromFileURLs = true

        webView.loadUrl("file:///android_asset/test.html")
    }
}
