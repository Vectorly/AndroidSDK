package io.vectorly.player.listener

import io.vectorly.player.VectorlyPlayer

/**
 * Interface definition for a callback to be invoked when the full screen button is clicked. To support
 * the full screen feature, you must add this Listener to the Player View and override the [onFullScreenToggled]
 * method to change the device orientation and the View width
 */
interface OnFullScreenToggledListener {

    /**
     * Called when the user clicks on the full screen button
     *
     * @param vectorlyPlayer: the VectorlyPlayer that the full screen button was clicked on
     */
    fun onFullScreenToggled(vectorlyPlayer: VectorlyPlayer)

}