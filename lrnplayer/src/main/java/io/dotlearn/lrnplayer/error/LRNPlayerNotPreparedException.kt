package io.dotlearn.lrnplayer.error

/**
 * An exception that gets thrown when you try to perform a task on the Player View that requires
 * a video to be prepared first.
 */
class LRNPlayerNotPreparedException(message: String): LRNPlayerException(message)