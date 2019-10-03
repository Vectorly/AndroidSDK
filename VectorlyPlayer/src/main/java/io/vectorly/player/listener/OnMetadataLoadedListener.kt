package io.vectorly.player.listener

import io.vectorly.player.VectorlyPlayer
import io.vectorly.player.loader.model.VideoMetadata

/**
 * Interface definition for a callback to be invoked when a vectorized video metadata is loaded
 */
interface OnMetadataLoadedListener {

    /**
     * Called when the metadata of the vectorized video file is loaded.
     *
     * @param vectorlyPlayer The VectorlyPlayer
     * @param metadata The metadata of the vectorized video
     */
    fun onMetadataLoaded(vectorlyPlayer: VectorlyPlayer, metadata: VideoMetadata)

}