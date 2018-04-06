package com.angcyo.csdn.jsoup

import com.angcyo.csdn.bean.*
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

object Csdn {
    /*博客首页*/
    const val csdn = "https://blog.csdn.net/"
    /*排行榜首页*/
    const val rank = "https://blog.csdn.net/ranking.html"
    /*专栏首页*/
    const val expert = "https://blog.csdn.net/column.html"
}

/**具体博文列表*/
public fun String.bodyList(): Observable<List<BlogListBean>> {
    return this.jsoupAsync()
            .subscribeOn(Schedulers.computation())
            .map {
                var blogBox = it.body().select(".blog-units.blog-units-box").first()
                val blogList = mutableListOf<BlogListBean>()

                if (blogBox == null) {
                    blogBox = it.body().select("#article_list").first()

                    for (e in blogBox.children()) {
                        blogList.add(BlogListBean().apply {
                            bodyTitle = it.title()
                            title = e.select(".link_title").text()
                            des = e.select(".article_description").text()
                            link = e.select("a").attr("href")
                            time = e.select(".link_postdate").text()
                            viewCount = "" //e.select(".link_view").select("a").text()
                            viewCountTip = e.select(".link_view").text()
                        })
                    }
                } else {
                    for (e in blogBox.children()) {
                        blogList.add(BlogListBean().apply {
                            bodyTitle = it.title()
                            title = e.select("h3").text()
                            des = e.select("p").text()
                            link = e.select("a").attr("href")

                            time = e.select(".left-dis-24")[0]?.text()
                            viewCountTip = "阅读:" + e.select(".left-dis-24")[1]?.text()
                        })
                    }
                }
                blogList
            }
}

/**导航列表*/
public fun String.bodyNavList(): Observable<List<BlogBaseBean>> {
    //bean.body().select(".nav_com").first().child(0).children()
    return this.jsoupAsync()
            .subscribeOn(Schedulers.computation())
            .map {
                val blogBox = it.body().select(".nav_com").first().child(0)
                val blogList = mutableListOf<BlogBaseBean>()
                blogBox.children().mapIndexed { index, element ->
                    if (index == 0) {
                        blogList.add(BlogBaseBean().apply {
                            bodyTitle = it.title()
                            title = "推荐"
                            link = this@bodyNavList
                        })
                    } else {
                        blogList.add(BlogBaseBean().apply {
                            bodyTitle = it.title()
                            title = element.select("a").text()
                            link = this@bodyNavList + element.select("a").attr("href")
                        })
                    }
                }
                blogList
            }
}

/**导航列表, 对应的内容列表*/
public fun String.bodyNavBlogList(): Observable<List<BlogCommendItemBean>> {
    //bean.body().select(".nav_com").first().child(0).children()
    return this.jsoupAsync()
            .subscribeOn(Schedulers.computation())
            .map {
                val blogBox = it.body().select(".feedlist_mod").first()
                val blogList = mutableListOf<BlogCommendItemBean>()
                blogBox.children().mapIndexed { _, element ->
                    val name = element.select(".name")?.text()
                    if (name.isNullOrEmpty()) {

                    } else {
                        blogList.add(BlogCommendItemBean().apply {
                            bodyTitle = it.title()
                            userName = name
                            val first = element.select(".csdn-tracking-statistics")[0]
                            link = first?.select("a")?.attr("href")
                            title = first?.text()
                            try {
                                val userElement = element.select(".user_img")
                                userBlogUrl = userElement.attr("href")
                                userAvatar = "https:" + userElement.select("img").attr("src")
                            } catch (e: Exception) {
                            }
                            time = element.select(".time")?.text()

                            viewCount = element.select(".read_num")?.select(".num")?.text()
                            viewCountTip = element.select(".read_num")?.select(".text")?.text()
                            //link = this@bodyNavList + element.select("a").attr("href")
                        })
                    }

                }
                blogList
            }
}

/**排行榜 导航列表, 以及对应的数据*/
public fun String.rankList(): Observable<List<RankListItemBean>> {
    return this.jsoupAsync()
            .subscribeOn(Schedulers.computation())
            .map {
                val list = mutableListOf<RankListItemBean>()
                val elements = it.body().select(".ranking")
                elements.map { e ->
                    list.add(RankListItemBean().apply {
                        bodyTitle = it.title()
                        title = e.select(".rank_t").text()
                        subItemBeans = mutableListOf()

                        val rankElement = e.select(".ranking_c").first() //排行榜
                        if (rankElement == null) {
                            //没有排行榜, 就是博客之星
                            e.select(".star_name").mapIndexed { index, start ->
                                subItemBeans.add(RankListSubItemBean().apply {
                                    title = start.text()
                                    userName = title
                                    link = start.attr("href")
                                    userAvatar = e.select("img")[index].attr("src")
                                })
                            }
                        } else {
                            rankElement.children()?.map { sub ->
                                subItemBeans.add(RankListSubItemBean().apply {
                                    //文章排行, 有title的, 一定是文章排行榜
                                    title = sub.select(".article_t").text()
                                    viewCountTip = sub.select(".article_span").text()
                                    link = sub.select(".article_t").attr("href")

                                    if (link.isNullOrEmpty()) {
                                        link = sub.select(".blog_a").attr("href")
                                    }

                                    //包含用户信息的, 就是用户排行榜
                                    userAvatar = sub.select(".blog_img").attr("src")
                                    if (userAvatar.isNullOrEmpty()) {
                                        userAvatar = sub.select(".column_img").attr("src")
                                        isUserAvatar = false
                                    }

                                    userName = sub.select(".blog_a").text() //也有可能是专栏的名称
                                    userBlogUrl = sub.select(".blog_a").attr("href")
                                    viewCountTip = sub.select("b").last().text()
                                })
                            }
                        }
                    })
                }
                list
            }
}