package com.example.quanlysinhvien

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class StudentDetailActivity : AppCompatActivity() {

    private lateinit var edtId: EditText
    private lateinit var edtName: EditText
    private lateinit var edtPhone: EditText
    private lateinit var edtAddress: EditText
    private lateinit var btnUpdate: Button
    private var position = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_detail)

        edtId = findViewById(R.id.edtDetailId)
        edtName = findViewById(R.id.edtDetailName)
        edtPhone = findViewById(R.id.edtDetailPhone)
        edtAddress = findViewById(R.id.edtDetailAddress)
        btnUpdate = findViewById(R.id.btnUpdateStudent)

        // Lấy dữ liệu từ Intent
        val intent = intent
        if (intent != null) {
            edtId.setText(intent.getStringExtra("EXTRA_ID"))
            edtName.setText(intent.getStringExtra("EXTRA_NAME"))
            edtPhone.setText(intent.getStringExtra("EXTRA_PHONE"))
            edtAddress.setText(intent.getStringExtra("EXTRA_ADDRESS"))
            position = intent.getIntExtra("EXTRA_POSITION", -1)
        }

        btnUpdate.setOnClickListener {
            val id = edtId.text.toString().trim()
            val name = edtName.text.toString().trim()
            val phone = edtPhone.text.toString().trim()
            val address = edtAddress.text.toString().trim()

            if (id.isEmpty() || name.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ MSSV và Họ tên", Toast.LENGTH_SHORT).show()
            } else {
                val resultIntent = Intent()
                resultIntent.putExtra("EXTRA_ID", id)
                resultIntent.putExtra("EXTRA_NAME", name)
                resultIntent.putExtra("EXTRA_PHONE", phone)
                resultIntent.putExtra("EXTRA_ADDRESS", address)
                resultIntent.putExtra("EXTRA_POSITION", position)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
        }
    }
}
