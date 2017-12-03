package io.dotlearn.lrnplayer

import android.annotation.SuppressLint
import android.content.Context
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.webkit.ConsoleMessage
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import io.dotlearn.lrnplayer.error.LRNPlayerException
import io.dotlearn.lrnplayer.error.LRNPlayerNotPreparedException
import io.dotlearn.lrnplayer.listener.*
import io.dotlearn.lrnplayer.utils.DisplayUtils

/**
 * A custom view that plays vectorized videos
 */
class LRNPlayerView: FrameLayout, LRNPlayerContract.PlayerView {

    // region View Variables
    private lateinit var webView: WebView
    private lateinit var containerView: FrameLayout
    // endregion

    private var isPrepared = false
    private var isWebViewLoaded = false
    private var prepareRequest: PrepareRequest? = null
    private lateinit var webInterface: LRNPlayerWebInterface

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
    private fun init(attrs: AttributeSet?, defStyle: Int) {
        val layoutView = LayoutInflater.from(context).inflate(R.layout.layout_lrnplayer, this)
        containerView = layoutView.findViewById(R.id.lrn_container)
        webView = layoutView.findViewById(R.id.lrn_web_view)

        webInterface = LRNPlayerWebInterface(this)

        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true

        webView.addJavascriptInterface(webInterface, "Android")

        webView.webChromeClient = object: WebChromeClient() {

            override fun onConsoleMessage(consoleMessage: ConsoleMessage): Boolean {
                webInterface.log("Line no: ${consoleMessage.lineNumber()}," +
                        " Source id: ${consoleMessage.sourceId()}," +
                        " Message: ${consoleMessage.message()}")
                return true
            }

        }

        webView.webViewClient = object : WebViewClient() {

            override fun onPageFinished(view: WebView, url: String) {
                isWebViewLoaded = true
                prepare(prepareRequest)
            }

        }

        webView.loadUrl("file:///android_asset/base.html")
    }
    // endregion

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        prepare(prepareRequest)
    }

    fun debug(debug: Boolean) {
        webInterface.debug = debug
    }

    override fun prepare(accessToken: String, videoId: String, autoStart: Boolean,
                         onPrepareListener: OnPreparedListener) {
        webInterface.prepareListener = onPrepareListener
        val prepareRequest = PrepareRequest(accessToken, videoId, autoStart)

        // We need to wait for the View to be laid out before loading the video, so things like
        // get height and width will work.
        if(ViewCompat.isLaidOut(this)) {
            // The view has been laid out, load up the video
            prepare(prepareRequest)
        }
        else {
            // Save the prepare request to be loaded when the View is laid out
            this.prepareRequest = prepareRequest
        }
    }

    private fun prepare(prepareRequest: PrepareRequest?) {
        if(prepareRequest != null && isWebViewLoaded) {
            val widthHeightPair = calculateWidthAndHeight()
            loadVideo(prepareRequest, widthHeightPair.first, widthHeightPair.second)
            this.prepareRequest = null
        }
    }

    private fun calculateWidthAndHeight(): Pair<Int, Int> {
        val videoWidth = DisplayUtils.px2dp(context, width)
        val usableScreenHeight = DisplayUtils.getUsableScreenHeight(context)
        var videoHeight = DisplayUtils.calculateHeightBasedOnWidthAndAspectRatio(1.77777778,
                videoWidth)

        if (videoHeight > usableScreenHeight) {
            videoHeight = usableScreenHeight
        }

        return Pair(videoWidth, videoHeight)
    }

    private fun loadVideo(prepareRequest: PrepareRequest, videoWidth: Int, videoHeight: Int) {
        isPrepared = false

        val stringToLoad = """javascript:prepare(
                        "${prepareRequest.accessToken}", "${prepareRequest.videoId}",
                        ${prepareRequest.autoStart}, $videoWidth, $videoHeight);"""
        webInterface.log(stringToLoad)
        webView.loadUrl(stringToLoad)
    }

    fun onPrepared() {
        isPrepared = true
    }

    override fun start() {
        if(checkIsPrepared()) {
            webView.loadUrl("javascript:play();")
        }
    }

    override fun pause() {
        if(checkIsPrepared()) {
            webView.loadUrl("javascript:pause();")
        }
    }

    override fun seekTo(seekPos: Long) {
        if(checkIsPrepared()) {
            webView.loadUrl("""javascript:seekTo("$seekPos");""")
        }
    }

    override fun setOnCompletionListener(completionListener: OnPlaybackCompletionListener) {
        webInterface.completionListener = completionListener
    }

    override fun setOnErrorListener(errorListener: OnErrorListener) {
        webInterface.errorListener = errorListener
    }

    override fun setOnDownloadListener(downloadProgressListener: OnDownloadProgressListener) {
        webInterface.downloadProgressListener = downloadProgressListener
    }

    override fun setOnMetadataLoadedListener(metadataLoadedListener: OnMetadataLoadedListener) {
        webInterface.metadataLoadedListener = metadataLoadedListener
    }

    override fun release() {
        containerView.removeAllViews()
        webView.clearHistory()

        webView.clearCache(false)
        // Loading a blank page
        webView.loadUrl("about:blank")

        webView.onPause()
        webView.destroy()
    }

    // region Helper Methods
    private fun onError(e: LRNPlayerException) {
        webInterface.onError(e)
    }

    private fun checkIsPrepared(): Boolean {
        if(!isPrepared) {
            onError(LRNPlayerNotPreparedException("LRNPlayerView is not prepared. Call prepare before using vectorized video playback"))
            return false
        }

        return true
    }
    // endregion

}