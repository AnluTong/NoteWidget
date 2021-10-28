package me.andrew.notewidget.ui.activity

import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_about.*
import kotlinx.android.synthetic.main.view_nav_bar.*
import me.andrew.notewidget.R

/**
 * Created by Andrew
 * Date on 21-10-28.
 */
class AboutActivity : BaseActivity(), View.OnClickListener {

    override fun layoutId() = R.layout.activity_about

    private val aboutConfigs = mutableMapOf(
        R.id.ic_about to R.string.about,
        R.id.ic_support to R.string.support
    )

    override fun initView() {
        iv_back.setOnClickListener {
            onBackPressed()
        }
        tv_title.setText(R.string.setting)

        ll_container.let {
            for (i in 0 until it.childCount) {
                val child = it.getChildAt(i)

                aboutConfigs[child.id]?.let { configs ->
                    child.findViewById<TextView>(R.id.tv_v_title).setText(configs)
                    child.findViewById<View>(R.id.v_v_click).setOnClickListener {
                        onClick(child)
                    }
                }
            }
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.ic_about -> {
            }
            R.id.ic_support -> {
            }
        }
    }
}