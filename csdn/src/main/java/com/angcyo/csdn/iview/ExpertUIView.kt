package com.angcyo.csdn.iview

import com.angcyo.csdn.R
import com.angcyo.csdn.base.BaseRecyclerUIView
import com.angcyo.csdn.bean.ExpertItemBean
import com.angcyo.csdn.jsoup.expertList
import com.angcyo.rtbs.X5WebUIView
import com.angcyo.uiview.kotlin.clickIt
import com.angcyo.uiview.net.RSubscriber
import com.angcyo.uiview.recycler.RBaseViewHolder
import com.angcyo.uiview.recycler.adapter.RExBaseAdapter

/**
 * 博客专栏
 * Created by angcyo on 2018/04/06 12:24
 */
class ExpertUIView : BaseRecyclerUIView<ExpertItemBean, ExpertItemBean, String>() {

    override fun getTitleShowString(): String {
        return "CSDN博客专栏"
    }

    override fun createAdapter(): RExBaseAdapter<ExpertItemBean, ExpertItemBean, String> {
        return object : RExBaseAdapter<ExpertItemBean, ExpertItemBean, String>(mActivity) {
            override fun getItemLayoutId(viewType: Int): Int {
                if (isHeaderItemType(viewType)) {
                    return R.layout.item_expert_header_layout
                }
                return R.layout.item_expert_data_layout
            }

            override fun onBindHeaderView(holder: RBaseViewHolder, posInHeader: Int, headerBean: ExpertItemBean?) {
                super.onBindHeaderView(holder, posInHeader, headerBean)
                headerBean?.let { bean ->
                    holder.tv(R.id.title_view).text = bean.title
                    holder.tv(R.id.des_view).text = bean.des

                    holder.gIV(R.id.image_view).apply {
                        reset()
                        url = bean.img
                    }

                    holder.clickItem {
                        startIView(X5WebUIView(bean.link))
                    }
                }
            }

            override fun onBindDataView(holder: RBaseViewHolder, posInData: Int, dataBean: ExpertItemBean?) {
                super.onBindDataView(holder, posInData, dataBean)
                dataBean?.let { bean ->
                    holder.tv(R.id.title_view).text = bean.title

                    holder.gIV(R.id.image_view).apply {
                        reset()
                        url = bean.img

                        clickIt {
                            startIView(X5WebUIView(bean.link))
                        }
                    }

                    holder.clickItem {
                        holder.clickView(R.id.image_view)
                    }
                }
            }
        }
    }

    override fun onUILoadData(page: Int, extend: String?) {
        super.onUILoadData(page, extend)
        "https://blog.csdn.net/column.html?&page=$page".expertList()
                .subscribe(object : RSubscriber<List<ExpertItemBean>>() {
                    override fun onSucceed(bean: List<ExpertItemBean>?) {
                        super.onSucceed(bean)
                        onUILoadFinish(bean)
                        bean?.let {
                            mExBaseAdapter?.resetHeaderData(it[0].headBean)
                        }
                    }
                })
    }
}