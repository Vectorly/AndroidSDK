package io.vectorly.player.error

/**
 * An exception that gets thrown when you try to perform a task on the Player View that requires
 * a video to be prepared first.
 */
class VectorPlayerNotPreparedException(message: String): VectorPlayerException(message)