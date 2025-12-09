package com.example.myapplication

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class StudentDetailActivity : AppCompatActivity() {
    private lateinit var edtStudentId: TextInputEditText
    private lateinit var edtName: TextInputEditText
    private lateinit var edtPhoneNumber: TextInputEditText
    private lateinit var edtAddress: TextInputEditText
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button
    private lateinit var btnCancel: Button
    
    private var student: Student? = null
    private var position: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_detail)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Chi Tiết Sinh Viên"

        // Initialize views
        edtStudentId = findViewById(R.id.edtStudentId)
        edtName = findViewById(R.id.edtName)
        edtPhoneNumber = findViewById(R.id.edtPhoneNumber)
        edtAddress = findViewById(R.id.edtAddress)
        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
        btnCancel = findViewById(R.id.btnCancel)

        // Get student data from intent
        student = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("student", Student::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("student")
        }
        position = intent.getIntExtra("position", -1)

        // Display student information
        student?.let {
            edtStudentId.setText(it.studentId)
            edtName.setText(it.name)
            edtPhoneNumber.setText(it.phoneNumber)
            edtAddress.setText(it.address)
        }

        // Update button listener
        btnUpdate.setOnClickListener {
            updateStudent()
        }

        // Delete button listener
        btnDelete.setOnClickListener {
            confirmDelete()
        }

        // Cancel button listener
        btnCancel.setOnClickListener {
            finish()
        }
    }

    private fun updateStudent() {
        val studentId = edtStudentId.text.toString().trim()
        val name = edtName.text.toString().trim()
        val phoneNumber = edtPhoneNumber.text.toString().trim()
        val address = edtAddress.text.toString().trim()

        // Validate inputs
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

        // Create updated student and return to MainActivity
        val updatedStudent = Student(studentId, name, phoneNumber, address)
        intent.putExtra("student", updatedStudent)
        intent.putExtra("position", position)
        intent.putExtra("action", "update")
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun confirmDelete() {
        AlertDialog.Builder(this)
            .setTitle("Xác nhận xóa")
            .setMessage("Bạn có chắc chắn muốn xóa sinh viên này?")
            .setPositiveButton("Xóa") { dialog, _ ->
                deleteStudent()
                dialog.dismiss()
            }
            .setNegativeButton("Hủy") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun deleteStudent() {
        intent.putExtra("position", position)
        intent.putExtra("action", "delete")
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
