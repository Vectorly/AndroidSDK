package io.vectorly.player.model

/**
 * A model class that holds the metadata for a vectorized video file
 */
data class Metadata(
	val thumbnail: String? = null,
	val defaultSize: String? = null,
	val size: Size? = null,
	val module: String? = null,
	val name: String? = null,
	val description: String? = null,
	val id: String? = null,
	val published: Boolean? = null
)
