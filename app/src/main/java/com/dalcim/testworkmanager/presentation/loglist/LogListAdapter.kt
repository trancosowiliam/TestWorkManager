package com.dalcim.testworkmanager.presentation.loglist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dalcim.testworkmanager.databinding.LogItemBinding
import com.dalcim.testworkmanager.domain.LogEntity
import com.dalcim.testworkmanager.ext.format
import java.util.*

class LogListAdapter(private val logItems: List<LogEntity>) :
    RecyclerView.Adapter<LogListAdapter.LogViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder {
        val binding = LogItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LogViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
        holder.bind(logItems[position])
    }

    override fun getItemCount(): Int {
        return logItems.size
    }


    inner class LogViewHolder(private val binding: LogItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(logEntityBkp: LogEntity) {
            binding.txtEventName.text = logEntityBkp.event
            binding.txtEventDate.text = Date(logEntityBkp.time).format()
        }
    }
}