package com.angcyo.csdn.iview

import android.os.Bundle
import com.angcyo.csdn.R
import com.angcyo.csdn.base.BaseSingleRecyclerUIView
import com.angcyo.csdn.bean.BlogBaseBean
import com.angcyo.csdn.bean.BlogCommendItemBean
import com.angcyo.csdn.jsoup.Csdn
import com.angcyo.csdn.jsoup.bodyNavBlogList
import com.angcyo.csdn.jsoup.bodyNavList
import com.angcyo.rtbs.X5WebUIView
import com.angcyo.uiview.base.UISlidingTabView
import com.angcyo.uiview.net.RSubscriber
import com.angcyo.uiview.recycler.RBaseViewHolder
import com.angcyo.uiview.recycler.adapter.RExBaseAdapter
import com.angcyo.uiview.widget.viewpager.UIViewPager

/**
 * Csdn 推荐 导航页
 * Created by angcyo on 2018/04/05 11:23
 */
class CommendUIView : UISlidingTabView() {

    override fun createPages(pages: ArrayList<TabPageBean>) {
        super.createPages(pages)
        (mBaseDataObject as List<*>?)?.let {
            for (nav in it) {
                (nav as BlogBaseBean?)?.let {
                    if (it.title != "关注") {
                        //关注列表需要登录才有数据
                        pages.add(TabPageBean(CommendSubUIView(it.link), it.title))
                    }
                }
            }
        }
    }

    override fun initViewPager(viewPager: UIViewPager) {
        super.initViewPager(viewPager)
        viewPager.offscreenPageLimit = 1
    }

    override fun getDefaultLayoutState(): LayoutState {
        return LayoutState.LOAD
    }

    override fun onViewShowFirst(bundle: Bundle?) {
        super.onViewShowFirst(bundle)
        Csdn.csdn.bodyNavList()
                .subscribe(object : RSubscriber<List<BlogBaseBean>>() {
                    override fun onSucceed(bean: List<BlogBaseBean>?) {
                        super.onSucceed(bean)
                        mBaseDataObject = bean
                        showContentLayout()
                    }
                })
    }
}

class CommendSubUIView(val url: String) : BaseSingleRecyclerUIView<BlogCommendItemBean>() {
    override fun createAdapter(): RExBaseAdapter<String, BlogCommendItemBean, String> {
        return object : RExBaseAdapter<String, BlogCommendItemBean, String>(mActivity) {
            override fun onBindDataView(holder: RBaseViewHolder, posInData: Int, dataBean: BlogCommendItemBean?) {
                super.onBindDataView(holder, posInData, dataBean)
                dataBean?.let { bean ->
                    holder.tv(R.id.view_count_tip_view).text = bean.viewCountTip
                    holder.tv(R.id.view_count_view).text = bean.viewCount
                    holder.tv(R.id.user_name_view).text = bean.userName
                    holder.tv(R.id.time_view).text = bean.time
                    holder.tv(R.id.title_view).text = bean.title

                    holder.gIV(R.id.user_avatar_view).apply {
                        reset()
                        url = bean.userAvatar
                    }

                    holder.click(R.id.user_name_view) {
                        startIView(BlogListUIView(bean.userBlogUrl, true))
                    }

                    holder.clickItem {
                        startIView(X5WebUIView(bean.link))
                    }
                }
            }

            override fun getItemLayoutId(viewType: Int): Int {
                return R.layout.item_commend_sub_layout
            }
        }
    }

    override fun isUIHaveLoadMore(datas: MutableList<BlogCommendItemBean>?): Boolean {
        return false
    }

    override fun needLoadData(): Boolean {
        return true
    }

    override fun onUILoadData(page: Int, extend: String?) {
        super.onUILoadData(page, extend)

        if (mBaseDataObject != null) {
            onUILoadFinish(mBaseDataObject as MutableList<BlogCommendItemBean>?)
        } else {
            url.bodyNavBlogList()
                    .subscribe(object : RSubscriber<List<BlogCommendItemBean>>() {
                        override fun onSucceed(bean: List<BlogCommendItemBean>?) {
                            super.onSucceed(bean)
                            mBaseDataObject = bean
                            onUILoadFinish(bean)
                        }
                    })
        }
    }

}