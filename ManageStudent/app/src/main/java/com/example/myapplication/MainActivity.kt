package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var lvStudents: ListView
    private val students = mutableListOf<Student>()
    private lateinit var adapter: StudentAdapter

    // Activity result launcher for adding student
    private val addStudentLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let { data ->
                val student = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    data.getParcelableExtra("student", Student::class.java)
                } else {
                    @Suppress("DEPRECATION")
                    data.getParcelableExtra("student")
                }
                student?.let {
                    students.add(it)
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }

    // Activity result launcher for viewing/editing student details
    private val studentDetailLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let { data ->
                val action = data.getStringExtra("action")
                val position = data.getIntExtra("position", -1)
                
                when (action) {
                    "update" -> {
                        if (position != -1) {
                            val student = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                data.getParcelableExtra("student", Student::class.java)
                            } else {
                                @Suppress("DEPRECATION")
                                data.getParcelableExtra("student")
                            }
                            student?.let {
                                students[position] = it
                                adapter.notifyDataSetChanged()
                            }
                        }
                    }
                    "delete" -> {
                        if (position != -1) {
                            students.removeAt(position)
                            adapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        lvStudents = findViewById(R.id.lvStudents)

        // Initialize adapter
        adapter = StudentAdapter(this, students)
        lvStudents.adapter = adapter

        // Add some sample data
        initializeSampleData()

        // List item click listener - open detail activity
        lvStudents.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(this, StudentDetailActivity::class.java)
            intent.putExtra("student", students[position])
            intent.putExtra("position", position)
            studentDetailLauncher.launch(intent)
        }
    }

    private fun initializeSampleData() {
        students.addAll(
            listOf(
                Student("20225810", "Luc Minh Duc", "01", "Hà Nội")
            )
        )
        adapter.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_add_student -> {
                val intent = Intent(this, AddStudentActivity::class.java)
                addStudentLauncher.launch(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}