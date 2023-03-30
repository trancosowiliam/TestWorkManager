package com.dalcim.testworkmanager.presentation.loglist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dalcim.testworkmanager.R
import com.dalcim.testworkmanager.domain.LogEntity
import com.dalcim.testworkmanager.ext.format
import java.util.*

class LogListAdapter(private val logItems: List<LogEntity>) :
    RecyclerView.Adapter<LogListAdapter.LogViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.log_item, parent, false)
        return LogViewHolder(view)
    }

    override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
        holder.bind(logItems[position])
    }

    override fun getItemCount(): Int {
        return logItems.size
    }


    inner class LogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val eventName by lazy { itemView.findViewById<TextView>(R.id.txtEventName) }
        private val eventDate by lazy { itemView.findViewById<TextView>(R.id.txtEventDate) }

        fun bind(logEntity: LogEntity) {
            eventName.text = logEntity.event
            eventDate.text = Date(logEntity.time).format()
        }
    }
}