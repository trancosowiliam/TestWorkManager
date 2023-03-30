package com.dalcim.testworkmanager.presentation.loglist

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.dalcim.testworkmanager.R
import com.dalcim.testworkmanager.database.LogDatabase

class LogListActivity : AppCompatActivity() {
    private val recLogList by lazy { findViewById<RecyclerView>(R.id.recLogList) }
    private val database by lazy { LogDatabase() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_list)

        recLogList.adapter = LogListAdapter(database.getLogs())
    }

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, LogListActivity::class.java))
        }
    }
}