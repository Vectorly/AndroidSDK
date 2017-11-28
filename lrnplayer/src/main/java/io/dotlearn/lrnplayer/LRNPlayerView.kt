package io.dotlearn.lrnplayer

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.webkit.ConsoleMessage
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import io.dotlearn.lrnplayer.error.ErrorCode
import io.dotlearn.lrnplayer.listener.OnDownloadProgressListener
import io.dotlearn.lrnplayer.listener.OnErrorListener
import io.dotlearn.lrnplayer.listener.OnPlaybackCompletionListener
import io.dotlearn.lrnplayer.listener.OnPreparedListener
import io.dotlearn.lrnplayer.utils.DisplayUtils

/**
 * A custom view that plays vectorized videos
 */
class LRNPlayerView: FrameLayout, LRNPlayerContract.PlayerView {

    // region View Variables
    private lateinit var mWebView: WebView
    // endregion

    private var mIsPrepared = false
    private lateinit var mWebInterface: LRNPlayerWebInterface

    // region View Init
    constructor(context:Context) : super(context) {
        init(null, 0)
    }

    constructor(context:Context, attrs:AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context:Context, attrs:AttributeSet, defStyle:Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }

    @SuppressLint("SetJavaScriptEnabled", "AddJavascriptInterface")
    private fun init(attrs:AttributeSet?, defStyle:Int) {
        // Load attributes
        /*val a = context.obtainStyledAttributes(attrs, R.styleable.LRNPlayerView, defStyle, 0)

        val showProgress = a.getBoolean(R.styleable.LRNPlayerView_showProgress, true)
        a.recycle()*/

        val layoutView = LayoutInflater.from(context).inflate(R.layout.layout_lrnplayer, this)
        mWebView = layoutView.findViewById(R.id.lrn_web_view)

        mWebInterface = LRNPlayerWebInterface(this)

        val webSettings = mWebView.settings
        webSettings.javaScriptEnabled = true
        /*webSettings.loadWithOverviewMode = true
        webSettings.useWideViewPort = true*/
        mWebView.addJavascriptInterface(mWebInterface, "Android")
    }
    // endregion

    fun debug(debug: Boolean) {
        mWebInterface.debug = debug
    }

    override fun prepare(accessToken: String, videoId: String, autoStart: Boolean,
                onPrepareListener: OnPreparedListener) {
        mWebInterface.prepareListener = onPrepareListener

        val width = DisplayUtils.px2dp(context,DisplayUtils.getScreenWidth(context))
        val height = DisplayUtils.getHeightToMaintain169AspectRatio(width)

        mWebView.webViewClient = object : WebViewClient() {

            override fun onPageFinished(view: WebView, url: String) {
                val stringToLoad = "javascript:prepare(\"$accessToken\", \"$videoId\", " +
                        " $autoStart, $width, $height)"
                mWebView.loadUrl(stringToLoad)
            }

        }

        mWebView.webChromeClient = object: WebChromeClient() {

            override fun onConsoleMessage(consoleMessage: ConsoleMessage): Boolean {
                mWebInterface.log("Line no: ${consoleMessage.lineNumber()}," +
                        " Source id: ${consoleMessage.sourceId()}," +
                        " Message: ${consoleMessage.message()}")
                return true
            }

        }

        mWebView.loadUrl("file:///android_asset/base.html")
    }

    override fun start() {
        checkIsPrepared()

        mWebView.loadUrl("javascript:start()")
    }

    override fun pause() {
        checkIsPrepared()

        mWebView.loadUrl("javascript:pause()")
    }

    override fun seekTo(seekPos: Long) {
        checkIsPrepared()

        mWebView.loadUrl("javascript:seekTo(\"$seekPos\")")
    }

    override fun setOnCompletionListener(completionListener: OnPlaybackCompletionListener) {
        mWebInterface.completionListener = completionListener
    }

    override fun setOnErrorListener(errorListener: OnErrorListener) {
        mWebInterface.errorListener = errorListener
    }

    override fun setOnDownloadListener(downloadProgressListener: OnDownloadProgressListener) {
        mWebInterface.downloadProgressListener = downloadProgressListener
    }

    // region Helper Methods
    private fun onError(errorCode: ErrorCode) {
        mWebInterface.onError(errorCode)
    }

    private fun checkIsPrepared() {
        if(!mIsPrepared) {
            onError(ErrorCode.NOT_PREPARED)
        }
    }
    // endregion

}