package com.angcyo.csdn.iview

import android.graphics.Color
import android.os.Bundle
import com.angcyo.csdn.R
import com.angcyo.csdn.base.BaseSingleRecyclerUIView
import com.angcyo.csdn.bean.RankListItemBean
import com.angcyo.csdn.bean.RankListSubItemBean
import com.angcyo.csdn.jsoup.Csdn
import com.angcyo.csdn.jsoup.rankList
import com.angcyo.rtbs.X5WebUIView
import com.angcyo.uiview.base.UISlidingTabView
import com.angcyo.uiview.container.ContentLayout
import com.angcyo.uiview.kotlin.https
import com.angcyo.uiview.kotlin.visible
import com.angcyo.uiview.net.RSubscriber
import com.angcyo.uiview.recycler.RBaseViewHolder
import com.angcyo.uiview.recycler.adapter.RExBaseAdapter
import com.angcyo.uiview.resources.ResUtil
import com.angcyo.uiview.rsen.RefreshLayout
import com.angcyo.uiview.utils.UI
import com.angcyo.uiview.widget.CircleImageView

/**
 * 博客排行榜
 * Created by angcyo on 2018/04/06 12:23
 */
class RankListUIView : UISlidingTabView() {

    override fun getTitleShowString(): String {
        return "CSDN排行榜"
    }

    override fun createPages(pages: ArrayList<TabPageBean>) {
        super.createPages(pages)

        (mBaseDataObject as List<*>?)?.let {
            it.map {
                (it as RankListItemBean?)?.let {
                    pages.add(TabPageBean(RankListSubUIView(it.title, it.subItemBeans), it.title))
                }
            }
        }
    }

    override fun getDefaultLayoutState(): LayoutState {
        return LayoutState.LOAD
    }

    override fun onViewShowFirst(bundle: Bundle?) {
        super.onViewShowFirst(bundle)
        showLoadView()
        Csdn.rank.rankList()
                .subscribe(object : RSubscriber<List<RankListItemBean>>() {
                    override fun onSucceed(bean: List<RankListItemBean>?) {
                        super.onSucceed(bean)
                        mBaseDataObject = bean
                        showContentLayout()
                        hideLoadView()
                    }
                })
    }
}

class RankListSubUIView(val title: String, val datas: List<RankListSubItemBean>) : BaseSingleRecyclerUIView<RankListSubItemBean>() {
    override fun createAdapter(): RExBaseAdapter<String, RankListSubItemBean, String> {
        return object : RExBaseAdapter<String, RankListSubItemBean, String>(mActivity, datas) {
            override fun getItemLayoutId(viewType: Int): Int {
                return R.layout.item_rank_list_sub_layout
            }

            override fun onBindDataView(holder: RBaseViewHolder, posInData: Int, dataBean: RankListSubItemBean?) {
                super.onBindDataView(holder, posInData, dataBean)
                dataBean?.let { bean ->
                    holder.tv(R.id.index_view).apply {
                        text = "${posInData + 1}."
                        UI.setBackgroundDrawable(this, when (posInData) {
                            0 -> ResUtil.generateRoundDrawable(50f, Color.parseColor("#E1DD46"), Color.parseColor("#E1DD46"))
                            1 -> ResUtil.generateRoundDrawable(50f, Color.parseColor("#CCD0D3"), Color.parseColor("#CCD0D3"))
                            2 -> ResUtil.generateRoundDrawable(50f, Color.parseColor("#C49873"), Color.parseColor("#C49873"))
                            else -> null
                        })
                        if (posInData < 3) {
                            setTextColor(Color.WHITE)
                        } else {
                            setTextColor(getColor(R.color.base_text_color_dark))
                        }
                    }

                    holder.tv(R.id.name_view).text = if (bean.title.isNullOrEmpty())
                        bean.userName else bean.title
                    holder.tv(R.id.tip_view).text = bean.viewCountTip

                    if (bean.userAvatar.isNullOrEmpty()) {
                        holder.gone(R.id.image_view)
                    } else {
                        holder.glideImgV(R.id.image_view).apply {
                            reset()
                            visible()
                            url = bean.userAvatar
                            if (bean.isUserAvatar) {
                                showType = CircleImageView.CIRCLE
                            } else {
                                showType = CircleImageView.NORMAL
                            }
                        }
                    }

                    holder.clickItem {
                        if (title.contains("专栏")) {
                            startIView(X5WebUIView("https://blog.csdn.net" + bean.link))
                        } else if (title.contains("文章") || title.contains("评论")) {
                            startIView(X5WebUIView(bean.link.https()))
                        } else {
                            startIView(BlogListUIView(bean.link.https(), true))
                        }
                    }
                }
            }
        }
    }

    override fun getDefaultLayoutState(): LayoutState {
        return LayoutState.CONTENT
    }

    override fun initRefreshLayout(refreshLayout: RefreshLayout?, baseContentLayout: ContentLayout?) {
        super.initRefreshLayout(refreshLayout, baseContentLayout)
        refreshLayout?.setNoNotifyPlaceholder()
    }

}
