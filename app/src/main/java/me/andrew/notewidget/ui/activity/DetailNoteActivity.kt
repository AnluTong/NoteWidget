package me.andrew.notewidget.ui.activity

import android.content.Context
import android.content.Intent
import android.view.inputmethod.InputMethodManager
import kotlinx.android.synthetic.main.activity_detail_note.*
import kotlinx.android.synthetic.main.view_nav_bar.*
import me.andrew.notewidget.R
import me.andrew.notewidget.domain.addWidget
import me.andrew.notewidget.domain.remoteViewInvalidate
import me.andrew.notewidget.ui.viewdata.ListNoteData

/**
 * Created by Andrew
 * Date on 21-9-29.
 */
class DetailNoteActivity : BaseActivity() {

    private var data: ListNoteData? = null

    override fun layoutId() = R.layout.activity_detail_note

    override fun initData() {
        et_text.setText(data?.content)
    }

    override fun initArguments(intent: Intent?) {
        val txt = intent?.getStringExtra("data")
        if (!txt.isNullOrEmpty()) {
            data = ListNoteData().apply { fromJson(txt) }
        }
    }

    override fun initView() {
        iv_back.setOnClickListener {
            onBackPressed()
        }
        tv_title.setText(R.string.detail_activity_title)
        btn_ok.setOnClickListener {
            sendText(et_text.text.toString())
            onBackPressed()
        }

        et_text.apply {
            postDelayed({
                requestFocus()
                setSelection(length())
                val imm =
                    applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
            }, 500)
        }
    }

    private fun sendText(text: String) {
        if (data == null) {
            data = ListNoteData()
        }
        data!!.apply {
            content = text
            time = System.currentTimeMillis()
            addWidget(this)
        }
        remoteViewInvalidate(applicationContext)
    }

    companion object {

        fun newIntent(ctx: Context, data: ListNoteData): Intent {
            val intent = Intent(ctx, DetailNoteActivity::class.java)
            intent.putExtra("data", data.toJson())
            return intent
        }
    }
}