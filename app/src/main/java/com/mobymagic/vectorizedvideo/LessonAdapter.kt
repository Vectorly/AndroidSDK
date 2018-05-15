package com.mobymagic.vectorizedvideo

import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.BaseQuickAdapter
import io.dotlearn.vectorizedvideo.R
import org.dotlearn.lrncurriculum.models.Lesson

class LessonAdapter(lessons: List<Lesson>)
    : BaseQuickAdapter<Lesson, BaseViewHolder>(R.layout.item_curriculum, lessons) {

    override fun convert(viewHolder: BaseViewHolder, item: Lesson) {
        viewHolder.setText(R.id.text_id, item.id)
                .setText(R.id.text_title, item.name)
                .setText(R.id.text_version, item.version.toString())
    }

}