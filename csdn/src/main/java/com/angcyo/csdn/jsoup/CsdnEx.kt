package com.angcyo.csdn.jsoup

import com.angcyo.csdn.bean.BlogListBean
import com.angcyo.jsoup.jsoupAsync
import rx.Observable
import rx.schedulers.Schedulers

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类的描述：
 * 创建人员：Robi
 * 创建时间：2018/04/04 18:39
 * 修改人员：Robi
 * 修改时间：2018/04/04 18:39
 * 修改备注：
 * Version: 1.0.0
 */
public fun String.bodyList(): Observable<List<BlogListBean>> {
    return this.jsoupAsync()
            .subscribeOn(Schedulers.computation())
            .map {
                val blogBox = it.body().select(".blog-units.blog-units-box").first()
                val blogList = mutableListOf<BlogListBean>()
                for (e in blogBox.children()) {
                    blogList.add(BlogListBean().apply {
                        bodyTitle = it.title()
                        title = e.select("h3").text()
                        des = e.select("p").text()
                        link = e.select("a").attr("href")
                    })
                }
                blogList
            }
}