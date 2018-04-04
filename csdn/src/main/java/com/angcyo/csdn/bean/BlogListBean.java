package com.angcyo.csdn.bean;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类的描述：
 * 创建人员：Robi
 * 创建时间：2018/04/04 18:40
 * 修改人员：Robi
 * 修改时间：2018/04/04 18:40
 * 修改备注：
 * Version: 1.0.0
 */
public class BlogListBean {
    public String bodyTitle;// 文章总标题
    public String title; //标题
    public String des; //描述
    public String link; //链接

    public BlogListBean() {
    }

    public BlogListBean(String title, String des, String link) {
        this.title = title;
        this.des = des;
        this.link = link;
    }
}
