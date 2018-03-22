package io.dotlearn.lrnplayer

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.webkit.ConsoleMessage
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import android.widget.TextView
import io.dotlearn.lrnplayer.error.LRNPlayerException
import io.dotlearn.lrnplayer.error.LRNPlayerNotPreparedException
import io.dotlearn.lrnplayer.listener.*
import io.dotlearn.lrnplayer.loader.VideoLoader
import io.dotlearn.lrnplayer.loader.model.VideoMetadata
import io.dotlearn.lrnplayer.utils.DisplayUtils
import io.dotlearn.lrnplayer.utils.Logger

/**
 * A custom view that plays vectorized videos
 */
class LRNPlayerView : FrameLayout {

    // region View Variables
    private lateinit var webView: WebView
    private lateinit var progressContainer: View
    private lateinit var downloadProgressTextView: TextView
    private lateinit var errorContainer: View
    private lateinit var errorTextView: TextView
    private lateinit var containerView: FrameLayout
    // endregion

    private var isPrepared = false
    private var isWebViewLoaded = false
    private var prepareRequest: PrepareRequest? = null
    private lateinit var webInterface: LRNPlayerWebInterface
    private lateinit var displayUtils: DisplayUtils

    // region View Init
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    private fun init() {
        val layoutView = LayoutInflater.from(context).inflate(R.layout.layout_lrnplayer, this)

        containerView = layoutView.findViewById(R.id.lrn_container)
        webView = layoutView.findViewById(R.id.lrn_web_view)
        progressContainer = layoutView.findViewById(R.id.progress_container)
        downloadProgressTextView = layoutView.findViewById(R.id.download_progress_text_view)
        errorContainer = layoutView.findViewById(R.id.error_container)
        errorTextView = layoutView.findViewById(R.id.error_text_view)

        displayUtils = DisplayUtils(getWindowManager())
        webInterface = LRNPlayerWebInterface(this)

        setupWebView()
    }

    private fun getWindowManager() = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

    @SuppressLint("SetJavaScriptEnabled", "AddJavascriptInterface")
    private fun setupWebView() {
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            webSettings.mediaPlaybackRequiresUserGesture = false
        }

        webView.addJavascriptInterface(webInterface, "Android")

        webView.webChromeClient = object : WebChromeClient() {

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

    fun prepare(accessToken: String, videoId: String, autoStartVideo: Boolean,
                onPrepareListener: OnPreparedListener) {
        return prepareView(accessToken, videoId, autoStartVideo, onPrepareListener)
    }

    private fun prepareView(accessToken: String, videoId: String, autoStartVideo: Boolean,
                            onPrepareListener: OnPreparedListener) {
        webInterface.prepareListener = onPrepareListener
        val prepareRequest = PrepareRequest(accessToken, videoId, autoStartVideo)

        // We need to wait for the View to be laid out before loading the video, so things like
        // get height and width will work.
        if (ViewCompat.isLaidOut(this)) {
            webInterface.log("View is laid out. Preparing")
            // The view has been laid out, load up the video
            prepare(prepareRequest)
        } else {
            // Save the prepare request to be loaded when the View is laid out
            webInterface.log("View is not laid out. Scheduling video preparation...")
            this.prepareRequest = prepareRequest
        }
    }

    private fun prepare(prepareRequest: PrepareRequest?) {
        if (prepareRequest != null) {
            if (isWebViewLoaded) {
                val widthHeightPair = calculateWidthAndHeight()
                loadVideo(prepareRequest, widthHeightPair.first, widthHeightPair.second)
                this.prepareRequest = null
            } else {
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
        progressContainer.visibility = View.VISIBLE
        errorContainer.visibility = View.GONE
        downloadProgressTextView.text = "0%"
        isPrepared = false

        val autoPlayVideo = prepareRequest.autoStartVideo

        VideoLoader.load(prepareRequest.accessToken, prepareRequest.videoId,
                object : VideoLoader.VideoLoadCallback {
                    override fun onVideoLoadStarted() {
                        webInterface.log("onVideoLoadStarted()")
                    }

                    override fun onVideoLoadProgress(bytesTransferred: Long, totalBytes: Long) {
                        webInterface.log("onVideoLoadProgress()")
                        webInterface.onDownloadProgress(bytesTransferred, totalBytes)
                    }

                    override fun onVideoLoaded(metadata: VideoMetadata, videoDataBase64Encoded: String) {
                        webInterface.log("onVideoLoaded")
                        val stringToLoad = """javascript:loadVideo("$videoDataBase64Encoded",
                                "$videoWidth", "$videoHeight", $autoPlayVideo, false, false);""".trimMargin()
                        webView.loadUrl(stringToLoad)
                    }

                    override fun onVideoLoadError(e: Exception) {
                        webInterface.log("onVideoLoadError")
                        webInterface.onError(LRNPlayerException(e.toString()))
                    }

                })
    }

    fun onPrepared() {
        progressContainer.visibility = View.GONE
        errorContainer.visibility = View.GONE
        isPrepared = true
    }

    fun start() {
        if (checkIsPrepared("start()")) {
            webView.loadUrl("javascript:play();")
        }
    }

    fun pause() {
        if (checkIsPrepared("pause()")) {
            webView.loadUrl("javascript:pause();")
        }
    }

    fun seekTo(seekPos: Long) {
        if (checkIsPrepared("seekTo()")) {
            webView.loadUrl("""javascript:seekTo("$seekPos");""")
        }
    }

    fun setOnCompletionListener(completionListener: OnPlaybackCompletionListener) {
        webInterface.completionListener = completionListener
    }

    fun setOnErrorListener(errorListener: OnErrorListener) {
        webInterface.errorListener = errorListener
    }

    fun setOnGetCurrentPositionListener(getCurrentPositionListener: OnGetCurrentPositionListener) {
        webInterface.getCurrentPositionListener = getCurrentPositionListener
    }

    fun setOnDownloadListener(downloadProgressListener: OnDownloadProgressListener) {
        webInterface.downloadProgressListener = downloadProgressListener
    }

    fun setOnMetadataLoadedListener(metadataLoadedListener: OnMetadataLoadedListener) {
        webInterface.metadataLoadedListener = metadataLoadedListener
    }

    fun setOnFullScreenToggledListener(fullScreenToggledListener: OnFullScreenToggledListener) {
        webInterface.fullScreenToggledListener = fullScreenToggledListener
    }

    internal fun showError(errorMsg: String) {
        progressContainer.visibility = View.GONE
        errorContainer.visibility = View.VISIBLE
        errorTextView.text = errorMsg
    }

    internal fun showDownloadProgress(downloadPercentage: Int) {
        Logger.d("showDownloadProgress. Progress: $downloadPercentage")
        val progressText = downloadPercentage.toString() + "%"
        downloadProgressTextView.text = progressText
    }

    fun release() {
        releaseAll()
    }

    private fun releaseAll() {
        VideoLoader.cancel()
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
        if (!isPrepared) {
            onError(LRNPlayerNotPreparedException("LRNPlayerView is not prepared. " +
                    "Ensure that the Player is prepared before calling $actionName"))
            return false
        }

        return true
    }
    // endregion

    internal class PrepareRequest(val accessToken: String, val videoId: String,
                                  val autoStartVideo: Boolean)

}