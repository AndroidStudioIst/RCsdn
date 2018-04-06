package com.angcyo.csdn

import android.content.Intent
import com.angcyo.csdn.iview.MainUIView
import com.angcyo.uiview.base.UILayoutActivity

class MainActivity : UILayoutActivity() {
    override fun onLoadView(intent: Intent?) {
        startIView(MainUIView(), false)
//        startIView(BlogListUIView("angcyo"), false)
        //startIView(CommendUIView(), false)

    }
}
