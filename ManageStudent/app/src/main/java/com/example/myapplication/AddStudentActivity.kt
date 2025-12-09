package com.example.myapplication

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class AddStudentActivity : AppCompatActivity() {
    private lateinit var edtStudentId: TextInputEditText
    private lateinit var edtName: TextInputEditText
    private lateinit var edtPhoneNumber: TextInputEditText
    private lateinit var edtAddress: TextInputEditText
    private lateinit var btnSave: Button
    private lateinit var btnCancel: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_student)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Thêm Sinh Viên"

        // Initialize views
        edtStudentId = findViewById(R.id.edtStudentId)
        edtName = findViewById(R.id.edtName)
        edtPhoneNumber = findViewById(R.id.edtPhoneNumber)
        edtAddress = findViewById(R.id.edtAddress)
        btnSave = findViewById(R.id.btnSave)
        btnCancel = findViewById(R.id.btnCancel)

        // Save button listener
        btnSave.setOnClickListener {
            saveStudent()
        }

        // Cancel button listener
        btnCancel.setOnClickListener {
            finish()
        }
    }

    private fun saveStudent() {
        val studentId = edtStudentId.text.toString().trim()
        val name = edtName.text.toString().trim()
        val phoneNumber = edtPhoneNumber.text.toString().trim()
        val address = edtAddress.text.toString().trim()

        // Validate inputs
        if (studentId.isEmpty()) {
            edtStudentId.error = "Vui lòng nhập MSSV"
            edtStudentId.requestFocus()
            return
        }

        if (name.isEmpty()) {
            edtName.error = "Vui lòng nhập họ tên"
            edtName.requestFocus()
            return
        }

        if (phoneNumber.isEmpty()) {
            edtPhoneNumber.error = "Vui lòng nhập số điện thoại"
            edtPhoneNumber.requestFocus()
            return
        }

        if (address.isEmpty()) {
            edtAddress.error = "Vui lòng nhập địa chỉ"
            edtAddress.requestFocus()
            return
        }

        // Create student and return to MainActivity
        val student = Student(studentId, name, phoneNumber, address)
        intent.putExtra("student", student)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
