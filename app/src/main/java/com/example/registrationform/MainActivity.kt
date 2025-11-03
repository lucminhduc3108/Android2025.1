
package com.example.registrationform

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.CalendarView
import android.widget.TextView
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class MainActivity : AppCompatActivity() {

    private val ERROR_BG = Color.parseColor("#FFCDD2")
    private val DEFAULT_BG = Color.parseColor("#DDDDDD")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val calendar = findViewById<CalendarView>(R.id.calendarView2)
        val dobView = findViewById<EditText>(R.id.editTextText5)
        val firstView   = findViewById<EditText>(R.id.editTextText)
        val lastView    = findViewById<EditText>(R.id.editTextText2)
        val emailView   = findViewById<EditText>(R.id.editTextText7)
        val addressView = findViewById<EditText>(R.id.editTextText6)
        val btnSelect = findViewById<Button>(R.id.button11)
        val btnRegister = findViewById<Button>(R.id.button)
        val rbMale    = findViewById<RadioButton>(R.id.radioButton)
        val rbFemale  = findViewById<RadioButton>(R.id.radioButton2)
        val tvGender  = findViewById<TextView>(R.id.textView5)
        val cbTerms = findViewById<CheckBox>(R.id.checkBox)


        calendar.visibility= View.GONE
        btnSelect.setOnClickListener {
            calendar.visibility =
                if (calendar.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }

        calendar.setOnDateChangeListener { _, year, month, day ->
            dobView.setText(String.format("%02d/%02d/%d", day, month + 1, year))
        }

        btnRegister.setOnClickListener {
            var ok = true

            fun checkField(et: EditText) {
                if (et.text.toString().isBlank()) {
                    et.setBackgroundColor(ERROR_BG)
                    ok = false
                } else {
                    et.setBackgroundColor(DEFAULT_BG)
                }
            }

            checkField(firstView)
            checkField(lastView)
            checkField(dobView)
            checkField(emailView)
            checkField(addressView)

            if (!rbMale.isChecked && !rbFemale.isChecked) {
                tvGender.setBackgroundColor(ERROR_BG)
                ok = false
            } else {
                tvGender.setBackgroundColor(Color.TRANSPARENT)
            }

            if (!cbTerms.isChecked) {
                cbTerms.setBackgroundColor(ERROR_BG)
                ok = false
            } else {
                cbTerms.setBackgroundColor(Color.TRANSPARENT)
            }
        }
    }
}