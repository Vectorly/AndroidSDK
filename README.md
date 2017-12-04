# LRNPlayerView
LRNPlayerView is a simple view that you can plugin to your apps to quickly get vectorized video playback working.

## Install
The Gradle dependency is available via jCenter. jCenter is the default Maven repository used by Android Studio.

Add this in your (app) module's `build.gradle` file:
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
        android:layout_height="wrap_content" />
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
                lrnPlayerView.prepare(accessToken, videoId, false, object: OnPreparedListener {
        
                    override fun onPrepared(lrnPlayerView: LRNPlayerView) {
                        lrnPlayerView.start()
                    }
        
                })
            }
        }
```

You can add some custom optional listeners to the LRNPlayerView
```kotlin
        // Set a listener that gets notified when the video playback completes
        lrnPlayerView.setOnCompletionListener(object: OnPlaybackCompletionListener {

            override fun onPlaybackCompletion(lrnPlayerView: LRNPlayerView) {
                // Do something when the video completes
            }

        })
        
        // Set a listener that gets called when an error occurred while working with the LRNPlayerView
        lrnPlayerView.setOnErrorListener(object: OnErrorListener {

            override fun onError(lrnPlayerView: LRNPlayerView, e: LRNPlayerException) {
                // Do something when an error occurs
            }

        })
        
        // Set a download progress listener that gets called as each chunk of the video is downloaded
        lrnPlayerView.setOnDownloadListener(object: OnDownloadProgressListener{

            override fun onDownloadProgress(lrnPlayerView: LRNPlayerView, progressPercent: Float) {
                // Do something with the download progress. The library already shows a progress bar
                // so there is no need for you to also show a progress bar
            }

        })
        
        // Sets a listener that gets called when the full screen button is clicked
        lrnPlayerView.setOnFullScreenToggledListener(object: OnFullScreenToggledListener{

            override fun onFullScreenToggled(lrnPlayerView: LRNPlayerView) {
                // The full screen button was clicked, toggle the phone orientation
            }

        })
        
        // Sets a listener when the video metadata is loaded
        lrnPlayerView.setOnMetadataLoadedListener(object: OnMetadataLoadedListener{

            override fun onMetadataLoaded(lrnPlayerView: LRNPlayerView, metadata: Metadata) {
                // Do something with the video metadata
            }

        })
```