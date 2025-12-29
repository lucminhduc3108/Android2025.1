package com.example.myapplication

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StudentRepository(application: Application) {
    private val studentDao: StudentDao = AppDatabase.getDatabase(application).studentDao()

    val allStudents: LiveData<List<Student>> = studentDao.getAllStudents().map { entities ->
        entities.map { it.toStudent() }
    }

    fun insertStudent(student: Student) {
        CoroutineScope(Dispatchers.IO).launch {
            studentDao.insertStudent(student.toEntity())
        }
    }

    fun updateStudent(student: Student) {
        CoroutineScope(Dispatchers.IO).launch {
            studentDao.updateStudent(student.toEntity())
        }
    }

    fun deleteStudent(studentId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            studentDao.deleteStudentById(studentId)
        }
    }

    suspend fun getStudentById(studentId: String): Student? {
        return studentDao.getStudentById(studentId)?.toStudent()
    }
}

