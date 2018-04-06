package com.angcyo.csdn.iview

import android.view.LayoutInflater
import android.view.MotionEvent
import com.angcyo.csdn.R
import com.angcyo.uiview.base.PageBean
import com.angcyo.uiview.base.UINavigationView
import com.angcyo.uiview.kotlin.isDown
import com.angcyo.uiview.viewgroup.SliderMenuLayout

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

    override fun haveSliderMenu(): Boolean {
        return true
    }

    override fun initSliderMenu(sliderMenuLayout: SliderMenuLayout, inflater: LayoutInflater) {
        super.initSliderMenu(sliderMenuLayout, inflater)
        inflater.inflate(R.layout.menu_blog_list_layout, sliderMenuLayout)
        sliderMenuLayout.sliderCallback = object : SliderMenuLayout.SimpleSliderCallback() {
            override fun canSlider(menuLayout: SliderMenuLayout, event: MotionEvent): Boolean {
                if (event.isDown() && event.x > 30 * density()) {
                    return false
                }
                return true
            }
        }
    }

    override fun onViewLoad() {
        super.onViewLoad()
        click(R.id.close_button) {
            sliderMenuLayout?.closeMenu()
        }
    }
}