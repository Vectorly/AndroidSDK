package org.dotlearn.lrncurriculum.models

import android.support.annotation.Keep

@Keep
data class Lesson(@Keep val id: String, @Keep val version: Int, @Keep val name: String)