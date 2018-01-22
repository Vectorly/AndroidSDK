package org.mobymagic.lrncurriculum.models

import com.google.gson.annotations.SerializedName

data class Lesson(val id: String, val version: Int, val name: String,
                  @SerializedName("children") val videoIds: List<String>)