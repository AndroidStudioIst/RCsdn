package com.angcyo.csdn.iview

import com.angcyo.csdn.R
import com.angcyo.csdn.base.BaseItemUIView
import com.angcyo.jsoup.jsoupAsync
import com.angcyo.uiview.base.Item
import com.angcyo.uiview.base.SingleItem
import com.angcyo.uiview.net.RSubscriber
import com.angcyo.uiview.recycler.RBaseViewHolder
import org.jsoup.nodes.Document

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
class MainUIView : BaseItemUIView() {
    override fun createItems(items: MutableList<SingleItem>) {
        items.add(object : SingleItem() {
            override fun onBindView(holder: RBaseViewHolder, posInData: Int, dataBean: Item?) {
                "https://blog.csdn.net/".jsoupAsync()
                        .subscribe(object : RSubscriber<Document>() {
                            override fun onSucceed(bean: Document) {
                                super.onSucceed(bean)
                                titleString = bean.title()
                                holder.tv(R.id.text_view).text = bean.text()
                            }
                        })
            }

            override fun getItemLayoutId(): Int {
                return R.layout.activity_main
            }
        })
    }

}