package com.example.quanlysinhvien

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddStudentActivity : AppCompatActivity() {

    private lateinit var edtId: EditText
    private lateinit var edtName: EditText
    private lateinit var edtPhone: EditText
    private lateinit var edtAddress: EditText
    private lateinit var btnAdd: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_student)

        edtId = findViewById(R.id.edtStudentId)
        edtName = findViewById(R.id.edtStudentName)
        edtPhone = findViewById(R.id.edtStudentPhone)
        edtAddress = findViewById(R.id.edtStudentAddress)
        btnAdd = findViewById(R.id.btnAddStudent)

        btnAdd.setOnClickListener {
            val id = edtId.text.toString().trim()
            val name = edtName.text.toString().trim()
            val phone = edtPhone.text.toString().trim()
            val address = edtAddress.text.toString().trim()

            if (id.isEmpty() || name.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ MSSV và Họ tên", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent()
                intent.putExtra("EXTRA_ID", id)
                intent.putExtra("EXTRA_NAME", name)
                intent.putExtra("EXTRA_PHONE", phone)
                intent.putExtra("EXTRA_ADDRESS", address)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }
}
