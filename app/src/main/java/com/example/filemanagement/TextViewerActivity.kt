package com.example.filemanagement

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import java.io.File

class TextViewerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_viewer)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        val filePath = intent.getStringExtra("file_path") ?: return
        val file = File(filePath)
        
        supportActionBar?.title = file.name

        val textContent: TextView = findViewById(R.id.textContent)
        
        try {
            val content = file.readText()
            textContent.text = content
        } catch (e: Exception) {
            textContent.text = "Error reading file: ${e.message}"
        }
    }
}
