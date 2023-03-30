package com.dalcim.testworkmanager.presentation.loglist

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.dalcim.testworkmanager.R
import com.dalcim.testworkmanager.database.LogDatabase
import com.dalcim.testworkmanager.databinding.ActivityLogListBinding

class LogListActivity : AppCompatActivity() {
    private val database by lazy { LogDatabase() }
    private lateinit var binding: ActivityLogListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recLogList.adapter = LogListAdapter(database.getLogs())
    }

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, LogListActivity::class.java))
        }
    }
}