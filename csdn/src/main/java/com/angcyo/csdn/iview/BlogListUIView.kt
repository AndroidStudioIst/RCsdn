package com.angcyo.csdn.iview

import com.angcyo.csdn.R
import com.angcyo.csdn.base.BaseSingleRecyclerUIView
import com.angcyo.csdn.bean.BlogListBean
import com.angcyo.csdn.jsoup.bodyList
import com.angcyo.rtbs.X5WebUIView
import com.angcyo.uiview.net.RSubscriber
import com.angcyo.uiview.recycler.RBaseViewHolder
import com.angcyo.uiview.recycler.adapter.RExBaseAdapter

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类的描述：
 * 创建人员：Robi
 * 创建时间：2018/04/04 18:55
 * 修改人员：Robi
 * 修改时间：2018/04/04 18:55
 * 修改备注：
 * Version: 1.0.0
 */
class BlogListUIView(val blogUrl: String) : BaseSingleRecyclerUIView<BlogListBean>() {
    override fun createAdapter(): RExBaseAdapter<String, BlogListBean, String> {
        return object : RExBaseAdapter<String, BlogListBean, String>(mActivity) {
            override fun onBindDataView(holder: RBaseViewHolder, posInData: Int, dataBean: BlogListBean?) {
                super.onBindDataView(holder, posInData, dataBean)
                dataBean?.let { bean ->
                    holder.tv(R.id.title_view).text = bean.title
                    holder.tv(R.id.des_view).text = bean.des

                    holder.click(R.id.card_root_layout) {
                        startIView(X5WebUIView(bean.link))
                    }
                }
            }

            override fun getItemLayoutId(viewType: Int): Int {
                return R.layout.item_blog_list_layout
            }
        }
    }

    override fun getDefaultLayoutState(): LayoutState {
        return super.getDefaultLayoutState()
    }

    override fun onUILoadData(page: Int, extend: String?) {
        super.onUILoadData(page, extend)
        blogUrl.bodyList()
                .subscribe(object : RSubscriber<List<BlogListBean>>() {
                    override fun onSucceed(bean: List<BlogListBean>?) {
                        super.onSucceed(bean)
                        bean?.firstOrNull()?.let {
                            titleString = it.bodyTitle
                        }
                        onUILoadFinish(bean)
                    }
                })
    }
}