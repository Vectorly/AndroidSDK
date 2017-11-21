package io.dotlearn.lrnplayer.error

class LRNPlayerException(message: String) : RuntimeException(message) {

    constructor(errorCode: ErrorCode): this("Error code: " + errorCode.code + ", Message: "
            + errorCode.message)

}