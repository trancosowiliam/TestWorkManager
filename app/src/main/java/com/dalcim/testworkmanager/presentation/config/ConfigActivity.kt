package com.dalcim.testworkmanager.presentation.config

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dalcim.testworkmanager.R
import com.dalcim.testworkmanager.databinding.ActivityConfigBinding
import com.dalcim.testworkmanager.domain.WorkerConfig
import com.dalcim.testworkmanager.repository.ConfigRepository

class ConfigActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConfigBinding
    private val repository by lazy { ConfigRepository(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfigBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupListeners()
        loadConfig()
    }

    private fun setupView() {
        val intervalAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.interval_unit,
            android.R.layout.simple_spinner_item
        )

        val policyAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.retry_policy,
            android.R.layout.simple_spinner_item
        )

        intervalAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerInterval.adapter = intervalAdapter
        binding.spinnerRetryInterval.adapter = intervalAdapter
        binding.spinnerRetryPolicy.adapter = policyAdapter
    }

    private fun setupListeners() {
        binding.btnSave.setOnClickListener {
            save()
        }

        binding.edtReturnSuccess.addTextChangedListener(ratioTextChangedListener())
        binding.edtReturnFailure.addTextChangedListener(ratioTextChangedListener())
        binding.edtReturnRetry.addTextChangedListener(ratioTextChangedListener())
    }

    private fun loadConfig() {
        val config = repository.getConfig()
        binding.edtInterval.setText(config.interval.toString())
        binding.spinnerInterval.setSelection(
            when(config.intervalUnit) {
                WorkerConfig.IntervalUnit.MINUTE -> 0
                else -> 1
            }
        )
        binding.edtRetryInterval.setText(config.retryInterval.toString())
        binding.spinnerRetryInterval.setSelection(
            when(config.retryIntervalUnit) {
                WorkerConfig.IntervalUnit.MINUTE -> 0
                else -> 1
            }
        )
        binding.spinnerRetryPolicy.setSelection(
            when(config.retryPolicy) {
                WorkerConfig.RetryPolicy.EXPONENTIAL -> 0
                else -> 1
            }
        )
        binding.edtReturnSuccess.setText(config.successRatio.toString())
        binding.edtReturnFailure.setText(config.failureRatio.toString())
        binding.edtReturnRetry.setText(config.retryRatio.toString())
    }

    private fun save() {
        if(validateConfig().not()) {
            return
        }

        val spinnerIntervalIndexSelected = binding.spinnerInterval.selectedItemPosition
        val spinnerRetryIntervalIndexSelected = binding.spinnerRetryInterval.selectedItemPosition
        val spinnerRetryPolicyIndexSelected = binding.spinnerRetryPolicy.selectedItemPosition

        val interval = binding.edtInterval.text?.toString()?.toInt() ?: 0
        val intervalUnit = when (spinnerIntervalIndexSelected) {
            0 -> WorkerConfig.IntervalUnit.MINUTE
            else -> WorkerConfig.IntervalUnit.HOUR
        }
        val retryInterval = binding.edtRetryInterval.text?.toString()?.toInt() ?: 0
        val retryIntervalUnit = when (spinnerRetryIntervalIndexSelected) {
            0 -> WorkerConfig.IntervalUnit.MINUTE
            else -> WorkerConfig.IntervalUnit.HOUR
        }
        val retryPolicy = when (spinnerRetryPolicyIndexSelected) {
            0 -> WorkerConfig.RetryPolicy.EXPONENTIAL
            else -> WorkerConfig.RetryPolicy.LINEAR
        }

        val successRatio = binding.edtReturnSuccess.intValue
        val failureRatio = binding.edtReturnFailure.intValue
        val retryRatio = binding.edtReturnRetry.intValue

        val workerConfig = WorkerConfig(
            interval = interval,
            intervalUnit = intervalUnit,
            retryInterval = retryInterval,
            retryIntervalUnit = retryIntervalUnit,
            retryPolicy = retryPolicy,
            successRatio = successRatio,
            failureRatio = failureRatio,
            retryRatio = retryRatio
        )

        saveInDatabase(workerConfig)
    }

    private fun saveInDatabase(workerConfig: WorkerConfig) {
        Log.i("WIL_LOG", workerConfig.toString())

        if(repository.saveConfig(workerConfig)) {
            showMessage("Salvo com sucesso")
            finish()
        }
    }

    private fun validateConfig(): Boolean {
        val total = binding.edtReturnSuccess.intValue +
                binding.edtReturnFailure.intValue +
                binding.edtReturnRetry.intValue

        val errorMessage = if (total < 1) {
            "Adicione um peso".also {
                showMessage(it)
            }
        } else if (binding.edtInterval.intValue < 15 && binding.spinnerInterval.selectedItemPosition == 0) {
            "A Frenquencia minima é 15 minutos".also {
                showMessage(it)
            }
        } else {
            ""
        }

        return errorMessage.isEmpty()
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun ratioTextChangedListener(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                val total = binding.edtReturnSuccess.intValue +
                        binding.edtReturnFailure.intValue +
                        binding.edtReturnRetry.intValue

                binding.lblReturnSuccessRatio.text = "${binding.edtReturnSuccess.intValue}/$total"
                binding.lblReturnFailureRatio.text = "${binding.edtReturnFailure.intValue}/$total"
                binding.lblReturnRetryRatio.text = "${binding.edtReturnRetry.intValue}/$total"
            }
        }
    }

    private val EditText.intValue: Int
        get() = try {
            this.text.toString().toInt()
        } catch (e: Exception) {
            0
        }

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, ConfigActivity::class.java))
        }
    }
}