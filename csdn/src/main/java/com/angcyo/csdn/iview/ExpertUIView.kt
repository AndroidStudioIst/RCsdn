package com.angcyo.csdn.iview

import com.angcyo.csdn.base.BaseSingleRecyclerUIView
import com.angcyo.csdn.bean.ExpertItemBean
import com.angcyo.uiview.recycler.adapter.RExBaseAdapter

/**
 * 博客专栏
 * Created by angcyo on 2018/04/06 12:24
 */
class ExpertUIView : BaseSingleRecyclerUIView<ExpertItemBean>() {
    override fun createAdapter(): RExBaseAdapter<String, ExpertItemBean, String> {
        return object : RExBaseAdapter<String, ExpertItemBean, String>(mActivity) {

        }
    }
}