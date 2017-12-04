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

Playing a video with the View is very simple. You just need to pass an `aceesToken`, `videoId`, `autoStart` flag and a prepared callback that gets notified when the video is ready.
```kotlin
        class MainActivity : AppCompatActivity() {
        
            override fun onCreate(savedInstanceState: Bundle?) {
                // ...
        
                val accessToken = "accessToken"
                val videoId = "videoId"
        
                val lrnPlayerView = findViewById<LRNPlayerView>(R.id.lrn_player_view)
                lrnPlayerView.prepare(accessToken, videoId, false, object: OnPreparedListener {
        
                    override fun onPrepared(lrnPlayerView: LRNPlayerView) {
                        // The video has been prepared and is ready for playback. If you set autoStart
                        // to false, you can manually start playback here, else you don't have to do
                        // anything here
                        lrnPlayerView.start()
                    }
        
                })
            }
        }
```

You can control playback programmatically using any of the functions below:
```kotlin
        start() // Starts video playback
        pause() // Pause video playback
        seekTo(seekPos) // Seeks to the specified position
```

Before calling any of the playback control functions above, ensure that a video has been prepared on the LRNPlayerView. If a video has not been prepared and no error listener was added, the control function will throw a `LRNPlayerNotPreparedException` exception. To add an error listener and prevent an exception from being thrown, use the code below:
```kotlin
// Set a listener that gets called when an error occurred while working with the LRNPlayerView
        lrnPlayerView.setOnErrorListener(object: OnErrorListener {

            override fun onError(lrnPlayerView: LRNPlayerView, e: LRNPlayerException) {
                // Do something when an error occurs
            }

        })
```

To support the full screen functionality, you will need to add:
```kotlin
// Sets a listener that gets called when the full screen button is clicked
        lrnPlayerView.setOnFullScreenToggledListener(object: OnFullScreenToggledListener{

            override fun onFullScreenToggled(lrnPlayerView: LRNPlayerView) {
                // The full screen button was clicked, toggle the phone orientation
                FullscreenUtils.toggleOrientation(activity)
            }

        })
```

If you want to show some information about the video, you can add a MetadataLoadedListener:
```kotlin
// Sets a listener when the video metadata is loaded
        lrnPlayerView.setOnMetadataLoadedListener(object: OnMetadataLoadedListener{

            override fun onMetadataLoaded(lrnPlayerView: LRNPlayerView, metadata: Metadata) {
                // Do something with the video metadata
            }

        })
```

To get notified as a video is being downloaded, add the listener:
```kotlin
// Set a download progress listener that gets called as each chunk of the video is downloaded
        lrnPlayerView.setOnDownloadListener(object: OnDownloadProgressListener{

            override fun onDownloadProgress(lrnPlayerView: LRNPlayerView, progressPercent: Float) {
                // Do something with the download progress. The library already shows a progress bar
                // so there is no need for you to also show a progress bar
            }

        })
```

You can add some custom optional listeners to the LRNPlayerView
```kotlin
        // Set a listener that gets notified when the video playback completes
        lrnPlayerView.setOnCompletionListener(object: OnPlaybackCompletionListener {

            override fun onPlaybackCompletion(lrnPlayerView: LRNPlayerView) {
                // Do something when the video completes
            }

        })
```

lrnPlayerView.debug(true)

That's all. You could see all this in action in the sample project in the app module.