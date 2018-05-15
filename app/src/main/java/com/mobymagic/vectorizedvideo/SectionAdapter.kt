package com.mobymagic.vectorizedvideo

import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.BaseQuickAdapter
import io.dotlearn.vectorizedvideo.R
import org.dotlearn.lrncurriculum.models.Section

class SectionAdapter(sections: List<Section>)
    : BaseQuickAdapter<Section, BaseViewHolder>(R.layout.item_curriculum, sections) {

    override fun convert(viewHolder: BaseViewHolder, item: Section) {
        viewHolder.setText(R.id.text_id, item.id)
                .setText(R.id.text_title, item.name)
                .setText(R.id.text_version, item.version.toString())
    }

}