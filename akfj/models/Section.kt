package org.mobymagic.lrncurriculum.models

import com.google.gson.annotations.SerializedName

data class Section(val id: String, val version: Int, val name: String,
                   @SerializedName("children") val lessonIds: List<String>)