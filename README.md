# LRNPlayerView
LRNPlayerView is a simple View that you can plugin to your Android apps to quickly get vectorized video playback working.

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

<b>Note:</b> Ensure the `layout_height` is set to `wrap_content`. Based on the width of the view, the height is automatically calculated to maintain a 16:9 aspect ratio.

#### Basic Code Setup

Playing a video with the View is very simple. You just need to pass an `aceesToken`, `videoId` and an `OnPreparedListener` that gets notified when the video is ready.

<b>Required:</b> Your app must have the `WRITE_EXTERNAL_STORAGE` permission in your `AndroidManifest.xml` file.
If you target Android 6.0 (Marshmallow) and above, also ensure that the user has granted this permission at [runtime](https://developer.android.com/training/permissions/requesting.html)

```kotlin
    val accessToken = "accessToken"
    val videoId = "videoId"

    val lrnPlayerView = findViewById<LRNPlayerView>(R.id.lrn_player_view)
    lrnPlayerView.prepare(accessToken, videoId, object: OnPreparedListener {
            
        override fun onPrepared(lrnPlayerView: LRNPlayerView) {
            // The video has been prepared and is ready for playback. If you set autoStart
            // to false, you can manually start playback here, else you don't have to do
            // anything here
            lrnPlayerView.start()
        }

    })
```

You can control video playback programmatically using any of the functions below:
```kotlin
    lrnPlayerView.start() // Starts video playback
    lrnPlayerView.pause() // Pause video playback
    lrnPlayerView.seekTo(seekPos) // Seeks to the specified position
```

Before calling any of the playback control functions above, ensure that a video has been prepared on the player view. If a video has not been prepared and no error listener was set, the control function will throw a `LRNPlayerNotPreparedException` exception. To set an error listener and prevent an exception from being thrown, use the code below:
```kotlin
    // Set a listener that gets called when an error occurred while working with the LRNPlayerView
    lrnPlayerView.setOnErrorListener(object: OnErrorListener {

        override fun onError(lrnPlayerView: LRNPlayerView, e: LRNPlayerException) {
            // Do something when an error occurs
        }

    })
```

<b>Note:</b> Its really important that you call the `lrnPlayerView.pause()` function in your `Activity` or `Fragment` `onPause` lifecycle callback. If you do not explicitly pause playback, the video and audio will continue to play even if the user is in another Activity.

If the player view is no longer needed or the `Activity` is being destroyed, you should call the `release` function on the View to release system resources.
```kotlin
    lrnPlayerView.release()
```

To support the full screen functionality, you will need to set an `OnFullScreenToggledListener` that toggles between landscape and portrait orientation. We have provided a helper class called `FullScreenUtils` to help you toggle the phone orientation:
```kotlin
    // Sets a listener that gets called when the full screen button is clicked
    lrnPlayerView.setOnFullScreenToggledListener(object: OnFullScreenToggledListener{

        override fun onFullScreenToggled(lrnPlayerView: LRNPlayerView) {
            // The full screen button was clicked, toggle the phone orientation
            FullScreenUtils.toggleOrientation(activity)
        }

    })
```

#### Additional functionality
If you want to show some information about the video, you can set an `OnMetadataLoadedListener` that gets called when the video metadata is ready (loaded):
```kotlin
    // Sets a listener when the video metadata is loaded
    lrnPlayerView.setOnMetadataLoadedListener(object: OnMetadataLoadedListener{

        override fun onMetadataLoaded(lrnPlayerView: LRNPlayerView, metadata: VideoMetadata) {
            // Do something with the video metadata
        }

    })
```

To get notified as a video is being downloaded, set the `OnDownloadProgressListener` listener:
```kotlin
    // Set a download progress listener that gets called as each chunk of the video is downloaded
    lrnPlayerView.setOnDownloadListener(object: OnDownloadProgressListener{

        override fun onDownloadProgress(lrnPlayerView: LRNPlayerView, progressPercent: Float) {
            // Do something with the download progress. The library already shows a progress bar
            // so there is no need for you to also show a progress bar
    }

    })
```

You can also get notified when a video playback completes by setting an `OnPlaybackCompletionListener` listener:
```kotlin
    // Set a listener that gets notified when the video playback completes
    lrnPlayerView.setOnCompletionListener(object: OnPlaybackCompletionListener {

        override fun onPlaybackCompletion(lrnPlayerView: LRNPlayerView) {
            // Do something when the video completes
        }

    })
```

That's all. You could see all this in action in the sample project in the `app` module.