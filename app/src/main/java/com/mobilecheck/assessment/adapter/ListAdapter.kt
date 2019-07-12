package com.mobilecheck.assessment.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.mobilecheck.assessment.Constants
import com.mobilecheck.assessment.R
import com.mobilecheck.assessment.activity.DetailActivity
import com.mobilecheck.assessment.model.SystemInfo
import kotlinx.android.synthetic.main.activity_detail.view.*
import kotlinx.android.synthetic.main.layout_checklist.view.*

class ListAdapter(context: Context, itemList: List<SystemInfo>) :
    RecyclerView.Adapter<ListAdapter.ViewHolder>() {
    val context: Context = context
    val itemList: List<SystemInfo> = itemList
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_checklist, parent, false)
        return ViewHolder(view, context)

    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val systemInfo = itemList.get(position);
        holder.bind(systemInfo)
    }

    class ViewHolder(v: View, context: Context) : RecyclerView.ViewHolder(v), View.OnClickListener {
        private var view: View = v
        private var context: Context = context
        private lateinit var systemInfo: SystemInfo

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(Constants().EXTRA_ID, systemInfo.id)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }

        fun bind(systemInfo: SystemInfo) {
            this.systemInfo = systemInfo
            if (systemInfo.status == 0) {
                view.tvItem.text = systemInfo.failTitle
                if (systemInfo.isAuto == Constants().IS_AUTO) {
                    view.imgStatus.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            context.resources,
                            R.drawable.group_alert,
                            null
                        )
                    )
                } else {
                    view.imgStatus.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            context.resources,
                            R.drawable.group_review,
                            null
                        )
                    )
                }
            } else {
                view.tvItem.text = systemInfo.successTitle
                view.imgStatus.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        context.resources,
                        R.drawable.group_updated,
                        null
                    )
                )
            }

        }
    }

}