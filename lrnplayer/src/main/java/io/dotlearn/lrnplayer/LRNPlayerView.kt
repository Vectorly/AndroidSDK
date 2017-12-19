package io.dotlearn.lrnplayer

import android.annotation.SuppressLint
import android.content.Context
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.WindowManager
import android.webkit.ConsoleMessage
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import io.dotlearn.lrnplayer.error.LRNPlayerException
import io.dotlearn.lrnplayer.error.LRNPlayerNotPreparedException
import io.dotlearn.lrnplayer.error.LRNPlayerOfflineException
import io.dotlearn.lrnplayer.listener.*
import io.dotlearn.lrnplayer.utils.DisplayUtils
import io.dotlearn.lrnplayer.utils.WirelessUtils

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
    private lateinit var displayUtils: DisplayUtils

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

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        val layoutView = LayoutInflater.from(context).inflate(R.layout.layout_lrnplayer, this)

        containerView = layoutView.findViewById(R.id.lrn_container)
        webView = layoutView.findViewById(R.id.lrn_web_view)

        displayUtils = DisplayUtils(getWindowManager())
        webInterface = LRNPlayerWebInterface(this)

        setupWebView()
    }

    private fun getWindowManager() = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

    @SuppressLint("SetJavaScriptEnabled", "AddJavascriptInterface")
    private fun setupWebView() {
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
            webInterface.log("View is laid out. Preparing")
            // The view has been laid out, load up the video
            prepare(prepareRequest)
        }
        else {
            // Save the prepare request to be loaded when the View is laid out
            webInterface.log("View is not laid out. Scheduling video preparation...")
            this.prepareRequest = prepareRequest
        }
    }

    private fun prepare(prepareRequest: PrepareRequest?) {
        if(prepareRequest != null) {
            if(isWebViewLoaded) {
                val widthHeightPair = calculateWidthAndHeight()
                loadVideo(prepareRequest, widthHeightPair.first, widthHeightPair.second)
                this.prepareRequest = null
            }
            else {
                this.prepareRequest = prepareRequest
            }
        }
    }

    private fun calculateWidthAndHeight(): Pair<Int, Int> {
        val videoWidth = displayUtils.px2dp(width)
        val usableScreenHeight = displayUtils.getUsableScreenHeight()
        var videoHeight = displayUtils.calculateHeightBasedOnWidthAndAspectRatio(1.77777778,
                videoWidth)

        if (videoHeight > usableScreenHeight) {
            videoHeight = usableScreenHeight
        }

        return Pair(videoWidth, videoHeight)
    }

    private fun loadVideo(prepareRequest: PrepareRequest, videoWidth: Int, videoHeight: Int) {
        isPrepared = false

        if(WirelessUtils.isConnected(context)) {
            val stringToLoad = """javascript:prepare(
                        "${prepareRequest.accessToken}", "${prepareRequest.videoId}",
                        ${prepareRequest.autoStart}, $videoWidth, $videoHeight);"""
            webInterface.log(stringToLoad)
            webView.loadUrl(stringToLoad)
        }
        else {
            webInterface.onError(LRNPlayerOfflineException("Cannot prepare video while the device is offline"))
        }
    }

    fun onPrepared() {
        isPrepared = true
    }

    override fun start() {
        if(checkIsPrepared("start()")) {
            webView.loadUrl("javascript:play();")
        }
    }

    override fun pause() {
        if(checkIsPrepared("pause()")) {
            webView.loadUrl("javascript:pause();")
        }
    }

    override fun seekTo(seekPos: Long) {
        if(checkIsPrepared("seekTo()")) {
            webView.loadUrl("""javascript:seekTo("$seekPos");""")
        }
    }

    override fun setOnCompletionListener(completionListener: OnPlaybackCompletionListener) {
        webInterface.completionListener = completionListener
    }

    override fun setOnErrorListener(errorListener: OnErrorListener) {
        webInterface.errorListener = errorListener
    }

    override fun setOnGetCurrentPositionListener(getCurrentPositionListener: OnGetCurrentPositionListener) {
        webInterface.getCurrentPositionListener = getCurrentPositionListener
    }

    override fun setOnDownloadListener(downloadProgressListener: OnDownloadProgressListener) {
        webInterface.downloadProgressListener = downloadProgressListener
    }

    override fun setOnMetadataLoadedListener(metadataLoadedListener: OnMetadataLoadedListener) {
        webInterface.metadataLoadedListener = metadataLoadedListener
    }

    override fun setOnFullScreenToggledListener(fullScreenToggledListener: OnFullScreenToggledListener) {
        webInterface.fullScreenToggledListener = fullScreenToggledListener
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

    private fun checkIsPrepared(actionName: String): Boolean {
        if(!isPrepared) {
            onError(LRNPlayerNotPreparedException("LRNPlayerView is not prepared. " +
                    "Ensure that the Player is prepared before calling $actionName"))
            return false
        }

        return true
    }
    // endregion

    internal class PrepareRequest(val accessToken: String, val videoId: String, val autoStart: Boolean)

}