package com.mobymagic.vectorizedvideo

import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.BaseQuickAdapter
import io.dotlearn.vectorizedvideo.R
import org.dotlearn.lrncurriculum.models.Course

class CourseAdapter(courses: List<Course>)
    : BaseQuickAdapter<Course, BaseViewHolder>(R.layout.item_curriculum, courses) {

    override fun convert(viewHolder: BaseViewHolder, item: Course) {
        viewHolder.setText(R.id.text_id, item.id)
                .setText(R.id.text_title, item.name)
                //.setText(R.id.text_version, item)
    }
}