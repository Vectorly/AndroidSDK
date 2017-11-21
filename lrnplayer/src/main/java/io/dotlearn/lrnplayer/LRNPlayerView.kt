package io.dotlearn.lrnplayer

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.widget.FrameLayout
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebView
import android.widget.ProgressBar
import io.dotlearn.lrnplayer.error.ErrorCode
import io.dotlearn.lrnplayer.error.LRNPlayerException
import io.dotlearn.lrnplayer.listener.OnCompletionListener
import io.dotlearn.lrnplayer.listener.OnDownloadProgressListener
import io.dotlearn.lrnplayer.listener.OnErrorListener
import io.dotlearn.lrnplayer.listener.OnPreparedListener


/**
 * TODO: document your custom view class.
 */
class LRNPlayerView: FrameLayout {

    // region View Variables
    private lateinit var webView: WebView
    private lateinit var progressView: ProgressBar
    // endregion

    // region Listener Variables
    private var prepareListener: OnPreparedListener? = null
    private var completionListener: OnCompletionListener? = null
    private var downloadProgressListener: OnDownloadProgressListener? = null
    private var errorListener: OnErrorListener? = null
    // endregion

    private var isPrepared = false

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

    private fun init(attrs:AttributeSet?, defStyle:Int) {
        // Load attributes
        val a = context.obtainStyledAttributes(attrs, R.styleable.LRNPlayerView, defStyle, 0)

        val showProgress = a.getBoolean(R.styleable.LRNPlayerView_showProgress, true)
        val progressColor = a.getColor(R.styleable.LRNPlayerView_progressColor, Color.WHITE)
        a.recycle()

        val layoutView = LayoutInflater.from(context).inflate(R.layout.layout_lrnplayer, this)
        webView = layoutView.findViewById(R.id.lrn_web_view)
        progressView = layoutView.findViewById(R.id.lrn_progress_View)

        progressView.visibility = if(showProgress) View.VISIBLE else View.GONE
        progressView.indeterminateDrawable.setColorFilter(progressColor, PorterDuff.Mode.MULTIPLY)
    }
    // endregion

    fun prepare(accessToken: String, videoId: String, onPrepareListener: OnPreparedListener) {
        prepareListener = onPrepareListener

        // TODO: Prepare the WebView for playing
    }

    fun start() {
        checkIsPrepared()
        // TODO start from WebView
    }

    fun pause() {
        checkIsPrepared()
        // TODO pause from WebView
    }

    fun seek() {
        checkIsPrepared()
        // TODO seek to from WebView
    }

    fun seekTo(seekPos: Long) {
        checkIsPrepared()
        // TODO seek to from WebView
    }

    fun getCurrentPosition() {
        checkIsPrepared()
        // TODO get current position from WebView
    }

    fun getDuration() {
        checkIsPrepared()
        // TODO get duration from WebView
    }

    fun setOnCompletionListener(completionListener: OnCompletionListener) {
        this.completionListener = completionListener
    }

    fun setOnErrorListener(errorListener: OnErrorListener) {
        this.errorListener = errorListener
    }

    fun setOnDownloadListener(downloadProgressListener: OnDownloadProgressListener) {
        this.downloadProgressListener = downloadProgressListener
    }

    // region Helper Methods
    private fun onError(errorCode: ErrorCode) {
        if(errorListener == null) {
            throw LRNPlayerException(errorCode)
        } else {
            errorListener?.onError(this, errorCode)
        }
    }

    private fun checkIsPrepared() {
        if(!isPrepared) {
            onError(ErrorCode.NOT_PREPARED)
        }
    }
    // endregion

}