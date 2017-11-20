package io.dotlearn.lrnplayer

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.view.LayoutInflater
import android.webkit.WebView


/**
 * TODO: document your custom view class.
 */
class LRNPlayerView: FrameLayout {

    private var accessToken: String? = null
    private var videoId: String? = null
    private lateinit var webView: WebView

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

        accessToken = a.getString(R.styleable.LRNPlayerView_accessToken)
        videoId = a.getString(R.styleable.LRNPlayerView_videoId)
        a.recycle()

        val layoutView = LayoutInflater.from(context).inflate(R.layout.layout_lrnplayer, this)

        webView = layoutView.findViewById(R.id.lrn_web_view)
    }

    fun load(accessToken: String, videoId: String) {
        this.accessToken = accessToken
        this.videoId = videoId
    }

}