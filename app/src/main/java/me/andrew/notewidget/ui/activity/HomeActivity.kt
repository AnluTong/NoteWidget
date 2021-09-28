package me.andrew.notewidget.ui.activity

import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.view_nav_bar.*
import me.andrew.notewidget.R
import me.andrew.notewidget.domain.getWidgetText
import me.andrew.notewidget.domain.remoteViewInvalidate
import me.andrew.notewidget.domain.saveWidgetText

/**
 * Created by Andrew
 * Date on 21-9-28.
 */
class HomeActivity : BaseActivity() {

    override fun layoutId() = R.layout.activity_home

    override fun initData() {
        et_text.setText(getWidgetText())
    }

    override fun onDestroy() {
        super.onDestroy()
        saveWidgetText(et_text.text.toString())
    }

    override fun initView() {
        iv_back.setOnClickListener {
            onBackPressed()
        }
        tv_title.setText(R.string.main_activity_title)
        btn_ok.setOnClickListener {
            sendText(et_text.text.toString())
            onBackPressed()
        }
    }

    private fun sendText(text: String) {
        saveWidgetText(text)
        remoteViewInvalidate(applicationContext)
    }
}