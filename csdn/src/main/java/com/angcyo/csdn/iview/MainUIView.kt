package com.angcyo.csdn.iview

import com.angcyo.csdn.R
import com.angcyo.uiview.base.PageBean
import com.angcyo.uiview.base.UINavigationView

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类的描述：
 * 创建人员：Robi
 * 创建时间：2018/04/04 17:41
 * 修改人员：Robi
 * 修改时间：2018/04/04 17:41
 * 修改备注：
 * Version: 1.0.0
 */
class MainUIView : UINavigationView() {
    override fun createPages(pages: ArrayList<PageBean>) {
        pages.add(PageBean(CommendUIView(), "首页", R.drawable.ico_home_n, R.drawable.ico_home_p))
        pages.add(PageBean(RankListUIView(), "排行榜", R.drawable.ico_rank_n, R.drawable.ico_rank_p))
        pages.add(PageBean(ExpertUIView(), "专栏", R.drawable.ico_expert_n, R.drawable.ico_expert_p))
    }
}