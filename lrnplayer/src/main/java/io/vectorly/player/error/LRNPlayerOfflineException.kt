package io.vectorly.player.error

/**
 * An exception that gets thrown when you attempt to prepare a video when the user is not connected
 * to the Internet.
 */
class LRNPlayerOfflineException(message: String): LRNPlayerException(message)