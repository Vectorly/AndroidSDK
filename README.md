# LRNPlayerView
LRNPlayerView is a simple view that you can plugin to your apps to quickly get vectorized video playback working.

## Install
The Gradle dependency is available via jCenter. jCenter is the default Maven repository used by Android Studio.

Add this in your module's `build.gradle` file:
```groovy
implementation 'io.dotlearn.lrnplayer:1.0.0'
```

## Usage

#### Layouts
The layout for your player Activity can be very simple. You only need a LRNPlayerView, all the controls and everything else are created by the player view itself.
```xml
<io.dotlearn.lrnplayer.LRNPlayerView
        android:id="@+id/lrn_player_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:showProgress="true"
        app:progressColor="@color/colorAccent"/>
```

#### Code Setup

Initializing the player is very simple. You just set the `aceesToken` and `videoId`, then some callback listener to receive notifications of important events.
```kotlin
        class MainActivity : AppCompatActivity() {
        
            override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.activity_main)
        
                val accessToken = "accessToken"
                val videoId = "videoId"
        
                val lrnPlayerView = findViewById<LRNPlayerView>(R.id.lrn_player_view)
                lrnPlayerView.prepare(accessToken, videoId, object: OnPreparedListener {
        
                    override fun onPrepared(lrnPlayerView: LRNPlayerView) {
                        lrnPlayerView.start()
                    }
        
                })
            }
        }
```

You can add some custom optional listeners to the LRNPlayerView
```kotlin
        lrnPlayerView.setOnCompletionListener(object: OnCompletionListener {

            override fun onCompletion(lrnPlayerView: LRNPlayerView) {
            }

        })
        lrnPlayerView.setOnErrorListener(object: OnErrorListener {

            override fun onError(lrnPlayerView: LRNPlayerView, errorCode: ErrorCode): Boolean {
                return false
            }

        })
        lrnPlayerView.setOnDownloadListener(object: OnDownloadProgressListener{

            override fun onDownloadProgress(downloadedBytes: Long, totalBytes: Long) {
            }

        })
```