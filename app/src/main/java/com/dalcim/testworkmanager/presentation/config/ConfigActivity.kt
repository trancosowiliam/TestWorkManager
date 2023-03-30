package com.dalcim.testworkmanager.presentation.config

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import com.dalcim.testworkmanager.R
import com.dalcim.testworkmanager.databinding.ActivityConfigBinding
import com.dalcim.testworkmanager.domain.WorkerConfig
import java.lang.Exception

class ConfigActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConfigBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfigBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupListeners()
    }

    private fun setupView() {
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.frequency,
            android.R.layout.simple_spinner_item
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerFrequency.adapter = adapter

        binding.spinnerFrequency.setSelection(0)
        binding.edtFrequency.setText("0")
        binding.edtReturnSuccess.setText("0")
        binding.edtReturnFailure.setText("0")
        binding.edtReturnRetry.setText("0")
    }

    private fun setupListeners() {
        binding.btnSave.setOnClickListener {
            save()
        }

        binding.edtReturnSuccess.addTextChangedListener(ratioTextChangedListener())
        binding.edtReturnFailure.addTextChangedListener(ratioTextChangedListener())
        binding.edtReturnRetry.addTextChangedListener(ratioTextChangedListener())
    }

    private fun save() {
        if(validateConfig().not()) {
            return
        }

        val spinnerIndexSelected = binding.spinnerFrequency.selectedItemPosition

        val frequency = binding.edtFrequency.text?.toString()?.toInt() ?: 0
        val frequencyUnit = when (spinnerIndexSelected) {
            0 -> WorkerConfig.FrequencyUnit.MINUTE
            else -> WorkerConfig.FrequencyUnit.HOUR
        }
        val successRatio = binding.edtReturnSuccess.intValue
        val failureRatio = binding.edtReturnFailure.intValue
        val retryRatio = binding.edtReturnRetry.intValue

        val workerConfig = WorkerConfig(
            frequency = frequency,
            frequencyUnit = frequencyUnit,
            successRatio = successRatio,
            failureRatio = failureRatio,
            retryRatio = retryRatio
        )

        Log.i("WIL_LOG", workerConfig.toString())

    }

    private fun validateConfig(): Boolean {
        val total = binding.edtReturnSuccess.intValue +
                binding.edtReturnFailure.intValue +
                binding.edtReturnRetry.intValue

        val errorMessage = if (total < 1) {
            "Adicione um peso".also {
                showMessage(it)
            }
        } else if (binding.edtFrequency.intValue < 15 && binding.spinnerFrequency.selectedItemPosition == 0) {
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