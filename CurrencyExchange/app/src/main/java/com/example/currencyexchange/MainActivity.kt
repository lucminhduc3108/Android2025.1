package com.example.currencyexchange

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.abs

class MainActivity : AppCompatActivity() {

    private lateinit var spFrom: Spinner
    private lateinit var spTo: Spinner
    private lateinit var etFrom: EditText
    private lateinit var etTo: EditText

    // tỷ giá cố định theo USD
    private val rates = mapOf(
        "USD" to 1.0,
        "EUR" to 0.92,
        "VND" to 24500.0,
        "JPY" to 152.0,
        "GBP" to 0.78,
        "CNY" to 7.05,
        "KRW" to 1370.0,
        "THB" to 36.5,
        "AUD" to 1.48,
        "SGD" to 1.35,
        "INR" to 84.0
    )
    private val currencies = rates.keys.toList()

    private var updatingFrom = false
    private var updatingTo = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        spFrom = findViewById(R.id.spFrom)
        spTo   = findViewById(R.id.spTo)
        etFrom = findViewById(R.id.etFrom)
        etTo   = findViewById(R.id.etTo)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, currencies)
        spFrom.adapter = adapter
        spTo.adapter = adapter
        spFrom.setSelection(currencies.indexOf("USD"))
        spTo.setSelection(currencies.indexOf("VND"))

        val spinnerListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) { recalcFrom() }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        spFrom.onItemSelectedListener = spinnerListener
        spTo.onItemSelectedListener = spinnerListener

        etFrom.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) { if (!updatingFrom) recalcFrom() }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        etTo.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) { if (!updatingTo) recalcTo() }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun parseDoubleOrNull(text: String?): Double? =
        text?.replace(",", ".")?.trim()?.toDoubleOrNull()

    private fun convert(amount: Double, from: String, to: String): Double {
        val rFrom = rates[from] ?: 1.0
        val rTo = rates[to] ?: 1.0
        return amount / rFrom * rTo
    }

    private fun recalcFrom() {
        val src = parseDoubleOrNull(etFrom.text?.toString())
        if (src == null) { updatingTo = true; etTo.setText(""); updatingTo = false; return }
        val fromCur = spFrom.selectedItem.toString()
        val toCur = spTo.selectedItem.toString()
        val res = convert(src, fromCur, toCur)
        updatingTo = true
        etTo.setText(formatAmount(res))
        etTo.setSelection(etTo.text?.length ?: 0)
        updatingTo = false
    }

    private fun recalcTo() {
        val dst = parseDoubleOrNull(etTo.text?.toString())
        if (dst == null) { updatingFrom = true; etFrom.setText(""); updatingFrom = false; return }
        val fromCur = spFrom.selectedItem.toString()
        val toCur = spTo.selectedItem.toString()
        val res = convert(dst, toCur, fromCur)
        updatingFrom = true
        etFrom.setText(formatAmount(res))
        etFrom.setSelection(etFrom.text?.length ?: 0)
        updatingFrom = false
    }

    private fun formatAmount(x: Double): String {
        val cleaned = if (abs(x) < 1e-12) 0.0 else x
        return if (abs(cleaned - cleaned.toLong()) < 1e-9) cleaned.toLong().toString()
        else String.format("%.4f", cleaned)
    }
}
