package com.angcyo.csdn

import com.angcyo.rtbs.RTbs
import com.angcyo.uiview.RApplication

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类的描述：
 * 创建人员：Robi
 * 创建时间：2018/04/04 17:38
 * 修改人员：Robi
 * 修改时间：2018/04/04 17:38
 * 修改备注：
 * Version: 1.0.0
 */
class App : RApplication() {
    override fun onAsyncInit() {
        super.onAsyncInit()
        RTbs.init(this)
    }
}