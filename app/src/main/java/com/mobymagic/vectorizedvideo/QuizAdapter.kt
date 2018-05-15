package com.mobymagic.vectorizedvideo

import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.BaseQuickAdapter
import io.dotlearn.vectorizedvideo.R
import org.dotlearn.lrncurriculum.models.Quiz

class QuizAdapter(quizzes: List<Quiz>)
    : BaseQuickAdapter<Quiz, BaseViewHolder>(R.layout.item_curriculum, quizzes) {

    override fun convert(viewHolder: BaseViewHolder, item: Quiz) {
        viewHolder.setText(R.id.text_id, item.id)
                .setText(R.id.text_title, item.name)
                .setText(R.id.text_version, item.version.toString())
    }

}