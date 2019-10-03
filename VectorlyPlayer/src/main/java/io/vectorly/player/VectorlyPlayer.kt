package io.vectorly.player

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
import io.vectorly.player.error.VectorPlayerException
import io.vectorly.player.error.VectorPlayerNotPreparedException
import io.vectorly.player.listener.*
import io.vectorly.player.loader.VideoDb
import io.vectorly.player.loader.VideoLoader
import io.vectorly.player.loader.di.Injector
import io.vectorly.player.utils.DisplayUtils


/**
 * A custom view that plays vectorized videos
 */
class VectorlyPlayer : FrameLayout {

    // region View Variables
    private lateinit var webView: WebView
    private lateinit var progressContainer: View
    private lateinit var errorContainer: View
    private lateinit var errorTextView: TextView
    private lateinit var containerView: FrameLayout
    // endregion

    private var isPrepared = false
    private var isWebViewLoaded = false
    private var prepareRequest: PrepareRequest? = null
    private lateinit var webInterface: VectorlyPlayerWebInterface
    private lateinit var displayUtils: DisplayUtils
    private val videoDownloader = Injector.provideDownloader()
    private  val baseDownloadUrl = "https://stream-staging.vectorly.io/mobile/encrypted"


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
        val layoutView = LayoutInflater.from(context).inflate(R.layout.layout_vectorplayer, this)

        containerView = layoutView.findViewById(R.id.vectorly_container)
        webView = layoutView.findViewById(R.id.vectorly_web_view)
        progressContainer = layoutView.findViewById(R.id.progress_container)
        errorContainer = layoutView.findViewById(R.id.error_container)
        errorTextView = layoutView.findViewById(R.id.error_text_view)

        displayUtils = DisplayUtils(getWindowManager())
        webInterface = VectorlyPlayerWebInterface(this)

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


    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

        println("Layout changed")
        super.onLayout(changed, l, t, r, b)
        prepare(prepareRequest)
    }

    fun load( videoId: String,  accessToken: String, autoStartVideo: Boolean,
                onLoadListener: OnLoadListener) {
        return prepareView( videoId, accessToken, autoStartVideo, onLoadListener)
    }


    fun exists( videoId: String, accessToken: String): Boolean {
        return VideoDb.exist(videoId, accessToken)
    }

    fun delete( videoId: String, accessToken: String): Boolean {
        return VideoDb.delete(videoId, accessToken);
    }

    fun download(videoId: String, accessToken: String,  downloadListener: DownloadListener) {

        val downloadURL = baseDownloadUrl + "/video/" + videoId + "/token/" + accessToken;

        videoDownloader.download(downloadURL, VideoDb.getFile(videoId, accessToken), "fsd", VideoLoader.getStuff(videoId, accessToken), downloadListener);

    }


    private fun prepareView( videoId: String, accessToken: String, autoStartVideo: Boolean,
                            onLoadListener: OnLoadListener) {


        webInterface.prepareListener = onLoadListener
        val prepareRequest = PrepareRequest(videoId, accessToken,  autoStartVideo)

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
                println("Calculaing width and height")
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

        println("Inside of load video.....")

        progressContainer.visibility = View.VISIBLE
        errorContainer.visibility = View.GONE
        isPrepared = false


        VideoLoader.load( prepareRequest.videoId, prepareRequest.accessToken,
                object : VideoLoader.VideoLoadCallback {


                    override fun onLocalVideoNotFound(videoId: String, accessToken: String) {

                        println("Calling on video loaded")

                        webInterface.log("onVideoLoaded")
                        val stringToLoad = """javascript:loadVideoWeb("$videoId",
                                "$accessToken");""".trimMargin()
                        webView.loadUrl(stringToLoad)
                    }

                    override fun onLocalVideoFound(videoDataBase64Encoded: String) {

                        webInterface.log("onVideoLoaded")

                        var chunksSent = 0;
                        val chunkLength = 200000;


                        while (chunksSent <  videoDataBase64Encoded.length){

                            var chunkToSend = "";

                            if(videoDataBase64Encoded.length < chunksSent + chunkLength ){
                                chunkToSend = videoDataBase64Encoded.substring(chunksSent);
                            } else{
                                chunkToSend = videoDataBase64Encoded.substring(chunksSent, chunksSent+chunkLength);
                            }

                            chunksSent = chunksSent + chunkToSend.length;

                            val chunkToLoad = """javascript:loadVideoBase64Chunk("$chunkToSend");""".trimMargin()
                            webView.loadUrl(chunkToLoad)

                        }

                        val stringToLoad = """javascript:loadVideoBase64();""".trimMargin()
                        webView.loadUrl(stringToLoad)


                    }

                    override fun onLocalVideoLoadError(e: Exception) {
                        webInterface.log("onVideoLoadError")
                        webInterface.onError(VectorPlayerException(e.toString()))
                    }

                })
    }

    fun onPrepared() {
        progressContainer.visibility = View.GONE
        errorContainer.visibility = View.GONE
        isPrepared = true
    }

    internal fun showError(errorMsg: String) {
        progressContainer.visibility = View.GONE
        errorContainer.visibility = View.VISIBLE
        errorTextView.text = errorMsg
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
    private fun onError(e: VectorPlayerException) {
        webInterface.onError(e)
    }

    private fun checkIsPrepared(actionName: String): Boolean {
        if (!isPrepared) {
            onError(VectorPlayerNotPreparedException("VectorlyPlayer is not prepared. " +
                    "Ensure that the Player is prepared before calling $actionName"))
            return false
        }

        return true
    }
    // endregion

    internal class PrepareRequest(val videoId: String, val accessToken: String,
                                  val autoStartVideo: Boolean)



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

    fun getCurrentPosition(getCurrentPositionListener: OnGetCurrentPositionListener){
        webInterface.getCurrentPositionListener = getCurrentPositionListener
        webView.loadUrl("javascript:getPosition();")

    }

    fun setOnCompletionListener(completionListener: OnPlaybackCompletionListener) {
        webInterface.completionListener = completionListener
    }

    fun setOnErrorListener(errorListener: OnErrorListener) {
        webInterface.errorListener = errorListener
    }

    fun setOnGetCurrentPositionListener(getCurrentPositionListener: OnGetCurrentPositionListener) {

    }



}






/*


*/





