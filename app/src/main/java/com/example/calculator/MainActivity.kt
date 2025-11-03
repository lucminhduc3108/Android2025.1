package com.example.calculator

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var display: TextView
    private var current = ""
    private var operator = ""
    private var firstOperand: Int? = null
    private var newOperation = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        display = findViewById(R.id.textView2)

        val numberButtons = listOf(
            R.id.button24, // 1
            R.id.button19, // 2
            R.id.button25, // 3
            R.id.button14, // 4
            R.id.button15, // 5
            R.id.button16, // 6
            R.id.button20, // 7
            R.id.button13, // 8
            R.id.button9,  // 9
            R.id.button22  // 0
        )

        for ((index, id) in numberButtons.withIndex()) {
            findViewById<Button>(id).setOnClickListener {
                if (newOperation) {
                    current = ""
                    newOperation = false
                }
                current += if (id == R.id.button22) "0" else (index + 1).toString()
                display.text = current
            }
        }

        findViewById<Button>(R.id.button12).setOnClickListener { setOperator("+") }
        findViewById<Button>(R.id.button17).setOnClickListener { setOperator("-") }
        findViewById<Button>(R.id.button11).setOnClickListener { setOperator("x") }
        findViewById<Button>(R.id.button8).setOnClickListener { setOperator("/") }

        findViewById<Button>(R.id.button18).setOnClickListener { calculate() }

        findViewById<Button>(R.id.button7).setOnClickListener {
            if (current.isNotEmpty()) {
                current = current.dropLast(1)
                display.text = if (current.isEmpty()) "0" else current
            }
        }

        findViewById<Button>(R.id.button3).setOnClickListener {
            current = ""
            display.text = "0"
        }

        findViewById<Button>(R.id.button6).setOnClickListener {
            current = ""
            operator = ""
            firstOperand = null
            display.text = "0"
        }

    }

    private fun setOperator(op: String) {
        if (current.isEmpty()) return
        firstOperand = current.toInt()
        operator = op
        newOperation = true
    }

    private fun calculate() {
        if (firstOperand == null || operator.isEmpty() || current.isEmpty()) return

        val secondOperand = current.toInt()
        val result = when (operator) {
            "+" -> firstOperand!! + secondOperand
            "-" -> firstOperand!! - secondOperand
            "x" -> firstOperand!! * secondOperand
            "/" -> if (secondOperand != 0) firstOperand!! / secondOperand else 0
            else -> 0
        }

        display.text = result.toString()
        current = result.toString()
        firstOperand = null
        operator = ""
        newOperation = true
    }
}
