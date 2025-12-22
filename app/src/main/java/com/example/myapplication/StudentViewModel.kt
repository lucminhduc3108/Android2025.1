package com.example.myapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StudentViewModel : ViewModel() {
    private val _students = MutableLiveData<MutableList<Student>>()
    val students: LiveData<MutableList<Student>> = _students

    private val _currentStudent = MutableLiveData<Student?>()
    val currentStudent: LiveData<Student?> = _currentStudent

    private val _currentPosition = MutableLiveData<Int>()
    val currentPosition: LiveData<Int> = _currentPosition

    init {
        _students.value = mutableListOf(
            Student("20225810", "Luc Minh Duc", "0123456789", "Hà Nội")
        )
    }

    fun addStudent(student: Student) {
        val list = _students.value ?: mutableListOf()
        list.add(student)
        _students.value = list
    }

    fun updateStudent(position: Int, student: Student) {
        val list = _students.value ?: return
        if (position in list.indices) {
            list[position] = student
            _students.value = list
        }
    }

    fun deleteStudent(position: Int) {
        val list = _students.value ?: return
        if (position in list.indices) {
            list.removeAt(position)
            _students.value = list
        }
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
