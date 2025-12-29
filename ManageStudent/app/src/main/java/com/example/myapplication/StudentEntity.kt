package com.example.myapplication

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "students")
data class StudentEntity(
    @PrimaryKey
    val studentId: String,
    val name: String,
    val phoneNumber: String,
    val address: String
) {
    fun toStudent(): Student {
        return Student(studentId, name, phoneNumber, address)
    }
}

fun Student.toEntity(): StudentEntity {
    return StudentEntity(studentId, name, phoneNumber, address)
}

