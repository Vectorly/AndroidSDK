package io.dotlearn.vectorizedvideo

import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.BaseQuickAdapter
import org.dotlearn.lrncurriculum.models.Video

class VideoAdapter(videos: List<Video>)
    : BaseQuickAdapter<Video, BaseViewHolder>(R.layout.item_curriculum, videos) {

    override fun convert(viewHolder: BaseViewHolder, item: Video) {
        viewHolder.setText(R.id.text_id, item.id)
                .setText(R.id.text_title, item.name)
                .setText(R.id.text_version, item.version.toString())
    }

}