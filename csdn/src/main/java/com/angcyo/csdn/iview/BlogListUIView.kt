package com.angcyo.csdn.iview

import android.view.LayoutInflater
import com.angcyo.csdn.R
import com.angcyo.csdn.base.BaseSingleRecyclerUIView
import com.angcyo.csdn.bean.BlogListBean
import com.angcyo.csdn.jsoup.bodyList
import com.angcyo.rtbs.X5WebUIView
import com.angcyo.uiview.net.RSubscriber
import com.angcyo.uiview.recycler.RBaseViewHolder
import com.angcyo.uiview.recycler.adapter.RExBaseAdapter
import com.angcyo.uiview.viewgroup.SliderMenuLayout

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类的描述：博文列表
 * 创建人员：Robi
 * 创建时间：2018/04/04 18:55
 * 修改人员：Robi
 * 修改时间：2018/04/04 18:55
 * 修改备注：
 * Version: 1.0.0
 */
class BlogListUIView(val blogUserName: String /*博客用户名的名称*/,
                     val isFullUrl: Boolean = false /*是否是完整的url*/)
    : BaseSingleRecyclerUIView<BlogListBean>() {

    override fun haveSliderMenu(): Boolean {
        return true
    }

    override fun initSliderMenu(sliderMenuLayout: SliderMenuLayout, inflater: LayoutInflater) {
        super.initSliderMenu(sliderMenuLayout, inflater)
        inflater.inflate(R.layout.menu_blog_list_layout, sliderMenuLayout)
    }

    override fun initOnShowContentLayout() {
        super.initOnShowContentLayout()
        click(R.id.close_button) {
            mSliderMenuLayout?.closeMenu()
        }
    }

    override fun createAdapter(): RExBaseAdapter<String, BlogListBean, String> {
        return object : RExBaseAdapter<String, BlogListBean, String>(mActivity) {
            override fun onBindDataView(holder: RBaseViewHolder, posInData: Int, dataBean: BlogListBean?) {
                super.onBindDataView(holder, posInData, dataBean)
                dataBean?.let { bean ->
                    holder.tv(R.id.title_view).text = bean.title
                    holder.tv(R.id.view_count_view).text = bean.viewCountTip
                    holder.tv(R.id.time_view).text = bean.time
                    holder.rxtv(R.id.des_view).apply {
                        isNeedPattern = false
                        text = bean.des
                    }

                    holder.click(R.id.card_root_layout) {
                        startIView(X5WebUIView(bean.link))
                        //mSliderMenuLayout?.openMenu()
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
        val url = if (isFullUrl) {
            "${blogUserName}/article/list/$page"
        } else {
            "https://blog.csdn.net/${blogUserName}/article/list/$page"
        }
        url.bodyList()
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