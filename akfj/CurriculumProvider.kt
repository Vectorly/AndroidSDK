package org.mobymagic.lrncurriculum

import android.content.Context
import android.util.LruCache
import org.mobymagic.lrncurriculum.models.Course

object CurriculumProvider {

    private val courseCache = LruCache<String, Course>()

    fun init(context: Context) {

    }

}