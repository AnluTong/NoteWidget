package me.andrew.notewidget.ui.activity

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.chad.library.adapter.base.listener.OnItemChildLongClickListener
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.view_nav_bar.*
import me.andrew.notewidget.R
import me.andrew.notewidget.domain.*
import me.andrew.notewidget.ui.viewdata.ListNoteData
import me.andrew.notewidget.ui.widget.EditPopWindow
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper

/**
 * Created by Andrew
 * Date on 21-9-29.
 */
class HomeActivity : BaseActivity(), OnItemChildClickListener, OnItemChildLongClickListener {

    private val editTitles = intArrayOf(R.string.make_top, R.string.delete)

    private val listAdapter by lazy {
        ListAdapter()
    }

    override fun layoutId() = R.layout.activity_home

    override fun onStart() {
        super.onStart()
        invalidate()
    }

    private fun invalidate() {
        val list = getNotes()
        listAdapter.setList(list)
    }

    override fun initView() {
        iv_back.setOnClickListener {
            onBackPressed()
        }
        tv_title.setText(R.string.home_activity_title)

        rc_list.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = listAdapter

            OverScrollDecoratorHelper.setUpOverScroll(
                this,
                OverScrollDecoratorHelper.ORIENTATION_VERTICAL
            )
        }

        listAdapter.addChildClickViewIds(R.id.pl_container)
        listAdapter.setOnItemChildClickListener(this)
        listAdapter.addChildLongClickViewIds(R.id.pl_container)
        listAdapter.setOnItemChildLongClickListener(this)

        fr_add.setOnClickListener {
            startActivity(Intent(this, DetailNoteActivity::class.java))
        }
    }

    private class ListAdapter :
        BaseQuickAdapter<ListNoteData, BaseViewHolder>(R.layout.view_holder_home_list) {

        override fun convert(holder: BaseViewHolder, item: ListNoteData) {
            holder.setText(R.id.tv_content, item.content)
                .setText(R.id.tv_time, getTime(item.time))
        }
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val data = adapter.getItem(position) as ListNoteData
        when (view.id) {
            R.id.pl_container -> startActivity(DetailNoteActivity.newIntent(this, data))
        }
    }

    override fun onItemChildLongClick(
        adapter: BaseQuickAdapter<*, *>,
        view: View,
        position: Int
    ): Boolean {
        val data = adapter.getItem(position) as ListNoteData
        when (view.id) {
            R.id.pl_container -> {
                EditPopWindow(this, editTitles) { v, p ->
                    when (p) {
                        0 -> {//make top
                            if (position == 0) {
                                return@EditPopWindow
                            } else {
                                data.priority = (adapter.getItem(0) as ListNoteData).priority + 1
                                addNote(data)
                                invalidate()
                                remoteViewInvalidate(applicationContext)
                            }
                        }
                        1 -> {//delete
                            deleteNote(data)
                            if (position == 0) {
                                remoteViewInvalidate(applicationContext)
                            }
                            invalidate()
                        }
                    }
                }.showPopupWindow(view)
            }
        }
        return true
    }
}