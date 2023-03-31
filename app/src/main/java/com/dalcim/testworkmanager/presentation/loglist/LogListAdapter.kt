package com.dalcim.testworkmanager.presentation.loglist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dalcim.testworkmanager.databinding.LogItemBinding
import com.dalcim.testworkmanager.domain.Breadcrumb
import com.dalcim.testworkmanager.ext.format
import java.util.*

class LogListAdapter(private val breadcrumbs: List<Breadcrumb>) :
    RecyclerView.Adapter<LogListAdapter.LogViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder {
        val binding = LogItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LogViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
        holder.bind(breadcrumbs[position])
    }

    override fun getItemCount(): Int {
        return breadcrumbs.size
    }


    inner class LogViewHolder(private val binding: LogItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(breadcrumbs: Breadcrumb) {
            binding.txtEventName.text = "${breadcrumbs.where}.${breadcrumbs.event}"
            binding.txtEventDate.text = breadcrumbs.date.format()
        }
    }
}