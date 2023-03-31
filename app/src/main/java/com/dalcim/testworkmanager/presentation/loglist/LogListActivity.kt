package com.dalcim.testworkmanager.presentation.loglist

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.dalcim.testworkmanager.databinding.ActivityLogListBinding
import com.dalcim.testworkmanager.repository.BreadcrumbRepository
import com.dalcim.testworkmanager.repository.ConfigRepository

class LogListActivity : AppCompatActivity() {
    private val repository by lazy { BreadcrumbRepository(this) }
    private lateinit var binding: ActivityLogListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recLogList.adapter = LogListAdapter(repository.getBreadcrumbs().also {
            Log.i("WIL_LOG", it.toString())
        })
    }

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, LogListActivity::class.java))
        }
    }
}