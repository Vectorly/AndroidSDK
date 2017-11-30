package io.dotlearn.lrnplayer.listener

import io.dotlearn.lrnplayer.LRNPlayerView
import io.dotlearn.lrnplayer.info.Metadata

/**
 * Interface definition for a callback to be invoked when a vectorized video metadata is loaded
 */
interface OnMetadataLoadedListener {

    /**
     * Called when the metadata of the vectorized video file is loaded.
     *
     * @param metadata The metadata of the vectorized video
     */
    fun onMetadataLoaded(lrnPlayerView: LRNPlayerView, metadata: Metadata)

}