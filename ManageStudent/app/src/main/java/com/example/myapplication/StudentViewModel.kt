package com.example.myapplication

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class StudentViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: StudentRepository = (application as StudentApplication).repository

    val students: LiveData<List<Student>> = repository.allStudents

    private val _currentStudent = MutableLiveData<Student?>()
    val currentStudent: LiveData<Student?> = _currentStudent

    private val _currentPosition = MutableLiveData<Int>()
    val currentPosition: LiveData<Int> = _currentPosition

    fun addStudent(student: Student) {
        repository.insertStudent(student)
    }

    fun updateStudent(student: Student) {
        repository.updateStudent(student)
    }

    fun deleteStudent(studentId: String) {
        repository.deleteStudent(studentId)
    }

    fun setCurrentStudent(student: Student?, position: Int = -1) {
        _currentStudent.value = student
        _currentPosition.value = position
    }

    fun clearCurrentStudent() {
        _currentStudent.value = null
        _currentPosition.value = -1
    }
}
